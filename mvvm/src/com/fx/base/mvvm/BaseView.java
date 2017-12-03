package com.fx.base.mvvm;

import com.fx.base.dialog.BaseDialog;
import com.fx.mvvm.FxmlView;
import com.fx.mvvm.ViewLoader;
import com.fx.mvvm.ViewModel;
import com.fx.mvvm.ViewTuple;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by cmlanche on 16/11/30.
 *
 * 界面的逻辑（基类）
 */
public abstract class BaseView<VM extends ViewModel> extends Pane implements Initializable, FxmlView<VM> {

    protected URL location;
    protected ResourceBundle resources;

    protected BaseDialog mDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;

        this.onViewCreated();
    }

    /**
     * 视图已创建好
     */
    protected abstract void onViewCreated();

    /**
     * 承载视图的对话框对象
     * 在onViewCreated之后执行（不需要继承）
     * @param dialog
     */
    public void onDialogSet(BaseDialog dialog){
        this.mDialog = dialog;
    }

    /**
     * 要在fx的ui线程中运行，请用此方法
     * @param runnable
     */
    protected void runOnUiThread(Runnable runnable){
        Platform.runLater(runnable);
    }

    public void closeDialog(){
        if(mDialog != null){
            mDialog.close();
        }
    }

    /**
     * 窗口是否打开，fx中close=hide
     * @return
     */
    public boolean isDialogShowing(){
        if(mDialog != null){
            return mDialog.getStage().isShowing();
        }
        return false;
    }

    /**
     * 把自己也作为一个视图节点Pane来加载(适合需要先创建自己，给自己传参的场景)
     */
    public ViewTuple loadAsRoot(){
        return ViewLoader.fxmlView(this.getClass()).codeBehind(this).root(this).load();
    }
}