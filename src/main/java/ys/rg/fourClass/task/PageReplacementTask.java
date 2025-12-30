package ys.rg.fourClass.task;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.handler.AlgorithmWebSocketHandler;
import ys.rg.fourClass.service.ExperimentalResultsService;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能：封装单个算法的执行逻辑，支持暂停/继续/停止，实时推送进度
 * 用法：被AlgorithmService创建，提交到线程池执行，核心是run()方法中的算法逻辑
 */
@Data
public class PageReplacementTask implements Runnable {
    // 实验参数
    private final Integer experimentId;
    private final String algorithmType;
    private final Experiment experiment;
    // 依赖服务
    private final ExperimentalResultsService resultsService;
    private final AlgorithmWebSocketHandler webSocketHandler;
    // 控制锁
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition pauseCondition = lock.newCondition();
    private volatile boolean isPaused = false; // 暂停标记
    private volatile boolean isStopped = false; // 停止标记

    // 算法执行状态
    private List<Integer> memoryList = new ArrayList<>(); // 内存页序列
    private List<Integer> tlbList = new ArrayList<>(); // 快表页序列
    private List<Integer> physicalPageSequence = new ArrayList<>(); // 物理页号序列
    private List<String> memoryAddressSequence = new ArrayList<>(); // 内存地址序列
    private List<Long> singleAccessTimeSequence = new ArrayList<>(); // 单次存取时间序列
    private List<Boolean> tlbHitStatusSequence = new ArrayList<>(); // TLB命中状态序列
    private List<Integer> residentMemorySetSequence = new ArrayList<>(); // 驻留内存集序列
    private List<Boolean> pageFaultSequence = new ArrayList<>(); // 缺页状态序列
    private int pageFaultCount = 0; // 缺页次数
    private int pageReplacementCount = 0; // 页面置换次数
    private int tlbHitCount = 0; // TLB命中次数

    public PageReplacementTask(Integer experimentId, String algorithmType, Experiment experiment,
                               ExperimentalResultsService resultsService, AlgorithmWebSocketHandler webSocketHandler) {
        this.experimentId = experimentId;
        this.algorithmType = algorithmType;
        this.experiment = experiment;
        this.resultsService = resultsService;
        this.webSocketHandler = webSocketHandler;
    }

    // 暂停
    public void pause() {
        lock.lock();
        try {
            isPaused = true;
        } finally {
            lock.unlock();
        }
    }

    // 继续
    public void resume() {
        lock.lock();
        try {
            isPaused = false;
            pauseCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    // 停止
    public void stop() {
        isStopped = true;
        resume(); // 唤醒线程使其退出
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis(); // 记录实际开始时间

        try {
            // 读取实验数据
            List<String> addressList = experiment.getLogicalAddressSequenceJson();
            List<Integer> pageNumberList = experiment.getLogicalPageNumberSequenceJson();
            int memorySetCount = experiment.getResidentMemorySetCount();
            int tlbSize = experiment.getTlbSize() != null ? experiment.getTlbSize() : 0;
            boolean useTlb = experiment.getIsUseTlb() != null ? experiment.getIsUseTlb() : false;
            int memoryAccessTime = experiment.getMemoryAccessTime() != null ? experiment.getMemoryAccessTime() : 100;
            int fastTableAccessTime = experiment.getFastTableAccessTime() != null ? experiment.getFastTableAccessTime() : 10;
            int pageFaultTime = experiment.getPageFaultTime() != null ? experiment.getPageFaultTime() : 500;

            int total = addressList.size();

            // 推送初始进度
            sendProgress(0, "启动" + algorithmType + "算法", null);

            // 初始化数据结构
            memoryList = new ArrayList<>();
            tlbList = new ArrayList<>();
            physicalPageSequence = new ArrayList<>();
            memoryAddressSequence = new ArrayList<>();
            singleAccessTimeSequence = new ArrayList<>();
            tlbHitStatusSequence = new ArrayList<>();
            residentMemorySetSequence = new ArrayList<>();
            pageFaultSequence = new ArrayList<>();

            // FIFO队列用于FIFO算法
            Queue<Integer> fifoQueue = new LinkedList<>();
            // LRU队列用于LRU算法
            LinkedHashMap<Integer, Long> lruMap = new LinkedHashMap<>(memorySetCount, 0.75f, true);

            // 核心：算法执行逻辑
            for (int i = 0; i < total; i++) {
                if (isStopped) {
                    sendProgress(-1, algorithmType + "算法已停止", null);
                    return;
                }

                // 检查暂停
                lock.lock();
                try {
                    while (isPaused) {
                        pauseCondition.await();
                    }
                } finally {
                    lock.unlock();
                }

                // 获取当前访问的地址和页号
                String currentAddress = addressList.get(i);
                int currentPageNumber = pageNumberList.get(i);

                // 计算存储时间、TLB命中状态、缺页状态
                boolean tlbHit = false;
                boolean pageFault = false;
                long accessTime = 0;

                // 第一步：检查TLB是否命中（如果使用TLB）
                if (useTlb && tlbSize > 0) {
                    if (tlbList.contains(currentPageNumber)) {
                        // TLB命中
                        tlbHit = true;
                        tlbHitCount++;
                        accessTime += fastTableAccessTime;

                        // 更新LRU顺序（如果使用LRU算法）
                        if ("LRU".equals(algorithmType)) {
                            updateTLBForLRU(currentPageNumber, tlbList, tlbSize);
                        }
                    } else {
                        // TLB未命中
                        accessTime += fastTableAccessTime + memoryAccessTime;

                        // TLB置换（如果TLB已满）
                        if (tlbList.size() >= tlbSize) {
                            // TLB置换算法（FIFO）
                            tlbList.remove(0);
                        }
                        // 将新页号加入TLB
                        tlbList.add(currentPageNumber);
                    }
                } else {
                    // 不使用TLB，直接访问内存
                    accessTime += memoryAccessTime;
                }

                // 第二步：检查内存是否命中
                if (!memoryList.contains(currentPageNumber)) {
                    // 缺页处理
                    pageFault = true;
                    pageFaultCount++;
                    accessTime += pageFaultTime;

                    // 内存置换
                    if (memoryList.size() >= memorySetCount) {
                        // 根据算法类型进行页面置换
                        int replacedPage = -1;
                        switch (algorithmType) {
                            case "FIFO":
                                replacedPage = fifoQueue.poll();
                                memoryList.remove(Integer.valueOf(replacedPage));
                                break;
                            case "LRU":
                                // LRU置换：移除最久未使用的页
                                Map.Entry<Integer, Long> oldestEntry = null;
                                for (Map.Entry<Integer, Long> entry : lruMap.entrySet()) {
                                    if (oldestEntry == null || entry.getValue() < oldestEntry.getValue()) {
                                        oldestEntry = entry;
                                    }
                                }
                                if (oldestEntry != null) {
                                    replacedPage = oldestEntry.getKey();
                                    lruMap.remove(replacedPage);
                                    memoryList.remove(Integer.valueOf(replacedPage));
                                }
                                break;
                            case "OPT":
                                // OPT置换：查找未来最久不会被使用的页
                                replacedPage = findOptimalReplacement(pageNumberList, i, memoryList);
                                memoryList.remove(Integer.valueOf(replacedPage));
                                break;
                        }
                        pageReplacementCount++;
                    }

                    // 将新页加入内存
                    memoryList.add(currentPageNumber);
                    fifoQueue.offer(currentPageNumber);
                    lruMap.put(currentPageNumber, System.currentTimeMillis());
                } else {
                    // 页面命中，更新LRU时间戳
                    if ("LRU".equals(algorithmType)) {
                        lruMap.put(currentPageNumber, System.currentTimeMillis());
                    }
                }

                // 记录物理页号（这里简化处理，假设物理页号等于逻辑页号+100）
                int physicalPageNumber = currentPageNumber + 100;
                physicalPageSequence.add(physicalPageNumber);

                // 记录内存地址（这里简化处理，使用物理页号计算）
                String memoryAddress = "0x" + Integer.toHexString(physicalPageNumber * 4096);
                memoryAddressSequence.add(memoryAddress);

                // 记录单次存取时间
                singleAccessTimeSequence.add(accessTime);

                // 记录TLB命中状态
                tlbHitStatusSequence.add(tlbHit);

                // 记录驻留内存集（复制当前内存状态）
                residentMemorySetSequence.addAll(new ArrayList<>(memoryList));

                // 记录缺页状态
                pageFaultSequence.add(pageFault);

                // 准备推送的数据
                Map<String, Object> stepData = new HashMap<>();
                stepData.put("currentAddress", currentAddress);
                stepData.put("currentPageNumber", currentPageNumber);
                stepData.put("memoryList", new ArrayList<>(memoryList));
                stepData.put("tlbList", new ArrayList<>(tlbList));
                stepData.put("accessTime", accessTime);
                stepData.put("tlbHit", tlbHit);
                stepData.put("pageFault", pageFault);
                stepData.put("pageFaultCount", pageFaultCount);
                stepData.put("stepIndex", i + 1);
                stepData.put("totalSteps", total);

                // 推送进度（每10条推一次，或每一步都推，这里改为每一步都推）
                int progress = (i + 1) * 100 / total;
                if (i % 10 == 0 || i == total - 1 || i < 3) {
                    sendProgress(progress, "处理地址：" + currentAddress + "，页号：" + currentPageNumber, stepData);
                }

                // 模拟处理时间（根据实际需要调整或删除）
                Thread.sleep(50);
            }

            // 算法完成，计算统计信息
            long realRunningTime = System.currentTimeMillis() - startTime;
            int totalAccesses = total;
            double tlbHitRate = totalAccesses > 0 ? (double) tlbHitCount / totalAccesses : 0;
            double pageFaultRate = totalAccesses > 0 ? (double) pageFaultCount / totalAccesses : 0;
            double pageHitRate = totalAccesses > 0 ? 1.0 - pageFaultRate : 0;

            // 模拟运行时间（基于单次存取时间累加）
            int emulateRunningTime = singleAccessTimeSequence.stream()
                    .mapToInt(Long::intValue)
                    .sum();
            double averageRealRunningTime = totalAccesses > 0 ? (double) emulateRunningTime / totalAccesses : 0;

            // 构建结果对象
            ExperimentalResults experimentalResults = new ExperimentalResults();
            experimentalResults.setAlgorithmType(algorithmType);
            experimentalResults.setExperimentId(experimentId);
            experimentalResults.setPhysicalPageNumberSequenceJson(physicalPageSequence);
            experimentalResults.setMemoryAddressSequenceJson(memoryAddressSequence);
            experimentalResults.setSingleAccessTimeSequenceJson(singleAccessTimeSequence);
            experimentalResults.setTlbHitStatusSequenceJson(tlbHitStatusSequence);
            experimentalResults.setResidentMemorySetSequenceJson(residentMemorySetSequence);
            experimentalResults.setTlbHitRate(tlbHitRate);
            experimentalResults.setPageFaultCount(pageFaultCount);
            experimentalResults.setPageReplacementCount(pageReplacementCount);
            experimentalResults.setPageFaultRate(pageFaultRate);
            experimentalResults.setPageHitRate(pageHitRate);
            experimentalResults.setRealRunningTime(realRunningTime);
            experimentalResults.setEmulateRunningTime(emulateRunningTime);
            experimentalResults.setAverageRealRunningTime(averageRealRunningTime);
            experimentalResults.setCreateTime(new Date().toString());
            experimentalResults.setIsDeleted(false);

            // 保存结果到数据库
            resultsService.saveAlgorithmResult(experimentalResults);

            // 推送最终完成消息
            Map<String, Object> finalData = new HashMap<>();
            finalData.put("tlbHitRate", tlbHitRate);
            finalData.put("pageFaultCount", pageFaultCount);
            finalData.put("pageReplacementCount", pageReplacementCount);
            finalData.put("pageFaultRate", pageFaultRate);
            finalData.put("pageHitRate", pageHitRate);
            finalData.put("realRunningTime", realRunningTime);

            sendProgress(100, algorithmType + "算法执行完成，结果已保存", finalData);

        } catch (InterruptedException e) {
            sendProgress(-1, algorithmType + "算法被中断", null);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            sendProgress(-1, algorithmType + "算法失败：" + e.getMessage(), null);
        }
    }

    /**
     * 更新TLB的LRU顺序
     */
    private void updateTLBForLRU(int pageNumber, List<Integer> tlbList, int tlbSize) {
        tlbList.remove(Integer.valueOf(pageNumber));
        tlbList.add(pageNumber);
    }

    /**
     * OPT算法：查找最优置换页面
     */
    private int findOptimalReplacement(List<Integer> pageNumberList, int currentIndex, List<Integer> memoryList) {
        int farthestIndex = -1;
        int pageToReplace = -1;

        for (int page : memoryList) {
            int nextUseIndex = -1;

            // 查找该页面下一次被使用的位置
            for (int i = currentIndex + 1; i < pageNumberList.size(); i++) {
                if (pageNumberList.get(i) == page) {
                    nextUseIndex = i;
                    break;
                }
            }

            // 如果之后不再使用，直接替换这个页面
            if (nextUseIndex == -1) {
                return page;
            }

            // 更新最远使用的页面
            if (nextUseIndex > farthestIndex) {
                farthestIndex = nextUseIndex;
                pageToReplace = page;
            }
        }

        return pageToReplace;
    }

    // 推送进度到前端
    private void sendProgress(int progress, String message, Map<String, Object> stepData) {
        // 步骤1：构建data子Map
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("progress", progress);
        dataMap.put("message", message);

        if (stepData != null) {
            dataMap.putAll(stepData);
        }

        // 步骤2：构建最终推送Map
        Map<String, Object> pushMap = new HashMap<>();
        pushMap.put("type", "PROGRESS");
        pushMap.put("algorithmType", algorithmType);
        pushMap.put("data", dataMap);

        // 序列化为JSON字符串
        String jsonMsg = JSON.toJSONString(pushMap);
        webSocketHandler.sendMessage(experimentId.toString(), jsonMsg);
    }
}
