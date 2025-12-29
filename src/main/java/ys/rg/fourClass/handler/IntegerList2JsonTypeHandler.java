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
 * 专门处理 List<Integer> 的TypeHandler（对应数据库TEXT/VARCHAR）
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes(List.class)
public class IntegerList2JsonTypeHandler extends BaseTypeHandler<List<Integer>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        String jsonStr = JSON.toJSONString(parameter);
        ps.setString(i, jsonStr);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonStr = rs.getString(columnName);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonStr = rs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonStr = cs.getString(columnIndex);
        return parseJsonToList(jsonStr);
    }

    private List<Integer> parseJsonToList(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        // 明确指定解析为List<Integer>
        return JSON.parseArray(jsonStr, Integer.class);
    }
}
