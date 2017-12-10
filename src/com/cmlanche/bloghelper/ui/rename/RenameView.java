package com.cmlanche.bloghelper.ui.rename;

import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.ui.common.CommonDialogLayoutView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by cmlanche on 2017/12/9.
 * 重命名界面
 */
public class RenameView extends CommonDialogLayoutView {

    @FXML
    TextField textField;

    private BucketFile bucketFile;

    public RenameView(BucketFile bucketFile) {
        this.bucketFile = bucketFile;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        textField.setText(bucketFile.getName());
        setTitle("重命名");
    }


    @Override
    protected void onOk() {
        String name = textField.getText();
        if (StringUtils.isEmpty(name)) {
            return;
        }
        ok(name.trim());
    }

    @Override
    protected void onCancel() {
        cancel();
    }
}
