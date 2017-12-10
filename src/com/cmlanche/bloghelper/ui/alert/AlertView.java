package com.cmlanche.bloghelper.ui.alert;

import com.cmlanche.bloghelper.ui.common.CommonDialogLayoutView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by cmlanche on 2017/12/10.
 */
public class AlertView extends CommonDialogLayoutView {

    private String title;
    private String content;

    @FXML
    Label contentLabel;

    public AlertView(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        this.setTitle(title);
        this.contentLabel.setText(content);
    }

    @Override
    protected void onOk() {
        ok(null);
    }

    @Override
    protected void onCancel() {
        cancel();
    }
}
