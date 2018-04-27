# JFragmentBus
主要解决fragment间定点传值和通讯（同样适用于activity）

# 如何集成
1、在项目的gradle中增加如下代码
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2、在library中添加
```
dependencies {
	compile 'com.github.zincPower:JFragmentbus:0.1'
}
```

# 如何使用
1、在需要被调用的方法加上注解Subscribe

##### 参数说明：
**value**：接收标记label，可以设置多个接收标志label

**threadMode**：线程模式，目前支持ThreadMode.MAIN和ThreadMode.POSTING两种。默认为ThreadMode.POSTING，即和调用的线程同个线程。

```
@Subscribe(value = {"MyLabel"},threadMode = ThreadMode.MAIN)
public void onMessageReceive() {}
```
2、在onCreate进行注册
```
JFragmentBus.getDefault().register(this);
```
3、在onDestroty进行注销
```
JFragmentBus.getDefault().unregister(this);
```
4、发送信息

注意：在发送的时候，发送的参数需和被调用的方法的参数类型和个数一致，否则即使label一样，也不会触发。
```
JFragmentBus.getDefault().post("MyLabel", ...params);
```
