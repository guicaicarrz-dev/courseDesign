package ys.rg.fourClass.dto;

import lombok.Data;

@Data
public class AlgorithmRequestDTO {
    //实验id
    private Integer experimentId;
    //算法类型
    private String algorithmType;
    //算法状态
    private String command;
}
