package ys.rg.fourClass.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("experiment")
public class Experiment {
    private Integer experimentId;
    private String experimentName;
}
