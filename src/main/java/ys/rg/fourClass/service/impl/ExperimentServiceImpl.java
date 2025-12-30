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
    public Result deriveExperiment(String path) {
        try {
            // 参数校验
            if (path == null || path.trim().isEmpty()) {
                return Result.fail("导出路径不能为空");
            }

            // 查询所有未逻辑删除的实验
            LambdaQueryWrapper<Experiment> experimentWrapper = new LambdaQueryWrapper<>();
            experimentWrapper.eq(Experiment::getIsDelete, false)
                            .orderByDesc(Experiment::getCreateTime);
            List<Experiment> experiments = experimentMapper.selectList(experimentWrapper);

            if (experiments.isEmpty()) {
                return Result.fail("没有可导出的实验数据");
            }

            // 创建导出数据列表
            List<ExcelExperimentalResultsDTO> exportData = new ArrayList<>();

            // 遍历每个实验，查询对应的实验结果
            for (Experiment experiment : experiments) {
                // 查询该实验的所有结果
                LambdaQueryWrapper<ExperimentalResults> resultWrapper = new LambdaQueryWrapper<>();
                resultWrapper.eq(ExperimentalResults::getExperimentId, experiment.getExperimentId())
                           .eq(ExperimentalResults::getIsDeleted, false)
                           .orderByDesc(ExperimentalResults::getCreateTime);
                List<ExperimentalResults> results = experimentalResultsMapper.selectList(resultWrapper);

                if (results.isEmpty()) {
                    // 如果没有实验结果，仍然导出实验信息
                    ExcelExperimentalResultsDTO dto = createEmptyResultDTO(experiment);
                    exportData.add(dto);
                } else {
                    // 为每个实验结果创建一行数据
                    for (ExperimentalResults result : results) {
                        ExcelExperimentalResultsDTO dto = createMergedDTO(experiment, result);
                        exportData.add(dto);
                    }
                }
            }

            // 使用EasyExcel导出到文件
            EasyExcel.write(path, ExcelExperimentalResultsDTO.class)
                    .sheet("实验结果导出")
                    .doWrite(exportData);

            return Result.ok("导出成功，文件路径：" + path);

        } catch (Exception e) {
            return Result.fail("导出失败：" + e.getMessage());
        }
    }

    /**
     * 创建空的实验结果DTO（只有实验信息，没有实验结果）
     */
    private ExcelExperimentalResultsDTO createEmptyResultDTO(Experiment experiment) {
        ExcelExperimentalResultsDTO dto = new ExcelExperimentalResultsDTO();
        
        // 设置实验信息
        BeanUtils.copyProperties(experiment, dto);
        
        // 实验结果字段保持默认值（null）
        
        return dto;
    }

    /**
     * 创建合并的DTO（实验信息 + 实验结果）
     */
    private ExcelExperimentalResultsDTO createMergedDTO(Experiment experiment, ExperimentalResults result) {
        ExcelExperimentalResultsDTO dto = new ExcelExperimentalResultsDTO();
        
        // 设置实验信息
        BeanUtils.copyProperties(experiment, dto);
        
        // 设置实验结果信息
        dto.setId(result.getId());
        dto.setAlgorithmType(result.getAlgorithmType());
        dto.setResultExperimentId(result.getExperimentId());
        
        // 处理List类型的JSON字段，转换为字符串
        dto.setPhysicalPageNumberSequenceJson(formatListToString(result.getPhysicalPageNumberSequenceJson()));
        dto.setMemoryAddressSequenceJson(formatListToString(result.getMemoryAddressSequenceJson()));
        dto.setSingleAccessTimeSequenceJson(formatListToString(result.getSingleAccessTimeSequenceJson()));
        dto.setTlbHitStatusSequenceJson(formatListToString(result.getTlbHitStatusSequenceJson()));
        dto.setResidentMemorySetSequenceJson(formatListToString(result.getResidentMemorySetSequenceJson()));
        
        // 设置其他实验结果字段
        dto.setTlbHitRate(result.getTlbHitRate());
        dto.setPageFaultCount(result.getPageFaultCount());
        dto.setPageReplacementCount(result.getPageReplacementCount());
        dto.setPageFaultRate(result.getPageFaultRate());
        dto.setPageHitRate(result.getPageHitRate());
        dto.setRealRunningTime(result.getRealRunningTime());
        dto.setEmulateRunningTime(result.getEmulateRunningTime());
        dto.setAverageRealRunningTime(result.getAverageRealRunningTime());
        dto.setResultCreateTime(result.getCreateTime());
        
        return dto;
    }

    /**
     * 将List转换为字符串格式，便于Excel显示
     */
    private <T> String formatListToString(List<T> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.toString();
    }
}

