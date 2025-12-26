package ys.rg.fourClass.util;

import java.util.Random;

public class AddressSequenceGenerator {
    //字符池
    private static final char[] HEX_CHARS = {
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F',
            'a','b','c','d','e','f'
    };

    //随机数生成
    private static final Random RANDOM = new Random();


    /**
     * 生成指定长度的随机十六进制字符串
     * @param length 字符串长度（≥1）
     * @return 随机十六进制字符串
     */
    private static String generateRandomHexString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("十六进制字符串长度不能小于1");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 随机选一个十六进制字符
            char hexChar = HEX_CHARS[RANDOM.nextInt(HEX_CHARS.length)];
            sb.append(hexChar);
        }
        return sb.toString();
    }


    /**
     * 随机生成符合规则的地址序列
     * @return 符合正则的地址序列字符串
     */
    public static String generateRandomAddressSeq() {
        // 分隔符
        String separator ="、";

        // 随机确定元素数量
        int elementCount = 2 + RANDOM.nextInt(9);

        // 拼接所有元素
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elementCount; i++) {
            // 生成单个元素：随机长度（1-8位）的十六进制字符串 + H
            String hexPart = generateRandomHexString(5);
            sb.append(hexPart).append("H");

            // 最后一个元素不加分隔符
            if (i != elementCount - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

}
