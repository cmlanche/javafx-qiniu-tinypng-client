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

    public BaseDialog(boolean isDragable){
        this.isDragable = isDragable;
        if(Platform.isFxApplicationThread()){
            create();
        }else {
            Platform.runLater(() -> create());
        }
    }

    public BaseDialog(){
        this(DEFAULT_DRAGABLE);
    }

    private void create(){
        mStage = new Stage();
        mStage.initModality(Modality.APPLICATION_MODAL);
        mStage.initOwner(null);
        mStage.initStyle(StageStyle.TRANSPARENT);
        mStage.setResizable(false);
        viewTuple = createContent();
        if(viewTuple.getView() instanceof BaseView){
            BaseView view = (BaseView) viewTuple.getView();
            view.onDialogSet(this);
            root = (Pane) viewTuple.getRoot();
            alignCenter(root.getPrefWidth(), root.getPrefHeight());
            if(isDragable)
                dragDialogAbleNode(root);
        }else{
            try {
                throw new Exception("your view is not a BaseView!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mScene = new Scene(viewTuple.getRoot());
//        mScene.getStylesheets().setAll(TestinStage.getInstance().getStylesheet());
        mStage.setScene(mScene);
    }

    /**
     * 创建主体内容
     * @return
     */
    protected abstract ViewTuple<?, ?> createContent();

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
