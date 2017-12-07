package com.cmlanche.bloghelper.ui;

import com.fx.base.mvvm.CustomView;
import com.sun.javafx.PlatformUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * Created by cmlanche on 2017/12/3.
 * 主view
 */
public class MainView extends CustomView {

    @FXML
    SiderBarView siderBarView;
    @FXML
    MenuBar menuBar;
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

        menuBar.getMenus().add(new Menu("hello"));

        if (PlatformUtil.isMac()) {
            menuBar.setUseSystemMenuBar(true);
        }
    }
}
