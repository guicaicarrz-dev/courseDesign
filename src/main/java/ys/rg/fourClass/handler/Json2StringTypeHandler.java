package ys.rg.fourClass.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.handlers.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 适配 MySQL JSON 类型的自定义处理器：
 * 1. 写库：Java对象（List/Map）→ JSON字符串 → 存入MySQL JSON类型字段
 * 2. 读库：直接返回JSON字符串（不反序列化，透传前端）
 */
@MappedTypes({Object.class}) // 支持所有Java对象（List/Map等）
@MappedJdbcTypes({JdbcType.JSON, JdbcType.OTHER, JdbcType.VARCHAR}) // 适配MySQL JSON类型（核心调整）
public class Json2StringTypeHandler extends BaseTypeHandler<Object> {

    /**
     * 写库核心逻辑：适配MySQL JSON类型字段的写入
     * @param parameter 传入的List/Map等Java对象
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        // 1. 将Java对象序列化为标准JSON字符串
        String jsonStr = JSON.toJSONString(parameter);
        // 2. 适配MySQL JSON类型：用setObject指定类型为OTHER（MySQL JSON类型的JDBC映射）
        // 兼容VARCHAR类型：若字段是VARCHAR，setString也生效
        if (jdbcType == JdbcType.JSON || jdbcType == JdbcType.OTHER) {
            ps.setObject(i, jsonStr, JdbcType.OTHER);
        } else {
            ps.setString(i, jsonStr);
        }
    }

    /**
     * 读库：按列名读取MySQL JSON类型字段，直接返回字符串
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // MySQL JSON类型字段用getString可直接获取JSON字符串（驱动自动转换）
        return rs.getString(columnName);
    }

    /**
     * 读库：按列下标读取，直接返回JSON字符串
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    /**
     * 读库：CallableStatement读取，直接返回JSON字符串
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
