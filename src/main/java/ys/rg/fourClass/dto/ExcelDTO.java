package ys.rg.fourClass.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * Excel导出DTO - 每条Experiment对应3条ExperimentalResults
 * 符合EasyExcel标注，包含所有属性
 */
@Data
@HeadRowHeight(25)
public class ExcelDTO {

    // ==================== Experiment 基本信息 ====================
    @ExcelProperty(value = {"实验信息", "实验ID"}, index = 0)
    @ColumnWidth(10)
    private Integer experimentId;

    @ExcelProperty(value = {"实验信息", "实验名称"}, index = 1)
    @ColumnWidth(25)
    private String experimentName;

    @ExcelProperty(value = {"实验信息", "逻辑地址序列"}, index = 2)
    @ColumnWidth(30)
    private String logicalAddressSequenceJson;

    @ExcelProperty(value = {"实验信息", "逻辑页号序列"}, index = 3)
    @ColumnWidth(30)
    private String logicalPageNumberSequenceJson;

    @ExcelProperty(value = {"实验信息", "逻辑地址序列大小"}, index = 4)
    @ColumnWidth(15)
    private Integer logicalAddressSequenceSize;

    @ExcelProperty(value = {"实验信息", "进程驻留内存集数量"}, index = 5)
    @ColumnWidth(18)
    private Integer residentMemorySetCount;

    @ExcelProperty(value = {"实验信息", "是否采用TLB"}, index = 6)
    @ColumnWidth(12)
    private Boolean isUseTlb;

    @ExcelProperty(value = {"实验信息", "内存访问时间(ms)"}, index = 7)
    @ColumnWidth(15)
    private Integer memoryAccessTime;

    @ExcelProperty(value = {"实验信息", "快表访问时间(ms)"}, index = 8)
    @ColumnWidth(15)
    private Integer fastTableAccessTime;

    @ExcelProperty(value = {"实验信息", "缺页时间(ms)"}, index = 9)
    @ColumnWidth(15)
    private Integer pageFaultTime;

    @ExcelProperty(value = {"实验信息", "实验创建时间"}, index = 10)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime;

    // ==================== 第一条 ExperimentalResults ====================
    @ExcelProperty(value = {"实验结果1", "算法类型"}, index = 11)
    @ColumnWidth(20)
    private String algorithmType1;

    @ExcelProperty(value = {"实验结果1", "物理页号序列"}, index = 12)
    @ColumnWidth(30)
    private String physicalPageNumberSequenceJson1;

    @ExcelProperty(value = {"实验结果1", "内存地址序列"}, index = 13)
    @ColumnWidth(30)
    private String memoryAddressSequenceJson1;

    @ExcelProperty(value = {"实验结果1", "单次存取时间序列"}, index = 14)
    @ColumnWidth(30)
    private String singleAccessTimeSequenceJson1;

    @ExcelProperty(value = {"实验结果1", "TLB命中状态序列"}, index = 15)
    @ColumnWidth(30)
    private String tlbHitStatusSequenceJson1;

    @ExcelProperty(value = {"实验结果1", "驻留内存集序列"}, index = 16)
    @ColumnWidth(30)
    private String residentMemorySetSequenceJson1;

    @ExcelProperty(value = {"实验结果1", "TLB命中率"}, index = 17)
    @ColumnWidth(12)
    private Double tlbHitRate1;

    @ExcelProperty(value = {"实验结果1", "缺页次数"}, index = 18)
    @ColumnWidth(10)
    private Integer pageFaultCount1;

    @ExcelProperty(value = {"实验结果1", "页面置换次数"}, index = 19)
    @ColumnWidth(12)
    private Integer pageReplacementCount1;

    @ExcelProperty(value = {"实验结果1", "缺页率"}, index = 20)
    @ColumnWidth(10)
    private Double pageFaultRate1;

    @ExcelProperty(value = {"实验结果1", "页面命中率"}, index = 21)
    @ColumnWidth(12)
    private Double pageHitRate1;

    @ExcelProperty(value = {"实验结果1", "实际运行时间(ns)"}, index = 22)
    @ColumnWidth(18)
    private Long realRunningTime1;

    @ExcelProperty(value = {"实验结果1", "模拟运行时间(ms)"}, index = 23)
    @ColumnWidth(15)
    private Integer emulateRunningTime1;

    @ExcelProperty(value = {"实验结果1", "平均实际运行时间(ns)"}, index = 24)
    @ColumnWidth(22)
    private Double averageRealRunningTime1;

    @ExcelProperty(value = {"实验结果1", "结果创建时间"}, index = 25)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime1;

    // ==================== 第二条 ExperimentalResults ====================
    @ExcelProperty(value = {"实验结果2", "算法类型"}, index = 26)
    @ColumnWidth(20)
    private String algorithmType2;

    @ExcelProperty(value = {"实验结果2", "物理页号序列"}, index = 27)
    @ColumnWidth(30)
    private String physicalPageNumberSequenceJson2;

    @ExcelProperty(value = {"实验结果2", "内存地址序列"}, index = 28)
    @ColumnWidth(30)
    private String memoryAddressSequenceJson2;

    @ExcelProperty(value = {"实验结果2", "单次存取时间序列"}, index = 29)
    @ColumnWidth(30)
    private String singleAccessTimeSequenceJson2;

    @ExcelProperty(value = {"实验结果2", "TLB命中状态序列"}, index = 30)
    @ColumnWidth(30)
    private String tlbHitStatusSequenceJson2;

    @ExcelProperty(value = {"实验结果2", "驻留内存集序列"}, index = 31)
    @ColumnWidth(30)
    private String residentMemorySetSequenceJson2;

    @ExcelProperty(value = {"实验结果2", "TLB命中率"}, index = 32)
    @ColumnWidth(12)
    private Double tlbHitRate2;

    @ExcelProperty(value = {"实验结果2", "缺页次数"}, index = 33)
    @ColumnWidth(10)
    private Integer pageFaultCount2;

    @ExcelProperty(value = {"实验结果2", "页面置换次数"}, index = 34)
    @ColumnWidth(12)
    private Integer pageReplacementCount2;

    @ExcelProperty(value = {"实验结果2", "缺页率"}, index = 35)
    @ColumnWidth(10)
    private Double pageFaultRate2;

    @ExcelProperty(value = {"实验结果2", "页面命中率"}, index = 36)
    @ColumnWidth(12)
    private Double pageHitRate2;

    @ExcelProperty(value = {"实验结果2", "实际运行时间(ns)"}, index = 37)
    @ColumnWidth(18)
    private Long realRunningTime2;

    @ExcelProperty(value = {"实验结果2", "模拟运行时间(ms)"}, index = 38)
    @ColumnWidth(15)
    private Integer emulateRunningTime2;

    @ExcelProperty(value = {"实验结果2", "平均实际运行时间(ns)"}, index = 39)
    @ColumnWidth(22)
    private Double averageRealRunningTime2;

    @ExcelProperty(value = {"实验结果2", "结果创建时间"}, index = 40)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime2;

    // ==================== 第三条 ExperimentalResults ====================
    @ExcelProperty(value = {"实验结果3", "算法类型"}, index = 41)
    @ColumnWidth(20)
    private String algorithmType3;

    @ExcelProperty(value = {"实验结果3", "物理页号序列"}, index = 42)
    @ColumnWidth(30)
    private String physicalPageNumberSequenceJson3;

    @ExcelProperty(value = {"实验结果3", "内存地址序列"}, index = 43)
    @ColumnWidth(30)
    private String memoryAddressSequenceJson3;

    @ExcelProperty(value = {"实验结果3", "单次存取时间序列"}, index = 44)
    @ColumnWidth(30)
    private String singleAccessTimeSequenceJson3;

    @ExcelProperty(value = {"实验结果3", "TLB命中状态序列"}, index = 45)
    @ColumnWidth(30)
    private String tlbHitStatusSequenceJson3;

    @ExcelProperty(value = {"实验结果3", "驻留内存集序列"}, index = 46)
    @ColumnWidth(30)
    private String residentMemorySetSequenceJson3;

    @ExcelProperty(value = {"实验结果3", "TLB命中率"}, index = 47)
    @ColumnWidth(12)
    private Double tlbHitRate3;

    @ExcelProperty(value = {"实验结果3", "缺页次数"}, index = 48)
    @ColumnWidth(10)
    private Integer pageFaultCount3;

    @ExcelProperty(value = {"实验结果3", "页面置换次数"}, index = 49)
    @ColumnWidth(12)
    private Integer pageReplacementCount3;

    @ExcelProperty(value = {"实验结果3", "缺页率"}, index = 50)
    @ColumnWidth(10)
    private Double pageFaultRate3;

    @ExcelProperty(value = {"实验结果3", "页面命中率"}, index = 51)
    @ColumnWidth(12)
    private Double pageHitRate3;

    @ExcelProperty(value = {"实验结果3", "实际运行时间(ns)"}, index = 52)
    @ColumnWidth(18)
    private Long realRunningTime3;

    @ExcelProperty(value = {"实验结果3", "模拟运行时间(ms)"}, index = 53)
    @ColumnWidth(15)
    private Integer emulateRunningTime3;

    @ExcelProperty(value = {"实验结果3", "平均实际运行时间(ns)"}, index = 54)
    @ColumnWidth(22)
    private Double averageRealRunningTime3;

    @ExcelProperty(value = {"实验结果3", "结果创建时间"}, index = 55)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime3;
    // ==================== 第四条 ExperimentalResults ====================
    @ExcelProperty(value = {"实验结果4", "算法类型"}, index = 41)
    @ColumnWidth(20)
    private String algorithmType4;

    @ExcelProperty(value = {"实验结果4", "物理页号序列"}, index = 42)
    @ColumnWidth(30)
    private String physicalPageNumberSequenceJson4;

    @ExcelProperty(value = {"实验结果4", "内存地址序列"}, index = 43)
    @ColumnWidth(30)
    private String memoryAddressSequenceJson4;

    @ExcelProperty(value = {"实验结果4","单次存取时间序列"}, index = 44)
    @ColumnWidth(30)
    private String singleAccessTimeSequenceJson4;

    @ExcelProperty(value = {"实验结果4", "TLB命中状态序列"}, index = 45)
    @ColumnWidth(30)
    private String tlbHitStatusSequenceJson4;

    @ExcelProperty(value = {"实验结果4", "驻留内存集序列"}, index = 46)
    @ColumnWidth(30)
    private String residentMemorySetSequenceJson4;

    @ExcelProperty(value = {"实验结果4", "TLB命中率"}, index = 47)
    @ColumnWidth(12)
    private Double tlbHitRate4;

    @ExcelProperty(value = {"实验结果4", "缺页次数"}, index = 48)
    @ColumnWidth(10)
    private Integer pageFaultCount4;

    @ExcelProperty(value = {"实验结果4", "页面置换次数"}, index = 49)
    @ColumnWidth(12)
    private Integer pageReplacementCount4;

    @ExcelProperty(value = {"实验结果4", "缺页率"}, index = 50)
    @ColumnWidth(10)
    private Double pageFaultRate4;

    @ExcelProperty(value = {"实验结果4", "页面命中率"}, index = 51)
    @ColumnWidth(12)
    private Double pageHitRate4;

    @ExcelProperty(value = {"实验结果4", "实际运行时间(ns)"}, index = 52)
    @ColumnWidth(18)
    private Long realRunningTime4;

    @ExcelProperty(value = {"实验结果4", "模拟运行时间(ms)"}, index = 53)
    @ColumnWidth(15)
    private Integer emulateRunningTime4;

    @ExcelProperty(value = {"实验结果4", "平均实际运行时间(ns)"}, index = 54)
    @ColumnWidth(22)
    private Double averageRealRunningTime4;

    @ExcelProperty(value = {"实验结果4", "结果创建时间"}, index = 55)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String createTime4;
}

