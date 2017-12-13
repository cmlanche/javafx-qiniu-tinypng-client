package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.listeners.ItemSelectListener;
import com.cmlanche.bloghelper.qiniu.QiniuManager;
import com.cmlanche.bloghelper.ui.siderbar.BucketItemView;
import com.fx.base.mvvm.CustomView;
import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by cmlanche on 2017/12/3.
 * <p>
 * 导航栏
 */
public class SiderBarView extends CustomView {

    @FXML
    ListView<String> listview;

    private StringProperty test;

    public final String getTest() {
        return testProperty().get();
    }

    public final StringProperty testProperty() {
        if (test == null) {
            test = new SimpleStringProperty();
        }
        return test;
    }

    public final void setTest(String test) {
        this.testProperty().set(test);
    }

    private ItemSelectListener<String> itemSelectListener;

    public SiderBarView(@NamedArg("test") String test) {
        super();
        testProperty().set(test);
    }

    @Override
    protected void onViewCreated() {
        initView();
        init();
    }

    void init() {
        String[] buckets = QiniuManager.getInstance().getBuckets();
        listview.getItems().addAll(buckets);
    }

    void initView() {
        listview.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    BucketItemView view = new BucketItemView();
                    view.setBucketName(item);
                    setGraphic(view);
                } else {
                    setGraphic(null);
                }
            }
        });
        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isNotEmpty(newValue)) {
                if (this.itemSelectListener != null) {
                    this.itemSelectListener.onItemSelected(newValue);
                }
            }
        });
    }

    public void setOnItemSelectedListener(ItemSelectListener<String> itemSelectListener) {
        this.itemSelectListener = itemSelectListener;
    }
}
