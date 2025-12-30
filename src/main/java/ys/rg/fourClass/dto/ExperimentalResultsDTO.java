package ys.rg.fourClass.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import ys.rg.fourClass.handler.BooleanList2JsonTypeHandler;
import ys.rg.fourClass.handler.IntegerList2JsonTypeHandler;
import ys.rg.fourClass.handler.LongList2JsonTypeHandler;
import ys.rg.fourClass.handler.StringList2JsonTypeHandler;

import java.util.List;

@Data
public class ExperimentalResultsDTO {
    //算法类型
    private String algorithmType;
    //物理页号序列
    private List<Integer> physicalPageNumberSequenceJson;
    //内存地址序列
    private List<String> memoryAddressSequenceJson;
    //单次存取时间序列
    private List<Long> singleAccessTimeSequenceJson;
    //TLB命中状态序列
    private List<Boolean> tlbHitStatusSequenceJson;
    //驻留内存集序列
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
}
