package com.gupao.edu.jdbc.framework.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author 张明
 * @description 用于构造条件查询对象
 */
public class QueryRule implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ASC_ORDER = 101;
    public static final int DESC_ORDER = 102;
    public static final int LIKE = 1;
    public static final int IN = 2;
    public static final int NOTIN = 3;
    public static final int BETWEEN = 4;
    public static final int EQ = 5;
    public static final int NOTEQ = 6;
    public static final int GT = 7;
    public static final int GE = 8;
    public static final int LT = 9;
    public static final int LE = 10;
    public static final int ISNULL = 11;
    public static final int ISNOTNULL = 12;
    public static final int ISEMPTY = 13;
    public static final int ISNOTEMPTY = 14;
    public static final int AND = 201;
    public static final int OR = 202;

    private List<Rule> rules;

    private int bracket = 0;

    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    private QueryRule() {
        rules = new ArrayList<>();
        orders = new ArrayList<>();
    }


    public int getBracket() {
        return bracket;
    }

    public QueryRule asc(String... properties) {
        orders.add(new Order(Arrays.asList(properties), true));
        return this;
    }

    public QueryRule desc(String... properties) {
        orders.add(new Order(Arrays.asList(properties), false));
        return this;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public QueryRule andIsEmptyNeedPrefix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, true).setAndOr(AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andIsEmptyNeedSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, false, true).setAndOr(AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andIsEmptyNeedPrefixAndSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, true, true).setAndOr(AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andIsEmpty(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY).setAndOr(AND));
        return this;
    }

    public QueryRule orIsEmpty(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY).setAndOr(OR));
        return this;
    }

    public QueryRule orIsEmptyNeedPrefix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orIsEmptyNeedSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, false, true).setAndOr(OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orIsEmptyNeedPrefixAndSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISEMPTY, true, true).setAndOr(AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andLike(String propertyName, String value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE).setAndOr(AND));
        return this;
    }


    public QueryRule andLikeNeedPrefix(String propertyName, String value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andLikeNeedSuffix(String propertyName, String value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andLikeNeedPrefixAndSuffix(String propertyName, String value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ).setAndOr(AND));
        return this;
    }

    public QueryRule andEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andEqualPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andBetween(String propertyName, Object... values) {
        this.rules.add(new Rule(propertyName, values, BETWEEN).setAndOr(AND));
        return this;
    }

    public QueryRule andBetweenNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andBetweenNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andBetweenPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andIn(String propertyName, List<Object> values) {
        this.rules.add(new Rule(propertyName, values.toArray(), IN).setAndOr(AND));
        return this;
    }

    public QueryRule andIn(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN).setAndOr(AND));
        return this;
    }

    public QueryRule andInNeedPrefix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andInNeedSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andInPrefixAndSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, IN, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orIn(String propertyName, List<Object> values) {
        this.rules.add(new Rule(propertyName, values.toArray(), IN).setAndOr(AND));
        return this;
    }

    public QueryRule orInNeedPrefix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN, true, false, OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orInNeedSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orInPrefixAndSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, IN, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andNotIn(String propertyName, List<Object> values) {
        this.rules.add(new Rule(propertyName, values.toArray(), NOTIN).setAndOr(AND));
        return this;
    }


    public QueryRule andNotInNeedPrefix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, true, false, OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andNotInNeedSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andNotInPrefixAndSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orNotIn(String propertyName, Object... values) {
        this.rules.add(new Rule(propertyName, values, NOTIN).setAndOr(OR));
        return this;
    }


    public QueryRule orNotInNeedPrefix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, true, false, OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orNotInNeedSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orNotInPrefixAndSuffix(String propertyName, Object[] value) {
        this.rules.add(new Rule(propertyName, value, NOTIN, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andNotEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ).setAndOr(AND));
        return this;
    }


    public QueryRule andNotEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andNotEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andNotEqualPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andGreaterThan(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT).setAndOr(AND));
        return this;
    }


    public QueryRule andGreaterThanNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andGreaterThanNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andGreaterThanPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andGreaterEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE).setAndOr(AND));
        return this;
    }


    public QueryRule andGreaterEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andGreaterEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andGreaterEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andLessThan(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT).setAndOr(AND));
        return this;
    }


    public QueryRule andLessThanNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andLessThanNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andLessThanNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andLessEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE).setAndOr(AND));
        return this;
    }


    public QueryRule andLessEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, true, false, AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andLessEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andLessEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orIsNull(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNULL).setAndOr(OR));
        return this;
    }

    public QueryRule orIsNullNeedPrefix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNULL, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orIsNullNeedSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNULL, false, true).setAndOr(OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orIsNullNeedPrefixAndSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNULL, true, true).setAndOr(OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orIsNotNull(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTNULL).setAndOr(OR));
        return this;
    }

    public QueryRule orIsNotNullNeedPrefix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTNULL, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orIsNotNullNeedSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTNULL, false, true).setAndOr(OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orIsNotNullNeedPrefixAndSuffix(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTNULL, true, true).setAndOr(OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orLike(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE).setAndOr(OR));
        return this;
    }


    public QueryRule orLikeNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orLikeNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orLikeNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LIKE, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ).setAndOr(OR));
        return this;
    }


    public QueryRule orEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, EQ, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orBetween(String propertyName, Object... values) {
        this.rules.add(new Rule(propertyName, values, BETWEEN).setAndOr(OR));
        return this;
    }


    public QueryRule orBetweenNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orBetweenNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orBetweenNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, BETWEEN, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orNotEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ).setAndOr(OR));
        return this;
    }

    public QueryRule orNotEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orNotEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orNotEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, NOTEQ, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orGreaterThan(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT).setAndOr(OR));
        return this;
    }


    public QueryRule orGreaterThanNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orGreaterThanNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orGreaterThanNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GT, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orGreaterEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE).setAndOr(OR));
        return this;
    }


    public QueryRule orGreaterEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orGreaterEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orGreaterEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, GE, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orLessThan(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT).setAndOr(OR));
        return this;
    }

    public QueryRule orLessThanNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orLessThanNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orLessThanNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LT, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orLessEqual(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE).setAndOr(OR));
        return this;
    }

    public QueryRule orLessEqualNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule orLessEqualNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, false, true, OR));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule orLessEqualNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, LE, true, true, OR));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule andIsNotEmpty(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTEMPTY).setAndOr(AND));
        return this;
    }


    public QueryRule andIsNotEmptyNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, true).setAndOr(AND));
        checkBrackets(true,false);
        return this;
    }


    public QueryRule andIsNotEmptyNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, false, true, AND));
        checkBrackets(false,true);
        return this;
    }

    public QueryRule andIsNotEmptyNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, true, true, AND));
        checkBrackets(true,true);
        return this;
    }


    public QueryRule orIsNotEmpty(String propertyName) {
        this.rules.add(new Rule(propertyName, ISNOTEMPTY).setAndOr(OR));
        return this;
    }

    public QueryRule orIsNotEmptyNeedPrefix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, true).setAndOr(OR));
        checkBrackets(true,false);
        return this;
    }

    public QueryRule orIsNotEmptyNeedSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, true, true, OR));
        checkBrackets(true,true);
        return this;
    }

    public QueryRule orIsNotEmptyNeedPrefixAndSuffix(String propertyName, Object value) {
        this.rules.add(new Rule(propertyName, new Object[]{value}, ISNOTEMPTY, false, true, OR));
        return this;
    }


    private void checkBrackets(boolean needPrefix, boolean needSuffix) {
        if (needPrefix){
            bracket++;
        }
        if (needSuffix){
            bracket++;
        }
    }


    public static QueryRule getInstance() {
        return new QueryRule();
    }


    /**
     * 条件对象
     */
    public static class Rule implements Serializable {
        private static final long serialVersionUID = 1L;

        private String property;

        private Object[] values;

        private int type;

        private boolean needPrefix = false;

        private boolean needSuffix = false;


        private int andOr = AND;

        public static final String prefix = "(";

        public static final String suffix = ")";


        public Rule(String property, Object[] values, int type, boolean needPrefix, boolean needSuffix, int andOr) {
            this.property = property;
            this.values = values;
            this.type = type;
            this.needPrefix = needPrefix;
            this.needSuffix = needSuffix;
            this.andOr = andOr;
        }

        public Rule(String property, Object[] values, int type) {
            this.property = property;
            this.values = values;
            this.type = type;
        }

        public Rule(String property, int type) {
            this.property = property;
            this.values = values;
        }

        public Rule(String property, int type, boolean needPrefix) {
            this.property = property;
            this.type = type;
            this.needPrefix = needPrefix;
        }


        public Rule(String property, int type, boolean needPrefix, boolean needSuffix) {
            this.property = property;
            this.type = type;
            this.needPrefix = needPrefix;
            this.needSuffix = needSuffix;
        }




        public Rule(String property, Object[] values, int type, boolean needPrefix) {
            this.property = property;
            this.values = values;
            this.type = type;
            this.needPrefix = needPrefix;
        }


        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isNeedPrefix() {
            return needPrefix;
        }

        public void setNeedPrefix(boolean needPrefix) {
            this.needPrefix = needPrefix;
        }

        public boolean isNeedSuffix() {
            return needSuffix;
        }

        public void setNeedSuffix(boolean needSuffix) {
            this.needSuffix = needSuffix;
        }

        public int getAndOr() {
            return andOr;
        }

        public Rule setAndOr(int andOr) {
            this.andOr = andOr;
            return this;
        }

        public Object[] getValues() {
            return values;
        }

        public void setValues(Object[] values) {
            this.values = values;
        }

        public static String getPrefix() {
            return prefix;
        }

        public static String getSuffix() {
            return suffix;
        }
    }

    public static class Order {

        private List<String> properties;

        private Boolean asc = true;


        public Order(List<String> properties, Boolean asc) {
            this.properties = properties;
            this.asc = asc;
        }

        public List<String> getProperties() {
            return properties;
        }

        public void setProperties(List<String> properties) {
            this.properties = properties;
        }

        public Boolean getAsc() {
            return asc;
        }

        public void setAsc(Boolean asc) {
            this.asc = asc;
        }
    }
}
