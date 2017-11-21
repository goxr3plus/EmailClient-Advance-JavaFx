package com.emailclient.controller;

import com.emailclient.view.SetStageIcon;
import com.emailclient.view.ViewFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /*
       Main class for javaFx, Check Controller classes in controller folder for the main methods as this was made
       based on MVC architecture pattern.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewFactory viewFactory = ViewFactory.defaultFactory;

        Scene scene = viewFactory.getMainScene();
        primaryStage.setOpacity(0.955f); // Scene transparency.
        primaryStage.setTitle("Email Client [prototype]");

        SetStageIcon.getInstance.setDefaultIcon(primaryStage); // Setting the icon.

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
