package ys.rg.fourClass.util;

import org.apache.commons.lang3.StringUtils;
import static ys.rg.fourClass.util.CheckTheFormat.ADDRESS_SEQ_REGEX;

public class AddressSequenceValidator {
    /**
     * 校验地址序列
     * @param addressSeq 地址序列
     * @return 保存后的结果
     */
    public static String validateWithMsg(String addressSeq) {
        if (StringUtils.isBlank(addressSeq)) {
            return "地址序列为空";
        }
        if (!addressSeq.matches(ADDRESS_SEQ_REGEX)) {
            return "地址序列格式错误：\n" +
                    "1. 每个地址单元需为十六进制字符+大写H结尾（如0E4FH、53245H）；\n" +
                    "2. 分隔符需统一（全为顿号、或全为逗号）；\n" +
                    "3. 至少包含2个地址单元；\n" +
                    "4. 无多余空格/特殊字符。";
        }
        return null;
    }
}
