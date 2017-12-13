package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.App;
import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.downloader.DownloadListener;
import com.cmlanche.bloghelper.downloader.FileDownloader;
import com.cmlanche.bloghelper.listeners.ItemSelectListener;
import com.cmlanche.bloghelper.listeners.UpdateListener;
import com.cmlanche.bloghelper.listeners.UploadListener;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.model.DownloadProcessData;
import com.cmlanche.bloghelper.model.ProcessData;
import com.cmlanche.bloghelper.model.TinnyCompressProcessData;
import com.cmlanche.bloghelper.model.UploadProcessData;
import com.cmlanche.bloghelper.qiniu.QiniuManager;
import com.cmlanche.bloghelper.tinypng.CompressListener;
import com.cmlanche.bloghelper.tinypng.TinypngManger;
import com.cmlanche.bloghelper.ui.alert.AlertDialog;
import com.cmlanche.bloghelper.ui.optimize.PngOptimzeView;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class ContentView extends CustomView {

    private String bucket;
    private ItemSelectListener<BucketFile> selectListener;
    private UpdateListener<BucketFile> updateListener;

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
    @FXML
    TableColumn<BucketFile, String> whColumn;
    @FXML
    TableColumn<BucketFile, ProcessData> processColumn;

    private ContextMenu contextMenu;
    private MenuItem downloadMenuItem;
    private MenuItem renameMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem optimzeMenuItem;
    private MenuItem uploadMenuItem;

    private ExecutorService loadBucketPool;
    private Future loadBucketFuture;

    @Override
    protected void onViewCreated() {
        initView();
        init();
    }

    public void init() {
        bucket = Config.getInstance().getLastestBucket();
        loadBucket(bucket);
    }

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
                getStyleClass().clear();
                if (!empty) {
                    BucketFile bucketFile = tableView.getItems().get(getIndex());
                    int status = BucketUtils.getBucketFileStauts(bucketFile);
                    getStyleClass().add(UIUtils.getStatusBackground(status));
                } else {
                    setGraphic(null);
                }
            }
        });

        whColumn.setCellValueFactory(new PropertyValueFactory<>("whSize"));
        processColumn.setCellValueFactory(new PropertyValueFactory<>("process"));
        processColumn.setCellFactory(param -> new TableCell<BucketFile, ProcessData>() {
            @Override
            protected void updateItem(ProcessData item, boolean empty) {
                if (!empty) {
                    if (item != null) {
                        if (item.getState() == ProcessData.IDLE) {
                            setText("");
                        } else {
                            // 分析下载状态
                            if (item instanceof DownloadProcessData) {
                                DownloadProcessData data = (DownloadProcessData) item;
                                if (data.getState() == ProcessData.ERROR) {
                                    setText("下载出错：" + data.getError());
                                } else if (data.getState() == ProcessData.WAITING) {
                                    setText("等待下载...");
                                } else if (data.getState() == ProcessData.PROCESSING) {
                                    setText(String.format("下载中...%d%%", data.getProgress()));
                                } else if (data.getState() == ProcessData.FINISH) {
                                    setText("下载完成");
                                }
                            } else if (item instanceof TinnyCompressProcessData) {
                                TinnyCompressProcessData data = (TinnyCompressProcessData) item;
                                if (data.getState() == ProcessData.ERROR) {
                                    setText("下载出错：" + data.getError());
                                } else if (data.getState() == ProcessData.PROCESSING) {
                                    setText("Tinny压缩中...");
                                } else if (data.getState() == ProcessData.FINISH) {
                                    setText("Tinny压缩完成！");
                                }
                            } else if (item instanceof UploadProcessData) {
                                UploadProcessData data = (UploadProcessData) item;
                                if (data.getState() == ProcessData.ERROR) {
                                    setText("上传出错：" + data.getError());
                                } else if (data.getState() == ProcessData.PROCESSING) {
                                    setText("上传中...");
                                } else if (data.getState() == ProcessData.FINISH) {
                                    setText("上传完成！");
                                }
                            }
                        }
                    }
                } else {
                    setText("");
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

    public void setUpdateListener(UpdateListener<BucketFile> updateListener) {
        this.updateListener = updateListener;
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
                bf.setUpdateTime(item.putTime / 10000); // 七牛云的文件时间单位为100纳秒
                bf.setUrl("http://" + domains[0] + "/" + item.key);
                bf.setHash(item.hash);
                // 计算图像的宽高大小，当文件已经被下载了才去计算
                bf.setWhSize(BucketUtils.getPictureSize(bf));
                bf.setProcess(new ProcessData(ProcessData.IDLE));
                tableView.getItems().add(bf);
            }
        }
    }

    public void loadBucket(String bucket) {
        if (loadBucketFuture != null && !loadBucketFuture.isDone()) {
            loadBucketFuture.cancel(true);
        }
        if (loadBucketPool == null) {
            loadBucketPool = Executors.newSingleThreadExecutor();
        }
        loadBucketFuture = loadBucketPool.submit(() -> {
            if (StringUtils.isNotEmpty(bucket)) {
                this.bucket = bucket;
                FileListing fileListing = QiniuManager.getInstance().getFiles(bucket, 1000);
                runOnUiThread(() -> this.loadFileListing(fileListing));
            }
        });
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
                download(bucketFile);
                break;
            case "rename":
                rename(bucketFile);
                break;
            case "delete":
                delete(bucketFile);
                break;
            case "optimize":
                optimize(bucketFile);
                break;
            case "upload":
                upload(bucketFile);
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
        RenameDialog.show(bucketFile, (flat, data) -> {
            if (flat == CloseFlag.OK) {
                String newName = (String) data;
                if (QiniuManager.getInstance().rename(bucketFile, newName)) {
                    Logger.info(tag, "rename success:" + bucketFile.getName() + " -> " + newName);
                    // 如果该文件以及被下载的话，则需要更新本地的名称
                    BucketUtils.renameLocalFile(bucketFile, newName);
                    BucketUtils.renameOptimizeFile(bucketFile, newName);
                    bucketFile.setName(newName);
                    tableView.refresh();
                }
            }
        });
    }

    /**
     * 删除一个文件
     *
     * @param bucketFile
     */
    private void delete(BucketFile bucketFile) {
        AlertDialog.show("提示", String.format("确定要删除文件'%s'吗？", bucketFile.getName()), (flat, data) -> {
            if (flat == CloseFlag.OK) {
                if (QiniuManager.getInstance().delete(bucketFile)) {
                    tableView.getItems().remove(bucketFile);
                }
            }
        });
    }

    /**
     * 下载文件
     *
     * @param bucketFile
     */
    private void download(BucketFile bucketFile) {
        FileDownloader.getInstance().download(bucketFile, new DownloadListener() {
            @Override
            public void onWating() {
                bucketFile.setProcess(new DownloadProcessData(ProcessData.WAITING));
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {
                DownloadProcessData processData = new DownloadProcessData(ProcessData.PROCESSING);
                processData.setProgress(progress);
                bucketFile.setProcess(processData);
                tableView.refresh();
            }

            @Override
            public void onFinished(String path) {
                bucketFile.setProcess(new DownloadProcessData(ProcessData.FINISH));
                tableView.refresh();
                if (updateListener != null) {
                    updateListener.onUpdate(bucketFile);
                }
            }

            @Override
            public void onError(String error) {
                DownloadProcessData data = new DownloadProcessData(ProcessData.ERROR);
                data.setError(error);
                bucketFile.setProcess(data);
            }
        });
    }

    /**
     * 优化一个文件
     *
     * @param bucketFile
     */
    private void optimize(BucketFile bucketFile) {
        if (UIUtils.isPng(bucketFile.getMineType()) || UIUtils.isJpg(bucketFile.getMineType())) {
            PngOptimzeView view = new PngOptimzeView(bucketFile);
            showDialog(App.getMainStage(), view, (flag, data) -> {
                if (flag == CloseFlag.OK) {
                    TinypngManger.getInstance().compress(bucketFile, new CompressListener() {
                        @Override
                        public void prepare() {
                            bucketFile.setProcess(new TinnyCompressProcessData(ProcessData.WAITING));
                            tableView.refresh();
                        }

                        @Override
                        public void compressing() {
                            bucketFile.setProcess(new TinnyCompressProcessData(ProcessData.PROCESSING));
                            tableView.refresh();
                        }

                        @Override
                        public void finish() {
                            bucketFile.setProcess(new TinnyCompressProcessData(ProcessData.FINISH));
                            tableView.refresh();
                            if (updateListener != null) {
                                updateListener.onUpdate(bucketFile);
                            }
                        }

                        @Override
                        public void error(String message) {
                            TinnyCompressProcessData data = new TinnyCompressProcessData(ProcessData.ERROR);
                            data.setError(message);
                            bucketFile.setProcess(data);
                            tableView.refresh();
                        }
                    });
                }
            });
        }
    }

    /**
     * 上传一个已经被优化的文件
     *
     * @param bucketFile
     */
    private void upload(BucketFile bucketFile) {
        if (bucketFile == null) return;
        QiniuManager.getInstance().upload(bucketFile, new UploadListener() {
            @Override
            public void prepare() {
                bucketFile.setProcess(new UploadProcessData(ProcessData.WAITING));
                tableView.refresh();
            }

            @Override
            public void uploading() {
                bucketFile.setProcess(new UploadProcessData(ProcessData.PROCESSING));
                tableView.refresh();
            }

            @Override
            public void finish() {
                bucketFile.setProcess(new UploadProcessData(ProcessData.FINISH));
                tableView.refresh();
                if (updateListener != null) {
                    updateListener.onUpdate(bucketFile);
                }
            }

            @Override
            public void error(String message) {
                UploadProcessData data = new UploadProcessData(ProcessData.ERROR);
                data.setError(message);
                bucketFile.setProcess(data);
                tableView.refresh();
            }
        });
    }
}
