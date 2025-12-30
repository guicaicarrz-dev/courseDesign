package ys.rg.fourClass.task;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.handler.AlgorithmWebSocketHandler;
import ys.rg.fourClass.service.ExperimentalResultsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public PageReplacementTask(Integer experimentId, String algorithmType, Experiment experiment, ExperimentalResultsService resultsService, AlgorithmWebSocketHandler webSocketHandler) {
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
        try {
            // 读取实验数据（所有算法共用同一份只读数据）
            List<String> addressList = experiment.getLogicalAddressSequenceJson();
            int memorySetCount = experiment.getResidentMemorySetCount();
            int total = addressList.size();

            // 推送初始进度
            sendProgress(0, "启动" + algorithmType + "算法");

            // 核心：算法执行逻辑（以FIFO为例，替换为你的LRU/OPT）
            for (int i = 0; i < total; i++) {
                if (isStopped) {
                    sendProgress(-1, algorithmType + "算法已停止");
                    return;
                }

                // 检查暂停
                lock.lock();
                try {
                    while (isPaused) pauseCondition.await();
                } finally {
                    lock.unlock();
                }

                // ========== 你的算法核心逻辑 ==========
                String currentAddress = addressList.get(i);
                // FIFO/LRU/OPT算法计算：生成物理页号、TLB命中状态、存取时间等
                // =====================================

                // 推送进度（每10条推一次）
                int progress = (i + 1) * 100 / total;
                if (i % 10 == 0 || i == total - 1) {
                    sendProgress(progress, "处理地址：" + currentAddress);
                }

                Thread.sleep(50); // 模拟耗时（可删除）
            }

            // 算法完成，保存结果
            sendProgress(100, algorithmType + "算法执行完成");
            ExperimentalResults experimentalResults = new ExperimentalResults();
            /**
             * 待补充结果数据
             */




            resultsService.saveAlgorithmResult(experimentalResults);

        } catch (InterruptedException e) {
            sendProgress(-1, algorithmType + "算法被中断");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            sendProgress(-1, algorithmType + "算法失败：" + e.getMessage());
        }
    }

    // 推送进度到前端
    private void sendProgress(int progress, String message) {
        // 步骤1：构建data子Map
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("progress", progress);
        dataMap.put("message", message);

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
