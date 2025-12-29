package ys.rg.fourClass.util;

import org.apache.commons.lang3.StringUtils;
import static ys.rg.fourClass.util.CheckTheFormat.WINDOWS_TXT_PATH_REGEX;

public class TxtPathValidator {
    /**
     * txt文件地址校验
     * @param txtPath txt文件地址
     * @return 保存后的结果
     */
    public static String validateWithMsg1(String txtPath) {
        if (StringUtils.isBlank(txtPath)) {
            return "txt文件地址为空";
        }
        if (!txtPath.matches(WINDOWS_TXT_PATH_REGEX)) {
            return "txt文件地址格式错误";
        }
        return null;
    }
}
