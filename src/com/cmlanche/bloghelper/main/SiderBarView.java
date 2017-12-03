package com.cmlanche.bloghelper.main;

import com.cmlanche.bloghelper.qiniu.ResManager;
import com.fx.base.mvvm.DefaultView;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by cmlanche on 2017/12/3.
 * <p>
 * 导航栏
 */
public class SiderBarView extends DefaultView {

    interface ItemSelectListener {
        void onSelected(String bucket);
    }

    @FXML
    ListView<String> listview;

    private ItemSelectListener itemSelectListener;

    public SiderBarView() {
        loadAsRoot();
    }

    @Override
    protected void init() {
        String[] buckets = ResManager.getInstance().getBuckets();
        listview.getItems().addAll(buckets);
    }

    @Override
    protected void initView() {
//        listview.setCellFactory(param -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty) {
//                    setText(item);
//                } else {
//                    setText("");
//                }
//            }
//        });
        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isNotEmpty(newValue)) {
                if (this.itemSelectListener != null) {
                    this.itemSelectListener.onSelected(newValue);
                }
            }
        });
    }

    public void setItemSelectListener(ItemSelectListener itemSelectListener) {
        this.itemSelectListener = itemSelectListener;
    }
}
