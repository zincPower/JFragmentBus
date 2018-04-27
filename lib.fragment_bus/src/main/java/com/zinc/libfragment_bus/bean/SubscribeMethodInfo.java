package com.zinc.libfragment_bus.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description 标记了注解方法信息
 */

public class SubscribeMethodInfo {

    //注解的标签
    private String label;

    //线程模式
    private String threadMode;

    //标注了注解的方法
    private Method method;

    //参数的类型
    private Class[] parameterClasses;

    public SubscribeMethodInfo(String label, String threadMode, Method method, Class[] parameterClasses) {
        this.label = label;
        this.threadMode = threadMode;
        this.method = method;
        this.parameterClasses = parameterClasses;
    }

    public String getLabel() {
        return label;
    }

    public String getThreadMode() {
        return threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public Class[] getParameterClasses() {
        return parameterClasses;
    }

}
