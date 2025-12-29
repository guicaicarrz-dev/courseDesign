package ys.rg.fourClass;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ys.rg.fourClass.entity.ExperimentalResults;
import ys.rg.fourClass.mapper.ExperimentalResultsMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * ExperimentalResults 实体测试类
 * 覆盖新增、查询、更新、逻辑删除等核心场景
 */
@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ResultTest {

    // 注入Mapper（确保ExperimentalResultsMapper已继承BaseMapper<ExperimentalResults>）
    @Autowired
    private ExperimentalResultsMapper experimentalResultsMapper;

    /**
     * 测试新增ExperimentalResults（包含所有JSON数组字段）
     */
    @Test
    public void testInsert() {
        // 1. 构建测试数据（适配所有TypeHandler）
        ExperimentalResults results = new ExperimentalResults();
        results.setAlgorithmType("LRU页面置换算法"); // 算法类型
        results.setExperimentId(2); // 关联的实验ID
        results.setTlbHitRate(0.85); // TLB命中率
        results.setPageFaultCount(3); // 缺页次数
        results.setPageReplacementCount(2); // 页面置换次数
        results.setPageFaultRate(0.15); // 缺页率
        results.setPageHitRate(0.85); // 页面命中率
        results.setRealRunningTime(123456789L); // 实际运行时间
        results.setEmulateRunningTime(5000); // 模拟运行时间
        results.setAverageRealRunningTime(12345678.9); // 平均实际时间
        results.setCreateTime("2025-12-27 10:00:00"); // 创建时间
        results.setIsDeleted(false); // 未逻辑删除

        // 2. 构建各JSON数组字段（适配对应TypeHandler）
        // 物理页号序列（IntegerList）
        List<Integer> physicalPageList = new ArrayList<>();
        physicalPageList.add(1);
        physicalPageList.add(3);
        physicalPageList.add(5);
        results.setPhysicalPageNumberSequenceJson(physicalPageList);

        // 内存地址序列（StringList）
        List<String> memoryAddressList = new ArrayList<>();
        memoryAddressList.add("0x1000");
        memoryAddressList.add("0x2000");
        memoryAddressList.add("0x3000");
        results.setMemoryAddressSequenceJson(memoryAddressList);

        // 单次存取时间序列（LongList）
        List<Long> singleAccessTimeList = new ArrayList<>();
        singleAccessTimeList.add(100L);
        singleAccessTimeList.add(200L);
        singleAccessTimeList.add(300L);
        results.setSingleAccessTimeSequenceJson(singleAccessTimeList);

        // TLB命中状态序列（BooleanList）
        List<Boolean> tlbHitStatusList = new ArrayList<>();
        tlbHitStatusList.add(true);
        tlbHitStatusList.add(false);
        tlbHitStatusList.add(true);
        results.setTlbHitStatusSequenceJson(tlbHitStatusList);

        // 驻留内存集序列（IntegerList）
        List<Integer> residentMemorySetList = new ArrayList<>();
        residentMemorySetList.add(1);
        residentMemorySetList.add(2);
        residentMemorySetList.add(3);
        results.setResidentMemorySetSequenceJson(residentMemorySetList);

        // 3. 执行新增
        int insert = experimentalResultsMapper.insert(results);
        log.info("新增结果：{}行受影响，新增ID：{}", insert, results.getId());
        assert insert == 1; // 验证新增成功
    }

    /**
     * 测试根据ID查询（验证所有TypeHandler解析正常）
     */
    @Test
    public void testSelectById() {
        // 替换为实际新增的ID（比如testInsert执行后返回的ID）
        Integer id = 2;
        ExperimentalResults results = experimentalResultsMapper.selectById(id);
        log.info("查询结果：{}", results);

        // 验证核心字段解析正常
        assert results != null;
        assert "LRU页面置换算法".equals(results.getAlgorithmType());
        assert results.getPhysicalPageNumberSequenceJson().size() == 3; // 验证IntegerList解析
        assert results.getMemoryAddressSequenceJson().contains("0x1000"); // 验证StringList解析
        assert results.getTlbHitStatusSequenceJson().get(1) == false; // 验证BooleanList解析
        assert results.getSingleAccessTimeSequenceJson().get(2) == 300L; // 验证LongList解析
    }

    /**
     * 测试查询所有记录（验证批量解析）
     */
    @Test
    public void testSelectList() {
        List<ExperimentalResults> resultsList = experimentalResultsMapper.selectList(null);
        log.info("查询所有记录，共{}条", resultsList.size());

        // 遍历验证
        for (ExperimentalResults results : resultsList) {
            log.info("记录ID：{}，算法类型：{}，TLB命中率：{}",
                    results.getId(), results.getAlgorithmType(), results.getTlbHitRate());
            // 验证JSON数组字段非空
            assert results.getPhysicalPageNumberSequenceJson() != null;
            assert results.getMemoryAddressSequenceJson() != null;
        }
    }

    /**
     * 测试更新（修改算法类型+JSON数组字段）
     */
    @Test
    public void testUpdate() {
        // 1. 查询原有数据
        Integer id = 1;
        ExperimentalResults results = experimentalResultsMapper.selectById(id);
        assert results != null;

        // 2. 修改字段
        results.setAlgorithmType("FIFO页面置换算法"); // 修改普通字段
        // 修改JSON数组字段
        List<String> newMemoryAddressList = new ArrayList<>();
        newMemoryAddressList.add("0x4000");
        newMemoryAddressList.add("0x5000");
        results.setMemoryAddressSequenceJson(newMemoryAddressList);

        // 3. 执行更新
        int update = experimentalResultsMapper.updateById(results);
        log.info("更新结果：{}行受影响", update);
        assert update == 1;

        // 4. 验证更新
        ExperimentalResults updatedResults = experimentalResultsMapper.selectById(id);
        assert "FIFO页面置换算法".equals(updatedResults.getAlgorithmType());
        assert updatedResults.getMemoryAddressSequenceJson().size() == 2;
        assert updatedResults.getMemoryAddressSequenceJson().contains("0x5000");
    }

    /**
     * 测试逻辑删除（修改isDeleted字段）
     */
    @Test
    public void testLogicDelete() {
        Integer id = 2;
        experimentalResultsMapper.deleteById( id); // 标记为逻辑删除
        // 验证删除后查询（如果Mapper配置了逻辑删除，selectById会返回null）
        ExperimentalResults deletedResults = experimentalResultsMapper.selectById(id);
        System.out.println(deletedResults);
    }
}
