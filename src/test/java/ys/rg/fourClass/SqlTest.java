package ys.rg.fourClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ys.rg.fourClass.entity.Experiment;
import ys.rg.fourClass.mapper.ExperimentMapper;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class SqlTest {
    @Autowired
    private ExperimentMapper experimentMapper;

    @org.junit.jupiter.api.Test
    void putIntoSql(){
        Experiment experiment = new Experiment();

        // 设置基本属性
        experiment.setExperimentName("分页管理算法实验");

        // 设置列表类型字段 - 会通过 List2JsonTypeHandler 自动转换为 JSON 字符串
        List<String> addressList = Arrays.asList("0x1000", "0x2000", "0x3000", "0x4000");
        experiment.setLogicalAddressSequenceJson(addressList);

        List<Integer> pageList = Arrays.asList(1, 2, 3, 4, 5, 6);
        experiment.setLogicalPageNumberSequenceJson(pageList);

        // 设置其他字段
        experiment.setLogicalAddressSequenceSize(addressList.size());
        experiment.setResidentMemorySetCount(4);
        experiment.setIsUseTlb(true);
        experiment.setMemoryAccessTime(100);
        experiment.setFastTableAccessTime(10);
        experiment.setPageFaultTime(1000);
        experiment.setIsDelete(false);

        // 插入数据
        int result = experimentMapper.insert(experiment);
        System.out.println("插入成功，影响行数: " + result);
        System.out.println("生成的主键ID: " + experiment.getExperimentId());
    }

    @org.junit.jupiter.api.Test
    void updateSql() {
        // 先查询一条记录
        Experiment experiment = experimentMapper.selectById(2);
        if (experiment != null) {
            // 修改字段
            experiment.setExperimentName("更新后的实验名称");
            experiment.getLogicalAddressSequenceJson().add("0x5000"); // 修改列表
            experiment.setLogicalAddressSequenceSize(experiment.getLogicalAddressSequenceJson().size());
            System.out.println(111);
            // 执行更新
            int result = experimentMapper.updateById(experiment);
            System.out.println("更新成功，影响行数: " + result);
        }
    }

    @org.junit.jupiter.api.Test
    void fromSql() {
        // 1. 查询所有记录
        System.out.println("=== 查询所有记录 ===");
        List<Experiment> experiments = experimentMapper.selectList(null);
        for (Experiment exp : experiments) {
            System.out.println("实验ID: " + exp.getExperimentId());
            System.out.println("实验名称: " + exp.getExperimentName());
            System.out.println("逻辑地址序列: " + exp.getLogicalAddressSequenceJson());
            System.out.println("逻辑页号序列: " + exp.getLogicalPageNumberSequenceJson());
            System.out.println("是否使用TLB: " + exp.getIsUseTlb());
            System.out.println("------------------------");
        }

        // 2. 按ID查询单条记录
        System.out.println("\n=== 按ID查询单条记录 ===");
        Experiment experiment = experimentMapper.selectById(1);
        if (experiment != null) {
            System.out.println("实验详情: " + experiment);

            // 验证类型处理器是否正常工作
            System.out.println("逻辑地址序列类型: " + experiment.getLogicalAddressSequenceJson().getClass());
            System.out.println("逻辑地址序列内容: " + experiment.getLogicalAddressSequenceJson());
            System.out.println("逻辑页号序列类型: " + experiment.getLogicalPageNumberSequenceJson().getClass());
            System.out.println("逻辑页号序列内容: " + experiment.getLogicalPageNumberSequenceJson());

            // 如果需要操作列表数据
            List<String> addressList = experiment.getLogicalAddressSequenceJson();
            List<Integer> pageList = experiment.getLogicalPageNumberSequenceJson();

            System.out.println("逻辑地址数量: " + addressList.size());
            System.out.println("逻辑页号数量: " + pageList.size());
        }
    }

    @org.junit.jupiter.api.Test
    void queryByCondition() {
        // 示例：查询使用TLB的实验
        System.out.println("=== 查询使用TLB的实验 ===");
        List<Experiment> experiments = experimentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Experiment>()
                        .eq("is_use_tlb", true)
                        .orderByDesc("create_time")
        );

        experiments.forEach(exp -> {
            System.out.println("实验ID: " + exp.getExperimentId() +
                    ", 名称: " + exp.getExperimentName() +
                    ", 创建时间: " + exp.getCreateTime());
        });
    }

    @org.junit.jupiter.api.Test
    void deleteFromSql() {
        // 逻辑删除
        int result = experimentMapper.deleteById(1);
        System.out.println("逻辑删除成功，影响行数: " + result);

        // 验证逻辑删除后是否还能查询到
        Experiment experiment = experimentMapper.selectById(1);
        if (experiment == null) {
            System.out.println("记录已逻辑删除，查询不到");
        } else {
            System.out.println("记录仍然存在，is_delete状态: " + experiment.getIsDelete());
        }
    }

    @org.junit.jupiter.api.Test
    void batchInsert() {
        // 批量插入测试数据
        for (int i = 1; i <= 5; i++) {
            Experiment experiment = new Experiment();
            experiment.setExperimentName("分页实验-" + i);

            // 生成测试数据
            List<String> addresses = Arrays.asList(
                    String.format("0x%04X", i * 1000),
                    String.format("0x%04X", i * 1000 + 100),
                    String.format("0x%04X", i * 1000 + 200)
            );
            experiment.setLogicalAddressSequenceJson(addresses);

            List<Integer> pages = Arrays.asList(i, i+1, i+2);
            experiment.setLogicalPageNumberSequenceJson(pages);

            experiment.setLogicalAddressSequenceSize(addresses.size());
            experiment.setResidentMemorySetCount(3);
            experiment.setIsUseTlb(i % 2 == 0); // 偶数编号使用TLB
            experiment.setMemoryAccessTime(100);
            experiment.setFastTableAccessTime(10);
            experiment.setPageFaultTime(1000);
            experiment.setCreateTime("2023-12-25 15:" + String.format("%02d", i) + ":00");
            experiment.setIsDelete(false);

            experimentMapper.insert(experiment);
            System.out.println("插入实验 " + i + "，ID: " + experiment.getExperimentId());
        }
    }

    @org.junit.jupiter.api.Test
    void testJsonConversion() {
        // 测试类型处理器的转换功能
        Experiment experiment = new Experiment();

        // 设置复杂数据
        List<String> complexAddresses = Arrays.asList("0xA000", "0xB000", "0xC000", "0xD000");
        List<Integer> complexPages = Arrays.asList(10, 20, 30, 40, 50);

        experiment.setLogicalAddressSequenceJson(complexAddresses);
        experiment.setLogicalPageNumberSequenceJson(complexPages);

        // 插入数据
        experimentMapper.insert(experiment);
        System.out.println("插入复杂数据，ID: " + experiment.getExperimentId());

        // 立即查询验证
        Experiment retrieved = experimentMapper.selectById(experiment.getExperimentId());
        if (retrieved != null) {
            System.out.println("查询到的地址序列: " + retrieved.getLogicalAddressSequenceJson());
            System.out.println("查询到的页号序列: " + retrieved.getLogicalPageNumberSequenceJson());
            System.out.println("数据类型验证: " +
                    (retrieved.getLogicalAddressSequenceJson() instanceof List) + ", " +
                    (retrieved.getLogicalPageNumberSequenceJson() instanceof List));
        }
    }
}
