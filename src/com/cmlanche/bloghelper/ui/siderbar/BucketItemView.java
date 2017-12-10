package com.cmlanche.bloghelper.ui.siderbar;

import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by cmlanche on 2017/12/10.
 * <p>
 * bucket view for sider listview item
 */
public class BucketItemView extends CustomView {

    @FXML
    Label nameLabel;

    @Override
    protected void onViewCreated() {

    }

    public void setBucketName(String name) {
        this.nameLabel.setText(name);
    }
}
