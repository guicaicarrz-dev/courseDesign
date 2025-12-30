package ys.rg.fourClass.service;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Service;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.handler.AlgorithmWebSocketHandler;
import ys.rg.fourClass.task.PageReplacementTask;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * 功能：核心算法控制，启动/暂停/继续/停止算法，管理任务上下文
 * 用法：被WebSocketHandler调用，为每个算法创建独立Task并提交到线程池
 */
@Service
public class AlgorithmService {

    @Resource(name = "algorithmExecutor")
    private ExecutorService algorithmExecutor;

    @Resource
    private ExperimentInitService experimentInitService;

    @Resource
    private ExperimentalResultsService resultsService;

    @Resource
    private AlgorithmWebSocketHandler webSocketHandler;

    // 缓存算法任务：key=experimentId_algorithmType，value=任务实例
    private final Map<String, PageReplacementTask> taskMap = new ConcurrentHashMap<>();

    // 启动算法
    public void startAlgorithm(Integer experimentId, String algorithmType) {
        Experiment experiment = experimentInitService.getParamsFromDb(experimentId);
        if (experiment == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("type", "ERROR");
            errorMap.put("data", "实验数据不存在");
            webSocketHandler.sendMessage(experimentId.toString(), JSON.toJSONString(errorMap));
            return;
        }

        // 创建算法任务
        PageReplacementTask task = new PageReplacementTask(experimentId, algorithmType, experiment, resultsService, webSocketHandler);
        String taskKey = experimentId + "_" + algorithmType;
        taskMap.put(taskKey, task);

        // 提交到线程池并行执行
        algorithmExecutor.submit(task);
    }

    // 暂停算法
    public void pauseAlgorithm(Integer experimentId, String algorithmType) {
        String taskKey = experimentId + "_" + algorithmType;
        PageReplacementTask task = taskMap.get(taskKey);
        if (task != null) task.pause();
    }

    // 继续算法
    public void resumeAlgorithm(Integer experimentId, String algorithmType) {
        String taskKey = experimentId + "_" + algorithmType;
        PageReplacementTask task = taskMap.get(taskKey);
        if (task != null) task.resume();
    }

    // 停止单个算法
    public void stopAlgorithm(Integer experimentId, String algorithmType) {
        String taskKey = experimentId + "_" + algorithmType;
        PageReplacementTask task = taskMap.remove(taskKey);
        if (task != null) task.stop();
    }

    // 停止实验下所有算法
    public void stopAllAlgorithms(Integer experimentId) {
        taskMap.keySet().removeIf(key -> key.startsWith(experimentId + "_"));
    }
}
