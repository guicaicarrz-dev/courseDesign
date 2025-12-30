package ys.rg.fourClass.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ys.rg.fourClass.dto.ExperimentDTO;
import ys.rg.fourClass.dto.ExperimentalResultsDTO;
import ys.rg.fourClass.dto.Result;
import ys.rg.fourClass.service.ExperimentService;
import ys.rg.fourClass.service.QueryExperimentResultService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实验结果控制器
 */
@RestController
@RequestMapping("/ExperimentResult")
public class ExperimentResultController {

    @Resource
    QueryExperimentResultService queryExperimentResultService;
    @Resource
    ExperimentService  experimentService;
    /**
     * 保存实验结果
     * @param result 实验结果
     * @return 保存后的结果
     */

    /**
     * 获取所有实验结果（分页）
     * @param page 当前页码
     * @param size 每页大小
     * @return 实验结果分页数据
     */
    @PostMapping("/getExperiment")
    public List<ExperimentDTO> getExperiment(int page,int size){
        return experimentService.getExperiment(page,size);
    }
    /**
     * 根据ID获取实验结果
     * @param id 实验结果ID
     * @return 实验结果
     */
    @PostMapping("/getExperimentalResults")
    public List<ExperimentalResultsDTO> getExperimentalResults(Integer id) {
        return queryExperimentResultService.getExperimentalResults(id);
    }

    /**
     * 删除实验结果
     * @param id 实验结果ID
     * @return 删除结果
     */
    @PostMapping("/deleteExperiment")
    public Result deleteExperiment(Integer id) {
        return experimentService.deleteExperiment(id);
    }

    /**
     * 导出实验结果成表格
     * @param path 保存地址
     * @return 是否成功导出
     */
    @PostMapping("/deriveExperiment")
    public Result deriveExperiment(String path) {
        return experimentService.deriveExperiment(path);
    }

}
