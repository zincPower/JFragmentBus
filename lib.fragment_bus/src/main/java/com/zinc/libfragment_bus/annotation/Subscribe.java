package com.zinc.libfragment_bus.annotation;

import com.zinc.libfragment_bus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    String[] value();

    //线程模式(默认是和调用的线程一样)
    String threadMode() default ThreadMode.POSTING;

}
