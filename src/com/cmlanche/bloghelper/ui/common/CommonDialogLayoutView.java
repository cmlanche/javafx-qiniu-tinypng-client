package com.cmlanche.bloghelper.ui.common;

import com.cmlanche.bloghelper.common.Logger;
import com.fx.base.mvvm.DefaultView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Created by cmlanche on 2017/12/10.
 */
public abstract class CommonDialogLayoutView extends DefaultView {

    @FXML
    private StackPane root;

    @Override
    protected void onViewCreated() {
        if (root != null) {
            Node okBtn = root.lookup("#ok");
            if (okBtn != null) {
                okBtn.setOnMouseClicked(event -> this.onOk());
            }

            Node cancelBtn = root.lookup("#cancel");
            if (cancelBtn != null) {
                cancelBtn.setOnMouseClicked(event -> this.onCancel());
            }
        } else {
            Logger.error(this.getClass().getName(), getClass().getName() + " do not set the root to your fxml!");
        }
    }

    protected abstract void onOk();

    protected abstract void onCancel();
}
