package ys.rg.fourClass.util;

public class CheckTheFormat {
    /**
     * 地选字符正则
     */
    public static final String ADDRESS_SEQ_REGEX = "^([0-9A-Fa-f]+H)(\\、|\\，)([0-9A-Fa-f]+H)(?:\\2[0-9A-Fa-f]+H)*$";
    /**
     * txt文件地选正则
     */
    public static final String WINDOWS_TXT_PATH_REGEX = "^[A-Za-z]:\\\\(?:[^\\\\/:*?\"<>|]+[\\\\/])*[^\\\\/:*?\"<>|]+\\.txt$";
}
