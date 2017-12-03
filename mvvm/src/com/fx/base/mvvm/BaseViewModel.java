package com.fx.base.mvvm;


import com.fx.mvvm.ViewModel;
import com.fx.mvvm.utils.notifications.NotificationObserver;

/**
 * Created by cmlanche on 16/11/30.
 *
 * 业务逻辑（基类）
 */
public abstract class BaseViewModel implements ViewModel {

    public BaseViewModel(){
        this.initProperties();
    }

    /**
     * 这个虚方法在界面构建前自动执行，这里请放置Property初始化的相关工作（与界面无关的也可）
     */
    protected abstract void initProperties();

    /**
     * 这个虚方法不是自动执行的，需要手动调用，我们假定的需求就是在界面初始化（属性绑定等）完成后做一些其他的业务逻辑
     */
    protected abstract void init();

    /**
     * 订阅全局消息
     * @param messageName 消息名称标识
     * @param payload 消息接收者
     */
    protected void subscribeToPublic(String messageName, NotificationObserver payload){
        subscribe(true, messageName, payload);
    }

    /**
     * 发送全局消息
     * @param messageName 消息名称标识
     * @param payload 发布消息的内容，是Object数组
     */
    protected void publishToPublic(String messageName, Object... payload) {
        publish(true, messageName, payload);
    }

    /**
     * 订阅私有消息
     * @param messageName 消息名称标识
     * @param payload 消息接收者
     */
    protected void subscribe(String messageName, NotificationObserver payload){
        subscribe(false, messageName, payload);
    }

    /**
     * 发布私有消息
     * @param messageName  消息名称标识
     * @param payload 发布消息的内容，是Object数组
     */
    protected void publish(String messageName, Object... payload){
        publish(false, messageName, payload);
    }
}
