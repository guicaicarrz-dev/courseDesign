package ys.rg.fourClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("experiment")
public class Experiment {
    //实验id
    @TableId(value = "experiment_id", type = IdType.AUTO)
    private Integer experimentId;
    //实验名称
    private String experimentName;
    //逻辑地址序列
    private String logicalAddressSequenceJson;
    //逻辑页号序列
    private String logicalPageNumberSequenceJson;
    //逻辑地址序列大小
    private Integer logicalAddressSequenceSize;
    //进程驻留内存集数量
    private Integer residentMemorySetCount;
    //是否采用TLB
    private Boolean isUseTLB;
    //内存访问时间
    private Integer memoryAccessTime;
    //快表访问时间
    private Integer fastTableAccessTime;
    //缺页时间
    private Integer pageFaultTime;
    //实验创建时间
    private String createTime;
    //逻辑删除标记
    private Boolean isDelete;
}
