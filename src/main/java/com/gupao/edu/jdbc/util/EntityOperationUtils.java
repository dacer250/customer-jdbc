package com.gupao.edu.jdbc.util;

import com.gupao.edu.jdbc.framework.core.EntityOperation;
import com.gupao.edu.jdbc.framework.core.FieldMapping;
import com.gupao.edu.jdbc.framework.core.SimpleRowMapper;
import com.gupao.edu.jdbc.framework.exception.MissPkException;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EntityOperationUtils {


    static final Set<Class<?>> SUPPORTED_SQL_OBJECTS = new HashSet<Class<?>>();

    public static final String BOOLEAN_IS_FIELD_PREFIX = "is";


    //初始化sql支持的数据类型
    static {
        Class<?>[] classes = {
                boolean.class, Boolean.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                String.class,
                Date.class,
                Timestamp.class,
                BigDecimal.class
        };
        SUPPORTED_SQL_OBJECTS.addAll(Arrays.asList(classes));
    }

    public static <T> EntityOperation init(Class<T> entityClass) {
        EntityOperation entityOperation = new EntityOperation();
        //获取对应的注解
        Table table = entityClass.getAnnotation(Table.class);
        entityOperation.setTableName(table == null ? entityClass.getSimpleName() : table.name());
        Map<String, FieldMapping> allFiledMappings = findAllFiledMappings(entityClass);
        entityOperation.setOperationClass(entityClass);
        entityOperation.setFieldMappings(allFiledMappings);
        //获取主键
        FieldMapping pkFromMapping = getPkFromMapping(entityOperation.getFieldMappings());
        if (pkFromMapping == null) {
            throw new MissPkException();
        }
        entityOperation.setPkField(pkFromMapping == null ? null : pkFromMapping.getField());
        entityOperation.setPkName(pkFromMapping == null ? null : pkFromMapping.getField().getName());
        entityOperation.setPkColumn(pkFromMapping.getColumnName());
        //将主键字段找出来
        entityOperation.setAllCoulnName(allFiledMappings.keySet().toString().replace("[", "").replace("]", "").replaceAll(" ", ""));
       entityOperation.setSimpleRowMapper(createRowMapper(entityOperation));
        return entityOperation;
    }


    private static <T> RowMapper createRowMapper(EntityOperation<T> entityOperation) {
        SimpleRowMapper<T>simpleRowMapper = new SimpleRowMapper(entityOperation.getFieldMappings());
         simpleRowMapper.setClazz(entityOperation.getOperationClass());
        return  simpleRowMapper;
    }


    private static String getterGetterName(Field field) {
        if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            if (field.getName().startsWith(BOOLEAN_IS_FIELD_PREFIX)) {
                if (field.getName().length() == 2) {
                    return "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                } else if (field.getName().length() == 3) {
                    return "get" + Character.toUpperCase(field.getName().charAt(2));
                } else {
                    return "get" + Character.toUpperCase(field.getName().charAt(2)) + field.getName().substring(3);
                }
            } else {
                return "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            }
        } else {
            return "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        }
    }


    private static boolean checkGetterMethod(Method method) {
        if (method == null) {
            return false;
        }

        if (method.getModifiers() == Modifier.STATIC) {
            return false;
        }
        if (method.getReturnType() == Void.class) {
            return false;
        }
        if (method.getParameterCount() != 0) {
            return false;
        }
        if (!isSupportSqlType(method.getReturnType())) {
            return false;
        }

        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getName().length() < 4) {
            return false;
        }
        return true;
    }

    private static <T> Map<String, FieldMapping> findAllFiledMappings(Class<T> entityClass) {
        Map<String, FieldMapping> fieldMappingMap = new HashMap<>(16);
        Field[] declaredFields = entityClass.getDeclaredFields();
        if (declaredFields != null) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                Column annotation = field.getAnnotation(Column.class);
                //根据字段获取名称
                String getterName = getterGetterName(field);
                String setterName = getterSetterName(field);
                Method getter = null;
                Method setter = null;
                try {
                    getter = entityClass.getDeclaredMethod(getterName);
                    setter = entityClass.getDeclaredMethod(setterName, field.getType());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    continue;
                }
                if (!checkGetterMethod(getter)) {
                    continue;
                }
                if (!checkSetterMethod(setter)) {
                    continue;
                }
                FieldMapping fieldMapping = new FieldMapping(annotation, setter, getter, field);
                if (field.isAnnotationPresent(Id.class)) {
                    fieldMapping.setId(true);
                }
                fieldMappingMap.put(field.getName(), fieldMapping);
            }
        }
        return fieldMappingMap;
    }

    /**
     * 检查setter方法合法性
     *
     * @param setter
     * @return
     */
    private static boolean checkSetterMethod(Method setter) {
        if (setter.getModifiers() == Modifier.STATIC) {
            return false;
        }
        if (setter.getReturnType() == Void.class) {
            return false;
        }
        if (!setter.getName().startsWith("set")) {
            return false;
        }

        if (setter.getName().length() < 4) {
            return false;
        }
        if (setter.getParameterCount() != 1) {
            return false;
        }

        if (!isSupportSqlType(setter.getParameterTypes()[0])) {
            return false;
        }
        return true;
    }

    private static String getterSetterName(Field field) {
        if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            if (field.getName().startsWith(BOOLEAN_IS_FIELD_PREFIX)) {
                if (field.getName().length() == 2) {
                    return "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                } else if (field.getName().length() == 3) {
                    return "set" + Character.toUpperCase(field.getName().charAt(2));
                } else {
                    return "set" + Character.toUpperCase(field.getName().charAt(2)) + field.getName().substring(3);
                }
            } else {
                return "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            }
        } else {
            return "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        }
    }


    public static boolean isSupportSqlType(Class clazz) {
        if (SUPPORTED_SQL_OBJECTS.contains(clazz)) {
            return true;
        }
        return false;
    }


    /**
     * 从映射关系中找出主键
     *
     * @param mappings
     * @return
     */
    private static FieldMapping getPkFromMapping(Map<String, FieldMapping> mappings) {
        for (Map.Entry<String, FieldMapping> entry : mappings.entrySet()) {
            //如果为主键字段
            if (entry.getValue().getId()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
