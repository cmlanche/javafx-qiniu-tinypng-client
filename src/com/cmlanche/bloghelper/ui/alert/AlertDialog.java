package com.cmlanche.bloghelper.ui.alert;

import com.cmlanche.bloghelper.App;
import com.fx.base.dialog.BaseDialog;
import com.fx.base.dialog.CloseDialogListener;
import com.fx.mvvm.ViewTuple;
import javafx.stage.Window;

/**
 * Created by cmlanche on 2017/12/10.
 */
public class AlertDialog extends BaseDialog {

    public AlertDialog(Window owner, ViewTuple<?, ?> viewTuple) {
        super(owner, viewTuple, false);
    }

    public static void show(String title, String content, CloseDialogListener listener) {
        AlertView view = new AlertView(title, content);
        new AlertDialog(App.getMainStage(), view.load()).setCloseListener(listener).show();
    }
}
