package ys.rg.fourClass.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(25)
public class ExcelExperimentalResultsDTO {

    // ==================== Experiment 实验信息 ====================

    @ExcelProperty(value = "实验信息-实验ID", index = 0)
    @ColumnWidth(15)
    private Integer experimentId;

    @ExcelProperty(value = "实验信息-实验名称", index = 1)
    @ColumnWidth(20)
    private String experimentName;

    @ExcelProperty(value = "实验信息-逻辑地址序列", index = 2)
    @ColumnWidth(25)
    private String logicalAddressSequenceJson;

    @ExcelProperty(value = "实验信息-逻辑页号序列", index = 3)
    @ColumnWidth(25)
    private String logicalPageNumberSequenceJson;

    @ExcelProperty(value = "实验信息-逻辑地址序列大小", index = 4)
    @ColumnWidth(20)
    private Integer logicalAddressSequenceSize;

    @ExcelProperty(value = "实验信息-进程驻留内存集数量", index = 5)
    @ColumnWidth(22)
    private Integer residentMemorySetCount;

    @ExcelProperty(value = "实验信息-快表大小", index = 6)
    @ColumnWidth(18)
    private Integer tlbSize;

    @ExcelProperty(value = "实验信息-是否采用TLB", index = 7)
    @ColumnWidth(18)
    private Boolean isUseTlb;

    @ExcelProperty(value = "实验信息-内存访问时间", index = 8)
    @ColumnWidth(18)
    private Integer memoryAccessTime;

    @ExcelProperty(value = "实验信息-快表访问时间", index = 9)
    @ColumnWidth(18)
    private Integer fastTableAccessTime;

    @ExcelProperty(value = "实验信息-缺页时间", index = 10)
    @ColumnWidth(18)
    private Integer pageFaultTime;

    @ExcelProperty(value = "实验信息-实验创建时间", index = 11)
    @ColumnWidth(22)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime;

    // ==================== ExperimentalResults 实验结果 ====================

    @ExcelProperty(value = "实验结果-记录ID", index = 12)
    @ColumnWidth(15)
    private Integer id;

    @ExcelProperty(value = "实验结果-算法类型", index = 13)
    @ColumnWidth(18)
    private String algorithmType;

    @ExcelProperty(value = "实验结果-关联实验ID", index = 14)
    @ColumnWidth(18)
    private Integer resultExperimentId;

    @ExcelProperty(value = "实验结果-物理页号序列", index = 15)
    @ColumnWidth(25)
    private String physicalPageNumberSequenceJson;

    @ExcelProperty(value = "实验结果-内存地址序列", index = 16)
    @ColumnWidth(25)
    private String memoryAddressSequenceJson;

    @ExcelProperty(value = "实验结果-单次存取时间序列", index = 17)
    @ColumnWidth(25)
    private String singleAccessTimeSequenceJson;

    @ExcelProperty(value = "实验结果-TLB命中状态序列", index = 18)
    @ColumnWidth(25)
    private String tlbHitStatusSequenceJson;

    @ExcelProperty(value = "实验结果-驻留内存集序列", index = 19)
    @ColumnWidth(25)
    private String residentMemorySetSequenceJson;

    @ExcelProperty(value = "实验结果-TLB命中率", index = 20)
    @ColumnWidth(15)
    private Double tlbHitRate;

    @ExcelProperty(value = "实验结果-缺页次数", index = 21)
    @ColumnWidth(15)
    private Integer pageFaultCount;

    @ExcelProperty(value = "实验结果-页面置换次数", index = 22)
    @ColumnWidth(18)
    private Integer pageReplacementCount;

    @ExcelProperty(value = "实验结果-缺页率", index = 23)
    @ColumnWidth(15)
    private Double pageFaultRate;

    @ExcelProperty(value = "实验结果-页面命中率", index = 24)
    @ColumnWidth(15)
    private Double pageHitRate;

    @ExcelProperty(value = "实验结果-实际运行时间(ms)", index = 25)
    @ColumnWidth(20)
    private Long realRunningTime;

    @ExcelProperty(value = "实验结果-模拟时间", index = 26)
    @ColumnWidth(15)
    private Integer emulateRunningTime;

    @ExcelProperty(value = "实验结果-平均实际时间(ms)", index = 27)
    @ColumnWidth(22)
    private Double averageRealRunningTime;

    @ExcelProperty(value = "实验结果-结果创建时间", index = 28)
    @ColumnWidth(22)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String resultCreateTime;
}