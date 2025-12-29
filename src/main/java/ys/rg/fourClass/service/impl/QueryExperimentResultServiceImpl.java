package ys.rg.fourClass.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ys.rg.fourClass.dto.ExperimentalResultsDTO;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.mapper.ExperimentalResultsMapper;
import ys.rg.fourClass.service.QueryExperimentResultService;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryExperimentResultServiceImpl implements QueryExperimentResultService {

    private final ExperimentalResultsMapper experimentalResultsMapper;

    public QueryExperimentResultServiceImpl(ExperimentalResultsMapper experimentalResultsMapper) {
        this.experimentalResultsMapper = experimentalResultsMapper;
    }

    @Override
    public List<ExperimentalResultsDTO> getExperimentalResults(Integer id) {
        List<ExperimentalResultsDTO> resultList = new ArrayList<>();
        
        if (id == null) {
            return resultList;
        }
        
        // 根据ID查询实验结果实体
        ExperimentalResults experimentalResults = experimentalResultsMapper.selectById(id);
        
        if (experimentalResults != null) {
            // 将实体转换为DTO
            ExperimentalResultsDTO dto = new ExperimentalResultsDTO();
            BeanUtils.copyProperties(experimentalResults, dto);
            resultList.add(dto);
        }
        
        return resultList;
    }
}
