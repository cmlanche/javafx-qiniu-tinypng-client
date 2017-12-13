package com.fx.base.dialog;

import com.fx.base.mvvm.BaseView;
import com.fx.mvvm.ViewTuple;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by cmlanche on 16/12/1.
 * 对话框基类
 */
public abstract class BaseDialog{

    Stage mStage;
    Scene mScene;
    Pane root;
    ViewTuple<?, ?> viewTuple;

    private double movex;
    private double movey;
    private boolean isDragable;
    private static final boolean DEFAULT_DRAGABLE = true;
    private CloseDialogListener closeListener;
    private Window owner;

    public BaseDialog(Window owner, ViewTuple<?, ?> viewTuple) {
        this(owner, viewTuple, DEFAULT_DRAGABLE);
    }

    public BaseDialog(Window owner, ViewTuple<?, ?> viewTuple, boolean isDragable) {
        this.owner = owner;
        this.viewTuple = viewTuple;
        this.isDragable = isDragable;
        this.safeCreate();
    }

    public BaseDialog(Window owner, boolean isDragable) {
        this(owner, null, isDragable);
    }

    public BaseDialog(Window owner) {
        this(owner, DEFAULT_DRAGABLE);
    }

    public BaseDialog() {
        this(null);
    }

    private void safeCreate() {
        if (Platform.isFxApplicationThread()) {
            create();
        } else {
            Platform.runLater(this::create);
        }
    }

    private BaseDialog create() {
        mStage = new Stage();
        mStage.initModality(Modality.WINDOW_MODAL);
        mStage.initOwner(getOwner());
        mStage.initStyle(StageStyle.DECORATED);
        mStage.setResizable(false);
        viewTuple = createContent();
        BaseView view = null;
        if(viewTuple.getView() instanceof BaseView){
            view = (BaseView) viewTuple.getView();
            view.onDialogSet(this);
            root = (Pane) viewTuple.getRoot();
            alignCenter(root.getPrefWidth(), root.getPrefHeight());
            if(isDragable)
                dragDialogAbleNode(root);
        } else {
            try {
                throw new Exception("your view is not a BaseView!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mScene = new Scene(viewTuple.getRoot());
//        mScene.getStylesheets().setAll(TestinStage.getInstance().getStylesheet());
        mStage.setScene(mScene);
        mStage.setTitle(view.getTitle());
        return this;
    }

    public BaseDialog setCloseListener(CloseDialogListener listener) {
        this.closeListener = listener;
        return this;
    }

    /**
     * 创建主体内容
     * @return
     */
    protected ViewTuple<?, ?> createContent() {
        return viewTuple;
    }

    protected Window getOwner() {
        return owner;
    }

    public void show(){
        if(Platform.isFxApplicationThread()){
            mStage.show();
        }else {
            Platform.runLater(() -> mStage.show());
        }
    }

    public void close(){
        if(Platform.isFxApplicationThread()){
            mStage.close();
        }else {
            Platform.runLater(() -> mStage.close());
        }
    }

    public void hide(){
        if(Platform.isFxApplicationThread()){
            mStage.hide();
        }else {
            Platform.runLater(() -> mStage.hide());
        }
    }

    /**
     * 对话框关闭时的回调
     *
     * @param flag
     * @param data
     */
    public void onClosed(int flag, Object data) {
        if (closeListener != null) {
            closeListener.close(flag, data);
        }
    }

    /**
     * 使窗口居中
     * @param width
     * @param height
     */
    private void alignCenter(double width, double height){
//        mStage.setX(TestinStage.getInstance().getPrimaryStage().getX() + (TestinStage.getInstance().getPrimaryStage().getWidth() - width) / 2);
//        mStage.setY(TestinStage.getInstance().getPrimaryStage().getY() + (TestinStage.getInstance().getPrimaryStage().getHeight() - height) / 2);
    }

    /**
     * 使某个节点具有拖动窗口的能力
     * @param node
     */
    public void dragDialogAbleNode(Node node){
        node.setOnMouseDragged(event -> {
            mStage.setX(event.getScreenX() - movex);
            mStage.setY(event.getScreenY() - movey);
        });
        node.setOnMousePressed(event -> {
            movex = event.getSceneX();
            movey = event.getSceneY();
        });

    }

    public Stage getStage() {
        return mStage;
    }

    public void setStage(Stage mStage) {
        this.mStage = mStage;
    }

    public Scene getScene() {
        return mScene;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }
}
