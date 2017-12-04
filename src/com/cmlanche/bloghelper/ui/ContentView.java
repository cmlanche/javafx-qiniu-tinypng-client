package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.listeners.ItemSelectListener;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.qiniu.ResManager;
import com.fx.base.mvvm.DefaultView;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class ContentView extends DefaultView {

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
    TableColumn<BucketFile, String> operationColumn;

    public ContentView() {
        loadAsRoot();
    }

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
        updateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("updateTime"));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (this.selectListener != null) {
                    this.selectListener.onItemSelected(newValue);
                }
            }
        });
    }

    public void setOnItemSelectedListener(ItemSelectListener<BucketFile> listener) {
        this.selectListener = listener;
    }

    private void loadFileListing(FileListing fileListing) {
        tableView.getItems().clear();
        if (fileListing != null && fileListing.items.length > 0) {
            String[] domains = ResManager.getInstance().getDomains(bucket);
            for (FileInfo item : fileListing.items) {
                BucketFile bf = new BucketFile();
                bf.setName(item.key);
                bf.setMineType(item.mimeType);
                bf.setSize(String.valueOf(item.fsize));
                bf.setUpdateTime(String.valueOf(item.putTime));
                bf.setUrl("http://" + domains[1] + "/" + item.key);
                tableView.getItems().add(bf);
            }
        }
    }

    public void loadBucket(String bucket) {
        if (StringUtils.isNotEmpty(bucket)) {
            this.bucket = bucket;
            FileListing fileListing = ResManager.getInstance().getFiles(bucket, 1000);
            loadFileListing(fileListing);
        }
    }
}
