package com.gupao.edu.jdbc.custome;

import com.gupao.edu.jdbc.framework.core.BaseDaoSupport;
import com.gupao.edu.jdbc.framework.core.QueryRule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TestDao extends BaseDaoSupport<User, Long> {

    public void selectByName(String name) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andEqual("name", name);
        List<User> select = select(queryRule);
        Optional<List<User>> optional = Optional.of(select);
        if (optional.isPresent()) {
            for (User user : select) {
                System.out.println(user.toString());
            }
        } else {
            System.out.println("null");
        }
    }

    public void selectBetweenIds(List<Object>ids) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andBetween("id", ids.toArray());
        List<User> select = select(queryRule);
        Optional<List<User>> optional = Optional.of(select);
        if (optional.isPresent()) {
            for (User user : select) {
                System.out.println(user.toString());
            }
        } else {
            System.out.println("null");
        }
    }



    public void selectIdsIn(List<Long>ids) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andIn("id", ids.toArray());
        List<User> select = select(queryRule);
        Optional<List<User>> optional = Optional.of(select);
        if (optional.isPresent()) {
            for (User user : select) {
                System.out.println(user.toString());
            }
        } else {
            System.out.println("null");
        }
    }

    public void selectTest(List<Long>ids) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andLike("name","a");
        queryRule.andIsEmptyNeedPrefix("name");
        queryRule.orNotInNeedSuffix("id",ids.toArray());
        queryRule.asc("id");
        queryRule.desc("name");
        List<User> select = select(queryRule);
        Optional<List<User>> optional = Optional.of(select);
        if (optional.isPresent()) {
            for (User user : select) {
                System.out.println(user.toString());
            }
        } else {
            System.out.println("null");
        }
    }


}
