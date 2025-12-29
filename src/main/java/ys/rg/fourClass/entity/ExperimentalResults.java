package ys.rg.fourClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import ys.rg.fourClass.handler.*;

import java.util.List;
import java.util.Map;

@Data
@TableName(value="experimental_results",autoResultMap = true)
public class ExperimentalResults {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //算法类型
    private String algorithmType;
    //实验id
    private Integer experimentId;
    //物理页号序列
    @TableField(typeHandler = IntegerList2JsonTypeHandler.class)
    private List<Integer> physicalPageNumberSequenceJson;
    //内存地址序列
    @TableField(typeHandler = StringList2JsonTypeHandler.class)
    private List<String> memoryAddressSequenceJson;
    //单次存取时间序列
    @TableField(typeHandler = LongList2JsonTypeHandler.class)
    private List<Long> singleAccessTimeSequenceJson;
    //TLB命中状态序列
    @TableField(typeHandler = BooleanList2JsonTypeHandler.class)
    private List<Boolean> tlbHitStatusSequenceJson;
    //驻留内存集序列
    @TableField(typeHandler = IntegerList2JsonTypeHandler.class)
    private List<Integer> residentMemorySetSequenceJson;
    //TLB命中率
    private Double tlbHitRate;
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
    //平均实际时间
    private Double averageRealRunningTime;
    //结果创建时间
    private String createTime;
    //逻辑删除标记
    private Boolean isDeleted;
}
