package com.cmlanche.bloghelper.main;

import com.cmlanche.bloghelper.model.Bucket;
import com.fx.base.mvvm.DefaultView;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


/**
 * Created by cmlanche on 2017/12/3.
 * <p>
 * 导航栏
 */
public class SiderBarView extends DefaultView {

    @FXML
    ListView<Bucket> listview;

    public SiderBarView() {
        loadAsRoot();
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }
}
