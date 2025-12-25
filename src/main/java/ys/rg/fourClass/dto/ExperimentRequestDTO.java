package ys.rg.fourClass.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 前端传入的实验请求DTO
 */
@Data
public class ExperimentRequestDTO {
    // 实验名称
    private String experimentName;
    // 前端传入的逻辑地址序列（如 ["0E8AH","0AF4H","0577H"]）
    @NotEmpty(message = "逻辑地址序列不能为空")
    private List<String> logicalAddressSequence;
    // 驻留内存集数量（默认3）
    @NotNull(message = "驻留内存数不能为空")
    private Integer residentMemorySetCount = 3;
    // 是否启用TLB（默认true）
    private Boolean isUseTLB = true;
    // 是否启用实际计算时间方式
    private Boolean isUseRealRunningTime = false;
    // 内存访问时间（默认100ns）
    private Integer memoryAccessTime = 100;
    // 快表访问时间（默认10ns）
    private Integer fastTableAccessTime = 10;
    // 缺页处理时间（默认2000ns）
    private Integer pageFaultTime = 2000;
}
