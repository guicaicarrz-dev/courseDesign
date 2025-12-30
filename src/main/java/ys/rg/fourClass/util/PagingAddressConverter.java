package ys.rg.fourClass.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PagingAddressConverter {

    // 系统参数（根据你的描述）
    public static final int ADDRESS_BITS = 16;      // 地址位数
    public static final int PAGE_NUMBER_BITS = 6;   // 逻辑页号位数
    public static final int OFFSET_BITS = 10;       // 页内偏移位数
    public static final int PAGE_SIZE = 1024;       // 页面大小 (1KB)
    public static final int MAX_PAGES = 64;         // 最大页数
    public static final int MAX_ADDRESS = 0xFFFF;   // 最大地址

    /**
     * 解析逻辑地址字符串（格式: "0E4FH"）
     * 规则：4位十六进制数，以H结尾，大写字母
     */
    public static int parseLogicalAddress(String addressStr) {
        if (addressStr == null || addressStr.length() != 5) {
            throw new IllegalArgumentException("地址格式错误: 必须是4位十六进制数+H，如'0E4FH'");
        }

        if (!addressStr.toUpperCase().endsWith("H")) {
            throw new IllegalArgumentException("地址格式错误: 必须以'H'结尾");
        }

        // 去掉'H'后缀，获取4位十六进制数
        String hexStr = addressStr.substring(0, 4).toUpperCase();

        try {
            // 解析十六进制字符串为整数
            int address = Integer.parseInt(hexStr, 16);

            // 验证地址范围（16位）
            if (address < 0 || address > MAX_ADDRESS) {
                throw new IllegalArgumentException(
                        String.format("地址超出范围(0x0000-0xFFFF): 0x%04X", address));
            }

            return address;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("地址包含非十六进制字符: " + addressStr);
        }
    }

    /**
     * ============================================
     * 功能1：输入逻辑地址序列，返回逻辑页号序列
     * ============================================
     * 输入：逻辑地址序列（字符串，如"0E4FH"）
     * 输出：逻辑页号序列（整数）
     */
    public static List<Integer> getLogicalPageNumbers(List<String> logicalAddresses) {
        List<Integer> pageNumbers = new ArrayList<>();

        if (logicalAddresses == null || logicalAddresses.isEmpty()) {
            return pageNumbers;
        }

        for (String address : logicalAddresses) {
            try {
                int logicalAddress = parseLogicalAddress(address);

                // 方法1：使用位运算提取高6位作为逻辑页号
                int pageNumber = (logicalAddress >> OFFSET_BITS) & 0x3F;

                // 方法2：或者使用除法（结果相同）
                // int pageNumber = logicalAddress / PAGE_SIZE;

                pageNumbers.add(pageNumber);

            } catch (IllegalArgumentException e) {
                System.err.println("错误处理地址 '" + address + "': " + e.getMessage());
                pageNumbers.add(-1); // 错误标记
            }
        }

        return pageNumbers;
    }

    /**
     * ============================================
     * 功能2：输入逻辑地址序列和物理页号序列，返回物理地址序列
     * ============================================
     * 输入：逻辑地址序列（字符串）和物理页号序列（整数）
     * 输出：物理地址序列（字符串，格式"0xXXXX"）
     */
    public static List<String> getPhysicalAddresses(List<String> logicalAddresses,
                                                    List<Integer> physicalPageNumbers) {
        List<String> physicalAddresses = new ArrayList<>();

        if (logicalAddresses == null || physicalPageNumbers == null) {
            throw new IllegalArgumentException("输入列表不能为null");
        }

        if (logicalAddresses.size() != physicalPageNumbers.size()) {
            throw new IllegalArgumentException(
                    String.format("地址序列和页号序列长度不一致: %d != %d",
                            logicalAddresses.size(), physicalPageNumbers.size()));
        }

        for (int i = 0; i < logicalAddresses.size(); i++) {
            try {
                String logicalAddrStr = logicalAddresses.get(i);
                int physicalPageNumber = physicalPageNumbers.get(i);

                // 验证物理页号范围
                if (physicalPageNumber < 0 || physicalPageNumber >= MAX_PAGES) {
                    throw new IllegalArgumentException(
                            String.format("物理页号超出范围(0-%d): %d", MAX_PAGES-1, physicalPageNumber));
                }

                // 解析逻辑地址
                int logicalAddress = parseLogicalAddress(logicalAddrStr);

                // 提取页内偏移（低10位）
                int offset = logicalAddress & 0x3FF;

                // 计算物理地址：物理页号 × 页面大小 + 页内偏移
                int physicalAddress = (physicalPageNumber * PAGE_SIZE) + offset;

                // 验证物理地址范围
                if (physicalAddress < 0 || physicalAddress > MAX_ADDRESS) {
                    throw new IllegalArgumentException(
                            String.format("物理地址超出范围(0-65535): %d (0x%04X)",
                                    physicalAddress, physicalAddress));
                }

                // 格式化为十六进制字符串（带0x前缀）
                String physicalAddrHex = String.format("0x%04X", physicalAddress);
                physicalAddresses.add(physicalAddrHex);

            } catch (IllegalArgumentException e) {
                System.err.println("错误处理第 " + (i+1) + " 个地址: " + e.getMessage());
                physicalAddresses.add("ERROR");
            }
        }

        return physicalAddresses;
    }

    public static List<String> parseAddressString(String addressString) {
        if (addressString == null || addressString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 去除首尾空格
        String trimmed = addressString.trim();

        // 按逗号分割，并去除每个元素的首尾空格
        String[] parts = trimmed.split("\\s*,\\s*");

        // 转换为List并过滤空字符串
        return Arrays.stream(parts)
                .filter(addr -> !addr.isEmpty())
                .collect(Collectors.toList());
    }
}
