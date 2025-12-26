package ys.rg.fourClass.handler;

import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 适配 MySQL TEXT/VARCHAR 类型的自定义处理器：
 * 1. 写库：Java对象（List/Map）→ JSON字符串 → 存入MySQL TEXT/VARCHAR字段
 * 2. 读库：直接返回JSON字符串（透传前端）
 */
@MappedTypes({Object.class}) // 支持所有Java对象（List/Map等）
@MappedJdbcTypes({JdbcType.VARCHAR}) // 仅适配TEXT/VARCHAR类型
public class Json2StringTypeHandler extends BaseTypeHandler<Object> {

    /**
     * 写库：Java对象转JSON字符串存入数据库（统一适配TEXT/VARCHAR）
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String jsonStr = JSON.toJSONString(parameter);
        // 移除JSON类型判断，TEXT/VARCHAR统一用setString写入
        ps.setString(i, jsonStr);
    }

    /**
     * 读库：按列名读取TEXT/VARCHAR字段中的JSON字符串
     */
    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    /**
     * 读库：按列下标读取TEXT/VARCHAR字段中的JSON字符串
     */
    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    /**
     * 读库：CallableStatement读取TEXT/VARCHAR字段中的JSON字符串
     */
    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
