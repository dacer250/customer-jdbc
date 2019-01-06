package com.gupao.edu.jdbc.framework.exception;

import com.gupao.edu.jdbc.util.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissBracketsException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
    private static  String msg = "missing bracket ( or  )";

    public MissBracketsException() {
       super(msg);
    }

}
