package ys.rg.fourClass.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ys.rg.fourClass.dto.ExcelExperimentalResultsDTO;
import ys.rg.fourClass.dto.ExperimentDTO;
import ys.rg.fourClass.dto.Result;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.mapper.ExperimentMapper;
import ys.rg.fourClass.mapper.ExperimentalResultsMapper;
import ys.rg.fourClass.service.ExperimentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExperimentServiceImpl implements ExperimentService {

    private final ExperimentMapper experimentMapper;
    private final ExperimentalResultsMapper experimentalResultsMapper;

    public ExperimentServiceImpl(ExperimentMapper experimentMapper, ExperimentalResultsMapper experimentalResultsMapper) {
        this.experimentMapper = experimentMapper;
        this.experimentalResultsMapper = experimentalResultsMapper;
    }

    @Override
    public Result deleteExperiment(Integer id) {
        if (id == null) {
            return Result.fail("实验ID不能为空");
        }

        int result = experimentMapper.deleteById(id);
        if (result > 0) {
            return Result.ok("删除成功");
        } else {
            return Result.fail("删除失败，记录不存在");
        }
    }

    @Override
    public List<ExperimentDTO> getExperiment(int page, int size) {
        List<ExperimentDTO> resultList = new ArrayList<>();
        
        // 参数校验
        if (page <= 0 || size <= 0) {
            return resultList;
        }
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 构建查询条件（排除已逻辑删除的记录）
        LambdaQueryWrapper<Experiment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Experiment::getIsDelete, false)
                    .orderByDesc(Experiment::getCreateTime)
                    .last("LIMIT " + offset + ", " + size);
        
        // 查询实验记录
        List<Experiment> experiments = experimentMapper.selectList(queryWrapper);
        
        // 转换为DTO
        for (Experiment experiment : experiments) {
            ExperimentDTO dto = new ExperimentDTO();
            BeanUtils.copyProperties(experiment, dto);
            resultList.add(dto);
        }
        
        return resultList;
    }

    @Override
    public Result deriveExperiment(HttpServletResponse response) {
        try {
            List<Experiment> experiments = experimentMapper.selectList(
                new LambdaQueryWrapper<Experiment>()
                    .eq(Experiment::getIsDelete, false)
                    .orderByAsc(Experiment::getExperimentId)
            );

            if (experiments.isEmpty()) {
                return Result.fail("没有可导出的实验数据");
            }

            List<ExperimentalResults> results = experimentalResultsMapper.selectList(
                new LambdaQueryWrapper<ExperimentalResults>()
                    .eq(ExperimentalResults::getIsDeleted, false)
                    .orderByAsc(ExperimentalResults::getExperimentId)
            );

            Map<Integer, List<ExperimentalResults>> resultsMap = new HashMap<>();
            for (ExperimentalResults result : results) {
                resultsMap.computeIfAbsent(result.getExperimentId(), k -> new ArrayList<>()).add(result);
            }

            List<ExcelExperimentalResultsDTO> excelDataList = new ArrayList<>();
            for (Experiment experiment : experiments) {
                List<ExperimentalResults> experimentResults = resultsMap.getOrDefault(experiment.getExperimentId(), new ArrayList<>());
                for (ExperimentalResults result : experimentResults) {
                    ExcelExperimentalResultsDTO dto = new ExcelExperimentalResultsDTO();
                    dto.setExperimentId(experiment.getExperimentId());
                    dto.setExperimentName(experiment.getExperimentName());
                    dto.setLogicalAddressSequenceJson(experiment.getLogicalAddressSequenceJson() != null ? experiment.getLogicalAddressSequenceJson().toString() : null);
                    dto.setLogicalPageNumberSequenceJson(experiment.getLogicalPageNumberSequenceJson() != null ? experiment.getLogicalPageNumberSequenceJson().toString() : null);
                    dto.setLogicalAddressSequenceSize(experiment.getLogicalAddressSequenceSize());
                    dto.setResidentMemorySetCount(experiment.getResidentMemorySetCount());
                    dto.setIsUseTlb(experiment.getIsUseTlb());
                    dto.setMemoryAccessTime(experiment.getMemoryAccessTime());
                    dto.setFastTableAccessTime(experiment.getFastTableAccessTime());
                    dto.setPageFaultTime(experiment.getPageFaultTime());
                    dto.setCreateTime(experiment.getCreateTime());

                    dto.setId(result.getId());
                    dto.setAlgorithmType(result.getAlgorithmType());
                    dto.setResultExperimentId(result.getExperimentId());
                    dto.setPhysicalPageNumberSequenceJson(result.getPhysicalPageNumberSequenceJson() != null ? result.getPhysicalPageNumberSequenceJson().toString() : null);
                    dto.setMemoryAddressSequenceJson(result.getMemoryAddressSequenceJson() != null ? result.getMemoryAddressSequenceJson().toString() : null);
                    dto.setSingleAccessTimeSequenceJson(result.getSingleAccessTimeSequenceJson() != null ? result.getSingleAccessTimeSequenceJson().toString() : null);
                    dto.setTlbHitStatusSequenceJson(result.getTlbHitStatusSequenceJson() != null ? result.getTlbHitStatusSequenceJson().toString() : null);
                    dto.setResidentMemorySetSequenceJson(result.getResidentMemorySetSequenceJson() != null ? result.getResidentMemorySetSequenceJson().toString() : null);
                    dto.setTlbHitRate(result.getTlbHitRate());
                    dto.setPageFaultCount(result.getPageFaultCount());
                    dto.setPageReplacementCount(result.getPageReplacementCount());
                    dto.setPageFaultRate(result.getPageFaultRate());
                    dto.setPageHitRate(result.getPageHitRate());
                    dto.setRealRunningTime(result.getRealRunningTime());
                    dto.setEmulateRunningTime(result.getEmulateRunningTime());
                    dto.setAverageRealRunningTime(result.getAverageRealRunningTime());
                    dto.setResultCreateTime(result.getCreateTime());

                    excelDataList.add(dto);
                }
            }

            String fileName = URLEncoder.encode("实验结果数据", StandardCharsets.UTF_8.toString()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            EasyExcel.write(response.getOutputStream(), ExcelExperimentalResultsDTO.class)
                    .sheet("实验结果")
                    .doWrite(excelDataList);

            return Result.ok("导出成功");
        } catch (IOException e) {
            return Result.fail("导出失败: " + e.getMessage());
        }
    }
}

