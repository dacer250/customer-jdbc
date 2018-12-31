package com.gupao.edu.jdbc.framework.core;

import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityOperation<T> {

    private String tableName;

    private Map<String, FieldMapping> fieldMappings;

    private Field pkField;

    private String pkName;

    private String allCoulnName;

    private Class<T> operationClass;

    private RowMapper<T> simpleRowMapper;

    private String pkColumn;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, FieldMapping> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(Map<String, FieldMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public Field getPkField() {
        return pkField;
    }

    public void setPkField(Field pkField) {
        this.pkField = pkField;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getAllCoulnName() {
        return allCoulnName;
    }

    public void setAllCoulnName(String allCoulnName) {
        this.allCoulnName = allCoulnName;
    }

    public Class getOperationClass() {
        return operationClass;
    }


    public void setOperationClass(Class<T> operationClass) {
        this.operationClass = operationClass;
    }

    public RowMapper<T> getSimpleRowMapper() {
        return simpleRowMapper;
    }

    public void setSimpleRowMapper(RowMapper<T> simpleRowMapper) {
        this.simpleRowMapper = simpleRowMapper;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }
}
