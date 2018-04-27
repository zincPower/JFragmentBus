package com.zinc.libfragment_bus.bean;

import java.lang.reflect.Method;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description 保存已经订阅的信息
 */

public class SubscriptionInfo {

    //订阅的对象
    private Object subscribe;

    //订阅的方法
    private SubscribeMethodInfo subscribeMethod;

    public SubscriptionInfo(Object subscribe, SubscribeMethodInfo subscribeMethod) {
        this.subscribe = subscribe;
        this.subscribeMethod = subscribeMethod;
    }

    public Object getSubscribe() {
        return subscribe;
    }

    public SubscribeMethodInfo getSubscribeMethod() {
        return subscribeMethod;
    }
}
