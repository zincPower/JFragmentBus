package com.zinc.libfragment_bus;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;

import com.zinc.libfragment_bus.annotation.Subscribe;
import com.zinc.libfragment_bus.bean.SubscribeMethodInfo;
import com.zinc.libfragment_bus.bean.SubscriptionInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description
 */

public class JFragmentBus {

    //缓存信息表，减少每次开启都需要反射的问题：类-->类中标记的注解的方法信息[可能多个方法]
    private final Map<Class, List<SubscribeMethodInfo>> CLASS_INFO_CACHE = new HashMap<>();

    //正在订阅的信息储存表：注解的value-->订阅的信息
    private final Map<String, List<SubscriptionInfo>> SUBSCRIBES = new HashMap<>();

    //注销辅助信息表
    private final Map<Class, List<String>> UNREGISTER_HELPER = new HashMap<>();

    private static final JFragmentBus ourInstance = new JFragmentBus();

    public static JFragmentBus getDefault() {
        return ourInstance;
    }

    private JFragmentBus() {
    }

    /**
     * @date 创建时间 2018/4/26
     * @author Jiang zinc
     * @Description 进行注册
     * @version
     */
    public void register(Object object) {

        List<SubscribeMethodInfo> subscribeMethodInfoList = getSubscribeMethodInfo(object);

        List<String> labels = UNREGISTER_HELPER.get(object.getClass());

        if (labels == null) {
            labels = new ArrayList<>();
            UNREGISTER_HELPER.put(object.getClass(), labels);
        }

        for (SubscribeMethodInfo subscribeMethodInfo : subscribeMethodInfoList) {

            String label = subscribeMethodInfo.getLabel();

            if (!labels.contains(label)) {
                labels.add(label);
            }

            List<SubscriptionInfo> subscriptionInfoList = SUBSCRIBES.get(label);
            if (subscriptionInfoList == null) {
                subscriptionInfoList = new ArrayList<>();
                SUBSCRIBES.put(label, subscriptionInfoList);
            }

            SubscriptionInfo subscriptionInfo = new SubscriptionInfo(object, subscribeMethodInfo);
            subscriptionInfoList.add(subscriptionInfo);

        }

    }

    /**
     * @date 创建时间 2018/4/26
     * @author Jiang zinc
     * @Description 获取订阅者的类中所有标记注解的方法信息
     * @version
     */
    private List<SubscribeMethodInfo> getSubscribeMethodInfo(Object object) {
        List<SubscribeMethodInfo> subscribeMethodInfos = CLASS_INFO_CACHE.get(object.getClass());

        // 检查是否有当前订阅者的类信息
        // 若无，则进行初始化
        if (subscribeMethodInfos == null) {
            subscribeMethodInfos = new ArrayList<>();
            CLASS_INFO_CACHE.put(object.getClass(), subscribeMethodInfos);

            // 获取所有方法
            Method[] declaredMethods = object.getClass().getDeclaredMethods();

            for (Method method : declaredMethods) {
                //获取Subscribe注解
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                //如果存在Subscribe注解则进行处理后缓存
                if (annotation != null) {
                    String[] values = annotation.value();
                    String threadMode = annotation.threadMode();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    method.setAccessible(true);
                    for (String value : values) {
                        SubscribeMethodInfo subscribeMethodInfo = new SubscribeMethodInfo(value, threadMode, method, parameterTypes);
                        subscribeMethodInfos.add(subscribeMethodInfo);
                    }
                }
            }

        }

        return subscribeMethodInfos;

    }

    /**
     * @param label  接收的标签
     * @param params 需要传输的参数
     * @date 创建时间 2018/4/26
     * @author Jiang zinc
     * @Description 发送消息
     * @version
     */
    public void post(String label, final Object... params) {

        List<SubscriptionInfo> subscriptionInfos = SUBSCRIBES.get(label);

        //如果没有该label的订阅类型，则直接放回
        if (subscriptionInfos == null) {
            return;
        }

        //遍历SUBSCRIBES订阅信息表
        for (final SubscriptionInfo subscriptionInfo : subscriptionInfos) {

            final SubscribeMethodInfo subscribeMethod = subscriptionInfo.getSubscribeMethod();

            Class[] parameterClasses = subscribeMethod.getParameterClasses();

            //若长度不同直接，则中断
            if (parameterClasses.length != params.length) {
                continue;
            }

            boolean flag = false;
            for (int i = 0; i < parameterClasses.length; ++i) {
                if (!parameterClasses[i].isInstance(params[i])) {   //是否有类型不同
                    flag = true;
                    break;
                }
            }

            if (flag) {
                continue;
            }

            String threadMode = subscriptionInfo.getSubscribeMethod().getThreadMode();
            if (threadMode.equals(ThreadMode.MAIN)) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callMethod(subscribeMethod, subscriptionInfo, params);
                    }
                });
            } else if (threadMode.equals(ThreadMode.POSTING)) {
                callMethod(subscribeMethod, subscriptionInfo, params);
            }
        }

    }

    private void callMethod(SubscribeMethodInfo subscribeMethod, SubscriptionInfo subscriptionInfo, Object... params) {
        try {
            subscribeMethod.getMethod().invoke(subscriptionInfo.getSubscribe(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @date 创建时间 2018/4/26
     * @author Jiang zinc
     * @Description 进行注销
     * @version
     */

    public void unregister(Object object) {

        List<String> labels = UNREGISTER_HELPER.get(object.getClass());

        //当labels不为空，说明需要进行对应的内存回收
        if (labels != null) {
            for (String label : labels) {
                List<SubscriptionInfo> subscriptionInfos = SUBSCRIBES.get(label);
                Iterator<SubscriptionInfo> iterator = subscriptionInfos.iterator();
                while (iterator.hasNext()) {
                    SubscriptionInfo subscriptionInfo = iterator.next();
                    //如果是同一个对象则进行回收
                    if (subscriptionInfo.getSubscribe() == object) {
                        iterator.remove();
                    }
                }
            }
        }

    }

    /**
     * @date 创建时间 2018/4/26
     * @author Jiang zinc
     * @Description 清空所有map
     * @version
     */
    public void clear() {
        CLASS_INFO_CACHE.clear();
        SUBSCRIBES.clear();
        UNREGISTER_HELPER.clear();
    }

}
