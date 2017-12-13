package com.fx.base.mvvm;

import com.fx.base.dialog.BaseDialog;
import com.fx.base.dialog.CloseDialogListener;
import javafx.stage.Stage;

/**
 * Created by cmlanche on 2017/12/3.
 * 默认的view，用来取消掉mvvm模式，直接在一个view中处理所有事情
 */
public abstract class DefaultView extends BaseView<DefaultViewModel> {


    /**
     * 显示一个对话框
     *
     * @param stage
     * @param view
     * @param listener
     */
    public void showDialog(Stage stage, DefaultView view, CloseDialogListener listener) {
        new BaseDialog(stage, view.load()) {
        }.setCloseListener(listener).show();
    }

    @Override
    public String getTitle() {
        return "untitled";
    }

}
