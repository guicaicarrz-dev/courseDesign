package ys.rg.fourClass.service;

import ys.rg.fourClass.dto.ExperimentDTO;
import ys.rg.fourClass.dto.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExperimentService {
    Result deleteExperiment(Integer id);

    List<ExperimentDTO> getExperiment(int page, int size);

    Result deriveExperiment(HttpServletResponse response);
}
