package com.gupao.edu.jdbc.framework.core;

import com.gupao.edu.jdbc.util.ReflectUtils;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**0
 * @author 张明
 */
public class SimpleRowMapper<T> implements RowMapper<T> {


    private Map<String, FieldMapping> fieldMappings;


    private Class<T> clazz;

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Implementations must implement this method to map each row of data
     * in the ResultSet. This method should not call {@code next()} on
     * the ResultSet; it is only supposed to map values of the current row.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the result object for the current row
     * @throws SQLException if a SQLException is encountered getting
     *                      column values (that is, there's no need to catch SQLException)
     */
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        Class superClassGenricType =clazz;
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        T newInstance = null;
        try {
            newInstance = (T) superClassGenricType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= columnCount; i++) {
            String catalogName = metaData.getColumnName(i);
            Object value = rs.getObject(i);
            //通过属性找setter方法
            FieldMapping fieldMapping = fieldMappings.get(catalogName);
            try {
                if (fieldMapping != null) {
                    fieldMapping.getSetter().invoke(newInstance, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return newInstance;
    }


    public SimpleRowMapper(Map<String, FieldMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }
}
