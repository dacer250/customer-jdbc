package com.gupao.edu.jdbc.framework.core;

import com.gupao.edu.jdbc.framework.exception.MissBracketsException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张明
 */
public class QueryRuleSqlBuilder {


    public static final String SQL_EMPTY_SEPERATOR = "  ";

    public static final String SQL_PREPARE_VALUE = "?";

    private List<String> sqlCharlList;

    private String whereSql;

    private String orderSql = "";

    int i = 0;

    /**
     * 保存列名列表
     */
    private List<String> propertiesList;
    /**
     * //保存参数值列表
     */
    private List<Object> values;


    /**
     * 用于替换问号
     */
    private Object[] valuesArr;

    public static final Pattern PATTERN = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);


    /**
     * 创建SQL构造器
     *
     * @param queryRule
     */
    public QueryRuleSqlBuilder(QueryRule queryRule) throws MissBracketsException {
        if (queryRule==null){
            throw new NullPointerException();
        }
        if (queryRule.getBracket()/2!=0){
            throw new MissBracketsException();
        }
        propertiesList = new ArrayList(queryRule.getRules().size());
        values = new ArrayList();
        sqlCharlList = new ArrayList<>(queryRule.getRules().size());
        for (QueryRule.Rule rule : queryRule.getRules()) {
            switch (rule.getType()) {
                case QueryRule.BETWEEN:
                    processBetween(rule);
                    break;
                case QueryRule.EQ:
                    processEqual(rule);
                    break;
                case QueryRule.LIKE:
                    processLike(rule);
                    break;
                case QueryRule.NOTEQ:
                    processNotEqual(rule);
                    break;
                case QueryRule.GT:
                    processGreaterThen(rule);
                    break;
                case QueryRule.GE:
                    processGreaterEqual(rule);
                    break;
                case QueryRule.LT:
                    processLessThen(rule);
                    break;
                case QueryRule.LE:
                    processLessEqual(rule);
                    break;
                case QueryRule.IN:
                    processIN(rule);
                    break;
                case QueryRule.NOTIN:
                    processNotIN(rule);
                    break;
                case QueryRule.ISNULL:
                    processIsNull(rule);
                    break;
                case QueryRule.ISNOTNULL:
                    processIsNotNull(rule);
                    break;
                case QueryRule.ISEMPTY:
                    processIsEmpty(rule);
                    break;
                case QueryRule.ISNOTEMPTY:
                    processIsNotEmpty(rule);
                    break;
                default:
                    throw new IllegalArgumentException("type " + rule.getType() + " not supported.");
            }
        }

        for (QueryRule.Order order : queryRule.getOrders()) {
            if (order.getAsc()) {
                processAsc(order);
            } else {
                processDesc(order);
            }
        }
        //拼装where语句
        appendWhereSql();
        //拼装参数值
        appendValues();
    }

    private void appendValues() {
        valuesArr = values.toArray(new Object[values.size()]);
    }


    private void appendWhereSql() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String sqlChar : sqlCharlList) {
            stringBuilder.append(sqlChar).append(SQL_EMPTY_SEPERATOR);
        }
        whereSql = removeSelect(removeOrders(stringBuilder.toString()));
    }


    protected String removeSelect(String sql) {
        if (sql.toLowerCase().matches("from\\s+")) {
            int beginPos = sql.toLowerCase().indexOf("from");
            return sql.substring(beginPos).replaceFirst("AND", "");
        } else {
            return sql.replaceFirst("AND", "");
        }
    }

    /**
     * 去掉order
     *
     * @param sql
     * @return
     */
    protected String removeOrders(String sql) {
        Pattern p = PATTERN;
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }


    private void processDesc(QueryRule.Order order) {
        ascOrDesc(order);
    }

    private void processAsc(QueryRule.Order order) {
        ascOrDesc(order);
    }

    private void ascOrDesc(QueryRule.Order order) {
        if (order.getProperties() == null || order.getProperties().size() == 0) {
            return;
        }
        StringBuilder orderSqlBuilder = new StringBuilder(orderSql);
        if (StringUtils.isBlank(orderSql)) {
            orderSqlBuilder.append("order by ").append(SQL_EMPTY_SEPERATOR);
        } else {
            orderSqlBuilder.append(",").append(SQL_EMPTY_SEPERATOR);
        }
        int i = 0;
        for (String property : order.getProperties()) {
            orderSqlBuilder.append(property);
            if (i != order.getProperties().size() - 1) {
                orderSqlBuilder.append(",");
            }
            orderSqlBuilder.append(SQL_EMPTY_SEPERATOR);
            i++;
        }
        if (order.getAsc()) {
            orderSqlBuilder.append("asc");
        } else {
            orderSqlBuilder.append("desc");
        }
        orderSqlBuilder.append(SQL_EMPTY_SEPERATOR);
        orderSql = orderSqlBuilder.toString();
    }


    private void processIsNotEmpty(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("!= ''").append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processIsEmpty(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("= ''").append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processIsNotNull(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("is not null").append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processIsNull(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("is null").append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processNotIN(QueryRule.Rule rule) {
        inAndNotIn(rule, Boolean.FALSE);
    }

    private void processIN(QueryRule.Rule rule) {
        inAndNotIn(rule, Boolean.TRUE);
    }


    private void inAndNotIn(QueryRule.Rule rule, boolean isIn) {
        Object[] values = rule.getValues();
        if (ArrayUtils.isEmpty(values)) {
            return;
        }
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule);
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                sqlChar.append(SQL_EMPTY_SEPERATOR).append(rule.getProperty()).append(SQL_EMPTY_SEPERATOR);
                if (isIn) {
                    sqlChar.append("in").append(SQL_EMPTY_SEPERATOR);
                } else {
                    sqlChar.append(" not  in").append(SQL_EMPTY_SEPERATOR);
                }
                sqlChar.append("(").append(SQL_PREPARE_VALUE).append(",").append(SQL_EMPTY_SEPERATOR);
                continue;
            }
            if (i!=0&&i!=values.length-1) {
                sqlChar.append(SQL_PREPARE_VALUE).append(",").append(SQL_EMPTY_SEPERATOR);
                continue;
            }
            if (i == values.length - 1) {
                sqlChar.append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR).append(")").append(SQL_EMPTY_SEPERATOR);
                continue;
            }
        }
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }


    private void processLessEqual(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("<=").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processLessThen(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("<").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processGreaterEqual(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append(">=").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processGreaterThen(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append(">").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processNotEqual(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("!=").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processLike(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("like").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        Object[] values = rule.getValues();
        values[0] = "%" + rule.getValues()[0] + "%";
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processEqual(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("=").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    private void processBetween(QueryRule.Rule rule) {
        StringBuilder sqlChar = SqlBuilderTemplate.proccessPrefix(rule)
                .append(SQL_EMPTY_SEPERATOR).append(rule.getProperty())
                .append(SQL_EMPTY_SEPERATOR).append("between").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE)
                .append(SQL_EMPTY_SEPERATOR).append("and").append(SQL_EMPTY_SEPERATOR)
                .append(SQL_PREPARE_VALUE).append(SQL_EMPTY_SEPERATOR);
        SqlBuilderTemplate.proccessSuffix(rule, sqlChar, this);
    }

    public void add(String properties, String sqlChar, Object[] value) {
        propertiesList.add(properties);
        if (value!=null) {
            for (Object object : value) {
                values.add(i, object);
                i++;
            }
        }
        sqlCharlList.add(sqlChar);
    }


    public static String getAndOr(int andOr) {
        if (andOr == QueryRule.AND) {
            return "AND";
        }
        if (andOr == QueryRule.OR) {
            return "OR";
        }
        return SQL_EMPTY_SEPERATOR;
    }

    public List<String> getSqlCharlList() {
        return sqlCharlList;
    }

    public void setSqlCharlList(List<String> sqlCharlList) {
        this.sqlCharlList = sqlCharlList;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }

    public List<String> getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(List<String> propertiesList) {
        this.propertiesList = propertiesList;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public Object[] getValuesArr() {
        return valuesArr;
    }

    public void setValuesArr(Object[] valuesArr) {
        this.valuesArr = valuesArr;
    }


    /**
     * 构建单条插入的SQL
     * @param tableName
     * @param params
     * @return
     */
    public String buliderForInsert(String tableName,Map<String,Object> params){
        if(null == tableName || tableName.trim().length() == 0 || params == null || params.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName);

        StringBuffer sbKey = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();

        sbKey.append("(");
        sbValue.append("(");
        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            sbKey.append(key);
            sbValue.append(" :").append(key);
            if(index != set.size() - 1){
                sbKey.append(",");
                sbValue.append(",");
            }
            index++;
        }
        sbKey.append(")");
        sbValue.append(")");

        sb.append(sbKey).append("VALUES").append(sbValue);

        return sb.toString();
    }



}
