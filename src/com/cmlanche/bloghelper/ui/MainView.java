package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.App;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.fx.base.mvvm.CustomView;
import com.sun.javafx.PlatformUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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


    public MainView() {
        super();
    }

    @Override
    protected void onViewCreated() {
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
        contentView.setUpdateListener(bucketFile -> {
            if (Platform.isFxApplicationThread()) {
                preView.loadFile(bucketFile);
            } else {
                runOnUiThread(() -> preView.loadFile(bucketFile));
            }
        });

        // 初始化菜单
        Menu setting = new Menu("设置");
        setting.getItems().add(getMenuItem("TinyPNG", "tinypng"));
        setting.getItems().add(getMenuItem("七牛", "qiniu"));
        setting.getItems().add(getMenuItem("Gif", "gif"));

        Menu about = new Menu("关于");
        about.getItems().add(getMenuItem("关于BlogHelper", "aboutBlogHelper"));
        about.getItems().add(getMenuItem("关于作者cmlanche", "aboutAnchor"));

        menuBar.getMenus().add(setting);
        menuBar.getMenus().add(about);

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

    /**
     * 创建菜单项
     *
     * @param name
     * @param id
     * @return
     */
    private MenuItem getMenuItem(String name, String id) {
        MenuItem item = new MenuItem(name);
        item.setId(id);
        item.setOnAction(new MenuHandler());
        return item;
    }

    private class MenuHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            MenuItem menuItem = (MenuItem) event.getSource();
            String id = menuItem.getId();
            switch (id) {
                case "tinypng":
                    break;
                case "qiniu":
                    break;
                case "gif":
                    break;
                case "aboutBlogHelper":
                    App.getHostService().showDocument("http://cmlanche.com/2017/12/03/%E5%9B%BE%E7%89%87%E5%8E%8B%E7%BC%A9(tinypng)+%E4%B8%83%E7%89%9B%E4%BA%91%E5%AD%98%E5%82%A8%E5%AE%A2%E6%88%B7%E7%AB%AF/");
                    break;
                case "aboutAnchor":
                    App.getHostService().showDocument("http://www.cmlanche.com");
                    break;
            }
        }
    }
}
