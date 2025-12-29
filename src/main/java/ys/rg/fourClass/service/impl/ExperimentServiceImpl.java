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
    public Result deriveExperiment() {
        return null;
    }
}

