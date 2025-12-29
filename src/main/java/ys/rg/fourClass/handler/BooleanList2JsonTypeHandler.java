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
 * 适配JDK 1.8：处理 List<Boolean> ↔ JSON字符串
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes(List.class)
public class BooleanList2JsonTypeHandler extends BaseTypeHandler<List<Boolean>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Boolean> parameter, JdbcType jdbcType) throws SQLException {
        String jsonStr = JSON.toJSONString(parameter);
        ps.setString(i, jsonStr);
    }

    @Override
    public List<Boolean> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonStr = rs.getString(columnName);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Boolean> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonStr = rs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Boolean> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonStr = cs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    private List<Boolean> parseJsonToList(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return JSON.parseArray(jsonStr, Boolean.class);
    }
}
