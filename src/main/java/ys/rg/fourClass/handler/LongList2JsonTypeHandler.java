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
import java.util.Collections;
import java.util.List;

/**
 * 适配JDK 1.8：处理 List<Long> ↔ JSON字符串
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes(List.class)
public class LongList2JsonTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        String jsonStr = JSON.toJSONString(parameter);
        ps.setString(i, jsonStr);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonStr = rs.getString(columnName);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonStr = rs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonStr = cs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    private List<Long> parseJsonToList(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            // JDK 1.8 用 Collections.emptyList() 替代 List.of()
            return Collections.emptyList();
        }
        return JSON.parseArray(jsonStr, Long.class);
    }
}
