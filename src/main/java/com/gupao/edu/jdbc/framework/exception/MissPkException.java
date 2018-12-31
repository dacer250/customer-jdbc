package com.gupao.edu.jdbc.framework.exception;

import com.gupao.edu.jdbc.util.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主键缺少异常
 */
public class MissPkException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
    private String msg = "missing pk  please mustadd pk(primary key)";

    public MissPkException() {
        logger.info(msg);
    }


}
