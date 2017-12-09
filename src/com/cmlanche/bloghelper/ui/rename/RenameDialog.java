package com.cmlanche.bloghelper.ui.rename;

import com.fx.base.dialog.BaseDialog;
import com.fx.base.dialog.CloseDialogListener;
import com.fx.mvvm.ViewLoader;
import com.fx.mvvm.ViewTuple;

/**
 * Created by cmlanche on 2017/12/9.
 */
public class RenameDialog extends BaseDialog {

    @Override
    protected ViewTuple<?, ?> createContent() {
        return ViewLoader.load(RenameView.class);
    }

    /**
     * 显示对话框
     *
     * @param listener
     */
    public static void show(CloseDialogListener listener) {
        RenameDialog dialog = new RenameDialog();
        dialog.setCloseListener(listener);
        dialog.show();
    }
}
