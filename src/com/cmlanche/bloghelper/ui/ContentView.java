package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.listeners.ItemSelectListener;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.qiniu.QiniuManager;
import com.cmlanche.bloghelper.ui.rename.RenameDialog;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.cmlanche.bloghelper.utils.UIUtils;
import com.cmlanche.bloghelper.utils.Utils;
import com.fx.base.dialog.CloseFlag;
import com.fx.base.mvvm.CustomView;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class ContentView extends CustomView {

    private String bucket;
    private ItemSelectListener<BucketFile> selectListener;

    @FXML
    TableView<BucketFile> tableView;
    @FXML
    TableColumn<BucketFile, Integer> indexColumn;
    @FXML
    TableColumn<BucketFile, String> nameColumn;
    @FXML
    TableColumn<BucketFile, String> mineTypeColumn;
    @FXML
    TableColumn<BucketFile, Long> sizeColumn;
    @FXML
    TableColumn<BucketFile, Long> updateTimeColumn;
    @FXML
    TableColumn<BucketFile, String> statusColumn;

    private ContextMenu contextMenu;
    private MenuItem downloadMenuItem;
    private MenuItem renameMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem optimzeMenuItem;
    private MenuItem uploadMenuItem;

    @Override
    public void init() {
        bucket = Config.getInstance().getLastestBucket();
        loadBucket(bucket);
    }

    @Override
    public void initView() {
        indexColumn.setCellFactory(param -> new TableCell<BucketFile, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(String.valueOf(this.getIndex() + 1));
                } else {
                    setText("");
                }
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mineTypeColumn.setCellValueFactory(new PropertyValueFactory<>("mineType"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeColumn.setCellFactory(param -> new TableCell<BucketFile, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                if (!empty) {
                    setText(Utils.getSizeName(item));
                } else {
                    setText("");
                }
            }
        });
        updateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("updateTime"));
        updateTimeColumn.setCellFactory(param -> new TableCell<BucketFile, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                if (!empty) {
                    setText(Utils.getDate(item));
                } else {
                    setText("");
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (this.selectListener != null) {
                    this.selectListener.onItemSelected(newValue);
                }
            }
        });

        statusColumn.setCellFactory(param -> new TableCell<BucketFile, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                if (!empty) {
                    BucketFile bucketFile = tableView.getItems().get(getIndex());
                    int status = BucketUtils.getBucketFileStauts(bucketFile);
                    getStyleClass().add(UIUtils.getStatusBackground(status));
                } else {
                    setGraphic(null);
                }
            }
        });

        this.initContextMenu();
    }

    /**
     * 初始化菜单
     */
    private void initContextMenu() {
        contextMenu = new ContextMenu();
        contextMenu.getItems().add(downloadMenuItem = getMenuItem("下载", "download"));
        contextMenu.getItems().add(renameMenuItem = getMenuItem("重命名", "rename"));
        contextMenu.getItems().add(deleteMenuItem = getMenuItem("删除", "delete"));
        contextMenu.getItems().add(optimzeMenuItem = getMenuItem("优化", "optimize"));
        contextMenu.getItems().add(uploadMenuItem = getMenuItem("上传", "upload"));
        contextMenu.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                BucketFile bucketFile = tableView.getSelectionModel().getSelectedItem();
                if (bucketFile != null) {
                    int status = BucketUtils.getBucketFileStauts(bucketFile);
                    switch (status) {
                        case BucketUtils.NORMAL:
                            downloadMenuItem.setDisable(false);
                            uploadMenuItem.setDisable(true);
                            optimzeMenuItem.setDisable(true);
                            break;
                        case BucketUtils.DOWNLOADED:
                            downloadMenuItem.setDisable(true);
                            uploadMenuItem.setDisable(true);
                            optimzeMenuItem.setDisable(false);
                            break;
                        case BucketUtils.OPTIMIZEED:
                            downloadMenuItem.setDisable(true);
                            uploadMenuItem.setDisable(false);
                            optimzeMenuItem.setDisable(false);
                            break;
                        case BucketUtils.OPTIMIZED_UPLOADED:
                            downloadMenuItem.setDisable(true);
                            uploadMenuItem.setDisable(true);
                            optimzeMenuItem.setDisable(false);
                            break;
                    }
                }
            }
        });
        tableView.setContextMenu(contextMenu);
    }

    private MenuItem getMenuItem(String name, String id) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setId(id);
        menuItem.setOnAction(event -> {
            MenuItem item = (MenuItem) event.getSource();
            BucketFile bucketFile = tableView.getSelectionModel().getSelectedItem();
            if (bucketFile != null) {
                handleAction(item.getId(), bucketFile);
            }
        });
        return menuItem;
    }

    public void setOnItemSelectedListener(ItemSelectListener<BucketFile> listener) {
        this.selectListener = listener;
    }

    private void loadFileListing(FileListing fileListing) {
        tableView.getItems().clear();
        if (fileListing != null && fileListing.items.length > 0) {
            String[] domains = QiniuManager.getInstance().getDomains(bucket);
            for (FileInfo item : fileListing.items) {
                BucketFile bf = new BucketFile();
                bf.setName(item.key);
                bf.setBucket(bucket);
                bf.setMineType(item.mimeType);
                bf.setSize(item.fsize);
                bf.setUpdateTime(item.putTime / 10000);
                bf.setUrl("http://" + domains[1] + "/" + item.key);
                bf.setHash(item.hash);
                tableView.getItems().add(bf);
            }
        }
    }

    public void loadBucket(String bucket) {
        if (StringUtils.isNotEmpty(bucket)) {
            this.bucket = bucket;
            FileListing fileListing = QiniuManager.getInstance().getFiles(bucket, 1000);
            loadFileListing(fileListing);
        }
    }

    /**
     * 处理菜单事件
     *
     * @param bucketFile
     * @param action
     */
    private void handleAction(String action, BucketFile bucketFile) {
        switch (action) {
            case "download":
                break;
            case "rename":
                rename(bucketFile);
                break;
            case "delete":
                break;
            case "optimize":
                break;
            case "upload":
                break;
        }
    }

    public void handleAction(String action) {
        BucketFile bucketFile = tableView.getSelectionModel().getSelectedItem();
        if (bucketFile != null) {
            handleAction(action, bucketFile);
        }
    }

    /**
     * 重命名一个bucketfile
     *
     * @param bucketFile
     */
    private void rename(BucketFile bucketFile) {
        RenameDialog.show((flat, data) -> {
            if (flat == CloseFlag.OK) {
                String newName = (String) data;
                QiniuManager.getInstance().rename(bucketFile, newName);
            }
        });
    }
}
