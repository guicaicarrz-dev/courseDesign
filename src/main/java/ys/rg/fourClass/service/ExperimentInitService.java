package ys.rg.fourClass.service;

import org.springframework.stereotype.Service;
import ys.rg.fourClass.dto.ExperimentInitDTO;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.mapper.ExperimentMapper;
import ys.rg.fourClass.util.PagingAddressConverter;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 功能：接收初始化参数，存入experiment表，返回自增experimentId
 * 用法：被ExperimentInitController调用，完成参数持久化
 */
@Service
public class ExperimentInitService {

    @Resource
    private ExperimentMapper experimentMapper;

    // 初始化参数并存库
    public Integer initAndSaveToDb(ExperimentInitDTO initDTO) {
        Experiment experiment = new Experiment();
        experiment.setExperimentName(initDTO.getExperimentName());
        List<String> logicalAddresses = PagingAddressConverter.parseAddressString(initDTO.getLogicalAddressSequence());
        experiment.setLogicalAddressSequenceJson(logicalAddresses);
        experiment.setLogicalPageNumberSequenceJson(PagingAddressConverter.getLogicalPageNumbers(logicalAddresses));
        experiment.setLogicalAddressSequenceSize(logicalAddresses.size());
        experiment.setResidentMemorySetCount(initDTO.getResidentMemorySetCount());
        experiment.setTlbSize(initDTO.getTlbSize());
        experiment.setIsUseTlb(initDTO.getIsUseTlb());
        experiment.setMemoryAccessTime(initDTO.getMemoryAccessTime());
        experiment.setFastTableAccessTime(initDTO.getFastTableAccessTime());
        experiment.setPageFaultTime(initDTO.getPageFaultTime());
        experiment.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        experiment.setIsDelete(false);

        experimentMapper.insert(experiment); // 存入数据库，自增experimentId
        return experiment.getExperimentId();
    }
    // 根据experimentId查询实验数据（算法执行时调用）
    public Experiment getParamsFromDb(Integer experimentId) {
        return experimentMapper.selectById(experimentId);
    }
}
