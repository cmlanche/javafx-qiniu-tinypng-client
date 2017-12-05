package com.cmlanche.bloghelper.ui;

import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;

/**
 * Created by cmlanche on 2017/12/3.
 * 主view
 */
public class MainView extends CustomView {

    @FXML
    SiderBarView siderBarView;
    @FXML
    ToolBarView toolBarView;
    @FXML
    ContentView contentView;
    @FXML
    PreView preView;

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        siderBarView.setOnItemSelectedListener(contentView::loadBucket);
        contentView.setOnItemSelectedListener(preView::loadFile);
    }
}
