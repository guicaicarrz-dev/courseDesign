package ys.rg.fourClass.dto;

import lombok.Data;

import java.util.List;
import java.util.Map; /**
 * 单个算法的执行结果DTO
 */
@Data
public class AlgorithmResultDTO {
    // 逻辑页号序列
    private List<Integer> logicalPageNumberSequence;
    // 物理页号序列
    private List<Integer> physicalPageNumberSequence;
    // 内存地址序列（逻辑地址→物理地址）
    private List<String> memoryAddressSequence;
    // 单次存取时间序列（ns）
    private List<Long> singleAccessTimeSequence;
    // TLB命中状态序列（true=命中，false=未命中）
    private List<Boolean> tlbHitStatusSequence;
    // TLB命中率
    private Double tlbHitRate;
    // 驻留内存集变化序列（每一步的内存页状态）
    private List<Integer> residentMemorySetSequence;
    // 缺页次数
    private Integer pageFaultCount;
    // 页面置换次数
    private Integer pageReplacementCount;
    // 缺页率
    private Double pageFaultRate;
    //页面命中率
    private Double pageHitRate;
    //实际时间
    private long realRunningTime;
    //模拟时间
    private Integer emulateRunningTime;
    //平均实际时间
    private Double averageRealRunningTime;

}
