package ys.rg.fourClass.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 返回前端的实验结果DTO
 */
@Data
public class ExperimentResponseDTO {
    // 实验ID（存库后返回）
    private Integer experimentId;
    // 实验名称
    private String experimentName;
    // 原始逻辑地址序列
    private List<String> logicalAddressSequence;
    // 各算法的结果（key：算法名，value：该算法的详细结果）
    private Map<String, AlgorithmResultDTO> algorithmResultMap;
}

