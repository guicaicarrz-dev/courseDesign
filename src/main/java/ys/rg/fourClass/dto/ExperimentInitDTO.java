package ys.rg.fourClass.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 功能：接收前端一次性提交的实验初始化参数
 * 用法：前端POST /experiment/init时传参，后端通过@Valid校验参数合法性
 */
@Data
public class ExperimentInitDTO {
    private String experimentName; // 实验名称
    @NotEmpty(message = "地址序列不能为空")
    private List<String> logicalAddressSequence; // 逻辑地址序列
    @NotNull(message = "驻留内存数不能为空")
    private Integer residentMemorySetCount = 3; // 驻留内存集数量
    private Boolean isUseTLB = true; // 是否启用TLB
    private Integer memoryAccessTime = 100; // 内存访问时间(ms)
    private Integer fastTableAccessTime = 10; // 快表访问时间(ms)
    private Integer pageFaultTime = 2000; // 缺页处理时间(ms)
    @NotEmpty(message = "请选择算法类型")
    private List<String> algorithmTypes; // 要执行的算法类型（FIFO/LRU/OPT）
}
