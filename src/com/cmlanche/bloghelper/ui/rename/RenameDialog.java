package com.cmlanche.bloghelper.ui.rename;

import com.cmlanche.bloghelper.Main;
import com.cmlanche.bloghelper.model.BucketFile;
import com.fx.base.dialog.BaseDialog;
import com.fx.base.dialog.CloseDialogListener;
import com.fx.mvvm.ViewTuple;
import javafx.stage.Window;

/**
 * Created by cmlanche on 2017/12/9.
 */
public class RenameDialog extends BaseDialog {

    public RenameDialog(ViewTuple<?, ?> viewTuple) {
        super(Main.getMainStage(), viewTuple, false);
    }

    @Override
    protected Window getOwner() {
        return Main.getMainStage();
    }

    /**
     * 显示对话框
     *
     * @param bucketFile
     * @param listener
     */
    public static void show(BucketFile bucketFile, CloseDialogListener listener) {
        RenameView view = new RenameView(bucketFile);
        RenameDialog dialog = new RenameDialog(view.load());
        dialog.setCloseListener(listener);
        dialog.show();
    }
}
