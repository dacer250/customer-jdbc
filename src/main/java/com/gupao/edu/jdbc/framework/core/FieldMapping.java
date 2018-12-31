package com.gupao.edu.jdbc.framework.core;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 张明
 * @description 数据库字段Y与java属性相对应
 */
public class FieldMapping {

    private Boolean insertable;

    private Boolean updatable;

    private Method setter;

    private Method getter;

    private Boolean nullable;

    private String columnName;

    private Field field;

    private Boolean id = false;

    /**
     * 返回值是否是枚举
     */
    private Class enumClass;


    public FieldMapping(Boolean insertable, Boolean updatable, Method setter, Method getter, Boolean nullable, String columnName, Field field, Class enumClass) {
        this.insertable = insertable;
        this.updatable = updatable;
        this.setter = setter;
        this.getter = getter;
        this.nullable = nullable;
        this.columnName = columnName;
        this.field = field;
        this.enumClass = enumClass;
    }

    public FieldMapping(Column column, Method setter, Method getter, Field field) {
        this.insertable = column == null ? true : column.insertable();
        this.updatable = column == null ? true : column.updatable();
        this.setter = setter;
        this.getter = getter;
        this.nullable = column == null ? true : column.nullable();
        this.columnName = column == null ? field.getName() : column.name();
        this.field = field;
        this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
    }


    public Boolean getInsertable() {
        return insertable;
    }

    public void setInsertable(Boolean insertable) {
        this.insertable = insertable;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    public Boolean getId() {
        return id;
    }

    public void setId(Boolean id) {
        this.id = id;
    }
}
