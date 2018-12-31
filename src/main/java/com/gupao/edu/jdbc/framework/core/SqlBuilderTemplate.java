package com.gupao.edu.jdbc.framework.core;

import static com.gupao.edu.jdbc.framework.core.QueryRuleSqlBuilder.SQL_EMPTY_SEPERATOR;

public class SqlBuilderTemplate {


    public static StringBuilder proccessPrefix(QueryRule.Rule rule) {
        StringBuilder sqlChar = new StringBuilder();
        sqlChar.append(QueryRuleSqlBuilder.getAndOr(rule.getAndOr())).append(QueryRuleSqlBuilder.SQL_EMPTY_SEPERATOR);
        if (rule.isNeedPrefix()) {
            sqlChar.append("(").append(SQL_EMPTY_SEPERATOR);
        } else {
            sqlChar.append(SQL_EMPTY_SEPERATOR);
        }

        return sqlChar;
    }


    public static void proccessSuffix(QueryRule.Rule rule, StringBuilder sqlChar, QueryRuleSqlBuilder queryRuleSqlBuilder) {
        if (rule.isNeedSuffix()) {
            sqlChar.append(")").append(SQL_EMPTY_SEPERATOR);
        } else {
            sqlChar.append(SQL_EMPTY_SEPERATOR);
        }
        queryRuleSqlBuilder.add(rule.getProperty(), sqlChar.toString().trim(), rule.getValues());
    }
}
