package com.cmlanche.bloghelper;

import com.cmlanche.bloghelper.ui.MainView;
import com.fx.mvvm.MvvmFX;
import javafx.application.Application;
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

        Scene scene = new Scene(new MainView(), 1000, 618);
        primaryStage.setScene(scene);
        primaryStage.setTitle(resourceBundle.getString("app.title"));
        primaryStage.show();
    }
}
