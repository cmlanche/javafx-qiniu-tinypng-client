package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.utils.BucketUtils;
import com.fx.base.mvvm.CustomView;
import com.sun.javafx.PlatformUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;

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
    @FXML
    ToolBar toolbar;
    @FXML
    Button downloadBtn;
    @FXML
    Button renameBtn;
    @FXML
    Button deleteBtn;
    @FXML
    Button optimizeBtn;
    @FXML
    Button uploadBtn;

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        siderBarView.setOnItemSelectedListener(contentView::loadBucket);
        contentView.setOnItemSelectedListener(bucketFile -> {
            // 设置操作状态
            int state = BucketUtils.getBucketFileStauts(bucketFile);
            switch (state) {
                case BucketUtils.NORMAL:
                    downloadBtn.setDisable(false);
                    uploadBtn.setDisable(true);
                    optimizeBtn.setDisable(true);
                    break;
                case BucketUtils.DOWNLOADED:
                    downloadBtn.setDisable(true);
                    uploadBtn.setDisable(true);
                    optimizeBtn.setDisable(false);
                    break;
                case BucketUtils.OPTIMIZEED:
                    downloadBtn.setDisable(true);
                    uploadBtn.setDisable(false);
                    optimizeBtn.setDisable(false);
                    break;
                case BucketUtils.OPTIMIZED_UPLOADED:
                    downloadBtn.setDisable(true);
                    uploadBtn.setDisable(true);
                    optimizeBtn.setDisable(false);
                    break;
            }
            // 加载预览图
            preView.loadFile(bucketFile);
        });

        // 初始化菜单
        menuBar.getMenus().add(new Menu("hello"));
        if (PlatformUtil.isMac()) {
            menuBar.setUseSystemMenuBar(true);
        }

        // 操作按钮
        downloadBtn.setOnAction(event -> contentView.handleAction("download"));
        renameBtn.setOnAction(event -> contentView.handleAction("rename"));
        deleteBtn.setOnAction(event -> contentView.handleAction("delete"));
        optimizeBtn.setOnAction(event -> contentView.handleAction("optimize"));
        uploadBtn.setOnAction(event -> contentView.handleAction("upload"));
    }
}
