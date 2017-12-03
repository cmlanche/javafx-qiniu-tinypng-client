package com.cmlanche.bloghelper;

import com.fx.mvvm.MvvmFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n");
        MvvmFX.setGlobalResourceBundle(resourceBundle);

        Parent parent = FXMLLoader.load(getClass().getResource("/com/cmlanche/bloghelper/main/MainView.fxml"));
        Scene scene = new Scene(parent, 1000, 618);
        primaryStage.setScene(scene);
        primaryStage.setTitle(resourceBundle.getString("app.title"));
        primaryStage.show();
    }
}
