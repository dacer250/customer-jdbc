package com.gupao.edu.jdbc.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtils {

   private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);


    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if
     * cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {
        //获取泛型参数
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            return Object.class;
        }
        if (!(genericSuperclass instanceof ParameterizedType)) {
            logger.debug(clazz.getSimpleName()
                    + "'s superclass not ParameterizedType");
            return Object.class;
        }
        ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments!=null&&actualTypeArguments.length>0){
            int size=actualTypeArguments.length;
            if (size-1<index){
                throw new ArrayIndexOutOfBoundsException(size);
            }
            if (!(actualTypeArguments[index] instanceof Class)) {
                logger.warn(clazz.getSimpleName()
                                + " not set the actual class on superclass generic parameter");
                return Object.class;
            }
            return (Class) actualTypeArguments[index];
        }
        return Object.class;
    }


}
