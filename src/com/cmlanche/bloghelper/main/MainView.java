package com.cmlanche.bloghelper.main;

import com.fx.base.mvvm.DefaultView;
import javafx.fxml.FXML;

/**
 * Created by cmlanche on 2017/12/3.
 * 主view
 */
public class MainView extends DefaultView {

    @FXML
    SiderBarView siderBarView;
    @FXML
    ToolBarView toolBarView;
    @FXML
    ContentView contentView;

    public MainView() {
        loadAsRoot();
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        siderBarView.setItemSelectListener(bucket -> contentView.loadBucket(bucket));
    }
}
