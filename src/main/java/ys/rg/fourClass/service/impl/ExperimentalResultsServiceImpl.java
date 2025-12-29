package ys.rg.fourClass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.mapper.ExperimentalResultsMapper;
import ys.rg.fourClass.service.ExperimentalResultsService;

@Service
public class ExperimentalResultsServiceImpl extends ServiceImpl<ExperimentalResultsMapper, ExperimentalResults> implements ExperimentalResultsService{

    @Autowired
    private ExperimentalResultsMapper experimentalResultsMapper;
    @Override
    public ExperimentalResults getExperimentalResultsById(Integer id)
    {
        return experimentalResultsMapper.selectById(id);
    }
}
