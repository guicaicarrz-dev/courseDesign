package ys.rg.fourClass.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import ys.rg.fourClass.dto.ExperimentDTO;
import ys.rg.fourClass.dto.ExperimentalResultsDTO;
import ys.rg.fourClass.dto.Result;
import ys.rg.fourClass.service.ExperimentService;
import ys.rg.fourClass.service.QueryExperimentResultService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ExperimentResultController {

    @Resource
    QueryExperimentResultService queryExperimentResultService;
    @Resource
    ExperimentService  experimentService;

    @PostMapping("/getExperiment")
    public List<ExperimentDTO> getExperiment(int page,int size){
        return experimentService.getExperiment(page,size);
    }

    @PostMapping("/getExperimentalResults")
    public List<ExperimentalResultsDTO> getExperimentalResults(Integer id) {
        return queryExperimentResultService.getExperimentalResults(id);
    }

    @PostMapping("/deleteExperiment")
    public Result deleteExperiment(Integer id) {
        return experimentService.deleteExperiment(id);
    }

    @GetMapping("/deriveExperiment")
    public Result deriveExperiment(HttpServletResponse response) {
        return experimentService.deriveExperiment(response);
    }

}
