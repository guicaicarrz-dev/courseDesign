package ys.rg.fourClass.service;

import ys.rg.fourClass.dto.ExperimentalResultsDTO;

import java.util.List;

public interface QueryExperimentResultService {
    /**
     * 根据ID获取实验结果
     * @param id 实验结果ID
     * @return 实验结果
     */
    List<ExperimentalResultsDTO> getExperimentalResults(Integer id);

}
