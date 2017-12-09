package com.cmlanche.bloghelper.ui.rename;

import com.fx.base.mvvm.DefaultView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by cmlanche on 2017/12/9.
 * 重命名界面
 */
public class RenameView extends DefaultView {

    @FXML
    TextField textField;

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {

    }

    @FXML
    public void onSure(ActionEvent event) {
        String name = textField.getText();
        if (StringUtils.isEmpty(name)) {
            return;
        }
        ok(name.trim());
    }

    @FXML
    public void onCancel(ActionEvent event) {
        cancel();
    }
}
