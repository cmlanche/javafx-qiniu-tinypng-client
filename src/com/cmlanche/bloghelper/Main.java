package com.cmlanche.bloghelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/com/cmlanche/bloghelper/main/MainView.fxml"));
        Scene scene = new Scene(parent, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
