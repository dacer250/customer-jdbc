package com.gupao.edu.jdbc.framework.core;

import com.gupao.edu.jdbc.framework.exception.MissPkException;
import com.gupao.edu.jdbc.util.EntityOperationUtils;
import com.gupao.edu.jdbc.util.ReflectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 张明
 */
public abstract class BaseDaoSupport<T extends Serializable, PK extends Serializable> extends JdbcDaoSupport {


    private static final Logger logger = LoggerFactory.getLogger(BaseDaoSupport.class);

    @Resource
    private void setCustomeJdbcTemplate(JdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }


    private EntityOperation entityOperation;

    public BaseDaoSupport() {
        //初始化所有映射
        //获取当前class
        Class currentClazz = this.getClass();
        //获取当前泛型参数
        Class<T> superClassGenricType = ReflectUtils.getSuperClassGenricType(currentClazz, 0);
        //记录sql 相关
        entityOperation = EntityOperationUtils.init(superClassGenricType);
        entityOperation.setOperationClass(superClassGenricType);
    }

    public EntityOperation getEntityOperation() {
        return entityOperation;
    }

    public int insert(T t) {
        return 0;
    }

    public int updateByPrimaryKey(T t) throws Exception {
        if (t == null) {
            throw new NullPointerException("primary key is null");
        }
        StringBuilder sqlBuilder = new StringBuilder("update ");
        //获取表名
        Class<? extends Serializable> aClass = t.getClass();
        List<Object> valueList = new ArrayList<>();
        sqlBuilder.append(entityOperation.getTableName()).append("  set").append(" ");
        Field[] declaredFields = aClass.getDeclaredFields();
        //获取主键
        if (declaredFields != null && declaredFields.length > 0) {
            String pk = null;
            Object idValue = null;
            int columnCount = 0;
            for (Field field : declaredFields) {
                Column annotation = field.getAnnotation(Column.class);
                if (field.isAnnotationPresent(Id.class)) {
                    pk = annotation == null ? field.getName() : annotation.name();
                    field.setAccessible(true);
                    idValue = field.get(t);
                    continue;
                }
                if (annotation != null) {
                    sqlBuilder.append(annotation.name()).append("=?").append(",");
                    columnCount++;
                    field.setAccessible(true);
                    Object value = field.get(t);
                    if (value == null) {
                        continue;
                    }
                    valueList.add(value);
                }
            }
            if (pk == null) {
                throw new MissPkException();
            }
            if (columnCount == 0) {
                return 0;
            }
            //去除最后一个逗号
            String sql = sqlBuilder.substring(0, sqlBuilder.length() - 1);
            sqlBuilder = new StringBuilder(sql);
            sqlBuilder.append(" where ").append(pk).append("=?");
            valueList.add(idValue);
            return getJdbcTemplate().update(sqlBuilder.toString(), valueList.toArray());
        } else {
            return 0;
        }

    }


    public int deleteByPrimaryKey(PK pk) {
        if (pk == null) {
            throw new MissPkException();
        }
        StringBuilder sqlBuilder = new StringBuilder("delete from ");
        sqlBuilder.append(entityOperation.getTableName()).append(" where ");
        sqlBuilder.append(entityOperation.getPkColumn()).append("=").append("?");
        return this.getJdbcTemplate().update(sqlBuilder.toString(), pk);
    }

    public int insertAll(List<T> entityList) {
        for (T t : entityList) {
            insert(t);
        }
        return 1;
    }


    /**
     * 根据查询条件获得一个对象
     *
     * @param queryRule
     * @return
     */
    protected List<T> select(QueryRule queryRule) {
        QueryRuleSqlBuilder bulider = new QueryRuleSqlBuilder(queryRule);
        String whereSql = bulider.getWhereSql();
        String orderSql = bulider.getOrderSql();
        StringBuffer sqlBuilder = new StringBuffer("select " + this.entityOperation.getAllCoulnName() + " from " + this.entityOperation.getTableName());
        if (StringUtils.isNotBlank(whereSql)) {
            sqlBuilder.append(QueryRuleSqlBuilder.SQL_EMPTY_SEPERATOR).append("where").append(QueryRuleSqlBuilder.SQL_EMPTY_SEPERATOR).append(whereSql);
        }
        if (StringUtils.isNotBlank(orderSql)) {
            sqlBuilder.append(QueryRuleSqlBuilder.SQL_EMPTY_SEPERATOR).append(orderSql);
        }
        String sql = sqlBuilder.toString();
        Object[] valuesArr = bulider.getValuesArr();
        logger.debug(sql);
        logger.debug(valuesArr.toString());
        List<T> query = getJdbcTemplate().query(sql, entityOperation.getSimpleRowMapper(), bulider.getValuesArr());
        return query;
    }

    protected List<Map<String, Object>> selectBySql(String sql, List<Object> params) throws Exception {
        return this.getJdbcTemplate().queryForList(sql, params.toArray());
    }


}
