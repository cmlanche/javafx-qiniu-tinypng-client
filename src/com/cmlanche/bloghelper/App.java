package com.cmlanche.bloghelper;

import com.cmlanche.bloghelper.ui.MainView;
import com.cmlanche.bloghelper.utils.UIUtils;
import com.fx.mvvm.MvvmFX;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class App extends Application {

    private static Stage mainStage;
    private static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        hostServices = getHostServices();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n");
        MvvmFX.setGlobalResourceBundle(resourceBundle);

        Scene scene = new Scene(new MainView(), 1000, 618);
        scene.getStylesheets().add(getClass().getResource(UIUtils.getGlobalStylesheet()).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(resourceBundle.getString("app.title"));
        primaryStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static HostServices getHostService() {
        return hostServices;
    }
}
