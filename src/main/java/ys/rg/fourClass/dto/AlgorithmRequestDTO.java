package ys.rg.fourClass.dto;

import lombok.Data;

@Data
public class AlgorithmRequestDTO {
    //算法类型
    private String algorithmType;
    //begin=1/stop=0
    private boolean action=false;
}
