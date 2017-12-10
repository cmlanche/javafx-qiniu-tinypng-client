package com.fx.base.mvvm;

import com.fx.base.dialog.BaseDialog;
import com.fx.base.dialog.CloseFlag;
import com.fx.mvvm.FxmlView;
import com.fx.mvvm.ViewLoader;
import com.fx.mvvm.ViewModel;
import com.fx.mvvm.ViewTuple;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by cmlanche on 16/11/30.
 *
 * 界面的逻辑（基类）
 */
public abstract class BaseView<VM extends ViewModel> extends StackPane implements Initializable, FxmlView<VM> {

    protected URL location;
    protected ResourceBundle resources;

    protected BaseDialog mDialog;

    private int closeFlag;
    private Object closeData;

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
        if (this.mDialog != null) {
            this.mDialog.getStage().showingProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    // dialog closed
                    mDialog.onClosed(closeFlag, closeData);
                }
            });
        }
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

    public void closeDialog(int flag) {
        closeDialog(flag, null);
    }

    public void closeDialog(int flag, Object data) {
        setCloseData(flag, data);
        closeDialog();
    }

    public void cancel() {
        closeDialog(CloseFlag.CANCEL);
    }

    public void ok(Object closeData) {
        closeDialog(CloseFlag.OK, closeData);
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

    public void setCloseFlag(int closeFlag) {
        this.closeFlag = closeFlag;
    }

    public void setCloseData(Object data) {
        this.closeData = data;
    }

    /**
     * 设置关闭的返回数据
     *
     * @param closeFlag
     * @param data
     */
    public void setCloseData(int closeFlag, Object data) {
        setCloseFlag(closeFlag);
        setCloseData(data);
    }

    /**
     * 把自己也作为一个视图节点Pane来加载(适合需要先创建自己，给自己传参的场景)
     */
    @SuppressWarnings("unchecked")
    public ViewTuple loadAsRoot(){
        return ViewLoader.fxmlView(this.getClass()).codeBehind(this).root(this).load();
    }

    /**
     * 复用自己作为Controller，fxml中的根节点与自己无关
     * 自己作为Controller，但不作为根节点时调用
     */
    @SuppressWarnings("unchecked")
    public ViewTuple<?, ?> load() {
        return ViewLoader.fxmlView(this.getClass()).codeBehind(this).load();
    }
}