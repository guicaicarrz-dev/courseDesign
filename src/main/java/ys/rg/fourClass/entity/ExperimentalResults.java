package ys.rg.fourClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("experimental_results")
public class ExperimentalResults {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //实验类型
    private String algorithmType;
    //实验id
    private Integer experimentId;
    //物理页号序列
    private String physicalPageNumberSequenceJson;
    //内存地址序列
    private String memoryAddressSequenceJson;
    //单次存取时间序列
    private String singleAccessTimeSequenceJson;
    //TLB命中状态序列
    private String tlbHitStatusSequenceJson;
    //TLB命中率
    private String tlbHitRateJson;
    //驻留内存集序列
    private String residentMemorySetSequenceJson;
    //缺页次数
    private Integer pageFaultCount;
    //页面置换次数
    private Integer pageReplacementCount;
    //缺页率
    private Double pageFaultRate;
    //页面命中率
    private Double pageHitRate;
    //实际时间
    private long realRunningTime;
    //模拟时间
    private Integer emulateRunningTime;
    //平均模拟存取时间
    private Double averageEmulateAccessTime;
    //结果创建时间
    private String createTime;
    //逻辑删除标记
    private Boolean isDeleted;
}
