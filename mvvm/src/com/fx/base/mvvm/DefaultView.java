package com.fx.base.mvvm;

/**
 * Created by cmlanche on 2017/12/3.
 * 默认的view，用来取消掉mvvm模式，直接在一个view中处理所有事情
 */
public abstract class DefaultView extends BaseView<DefaultViewModel> {

    @Override
    protected void onViewCreated() {
        this.initView();
        this.init();
    }

    /**
     * 初始化数据
     */
    protected abstract void init();

    /**
     * 初始化界面
     */
    protected abstract void initView();
}
