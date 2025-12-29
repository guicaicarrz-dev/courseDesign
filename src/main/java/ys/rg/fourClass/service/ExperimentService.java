package ys.rg.fourClass.service;

import ys.rg.fourClass.dto.ExperimentDTO;
import ys.rg.fourClass.dto.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExperimentService {
    /**
     * 删除实验结果
     * @param id 实验结果ID
     * @return 删除结果
     */
    Result deleteExperiment(Integer id);
    /**
     * 获取所有实验结果（分页）
     * @param page 当前页码
     * @param size 每页大小
     * @return 实验结果分页数据
     */
    List<ExperimentDTO> getExperiment(int page, int size);
    /**
     * 导出实验结果
     * @return 文件
     */
    Result deriveExperiment();
}
