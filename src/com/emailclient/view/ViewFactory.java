package com.emailclient.view;

import com.emailclient.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.naming.OperationNotSupportedException;

public class ViewFactory {

    /*
    Class referencing the controller classes in controller package.
    An alternative to Singleton.java class.
    This class was also the idea of the instructor, pretty cool class :) does a lot of work.
     */

    public static ViewFactory defaultFactory = new ViewFactory();
    private static boolean mainViewInitialized = false;

    private final String COMPOSEMESSAGELAYOUT_FXML_PATH = "ComposeMessageLayout.fxml";
    private final String EMAILDETAILSLAYOUT_FXML_PATH = "EmailDetailsLayout.fxml";
    private final String MAINLAYOUT_FXML_PATH = "MainLayout.fxml";
    private final String DEAFAULT_CSS = "style.css";

    private ModelAccess modelAccess = new ModelAccess();

    // Controller classes.
    private MainController mainController;
    private EmailDetailsController emailDetailsController;

    // A generalized method to initialize scenes rather than individual method for every scene.
    private Scene initializeScene(String fxmlPath, AbstractController abstractController) {

        FXMLLoader loader;
        Parent parent;
        Scene scene;

        try {

            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(abstractController);
            parent = loader.load();
        } catch (Exception e) {
            System.out.println("[X] Exception in ViewFactory.java class." +
                    "\n" +
                    "Cannot load *.fxml");
            //e.printStackTrace();
            return null;
        }
        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource(DEAFAULT_CSS).toExternalForm());

        return scene;
    }

    public Scene getMainScene() throws OperationNotSupportedException {

        /*
        Functionality shifted towards usage of Abstract in the new code.
         */

//        Parent pane;
//
//        try {
//            pane = FXMLLoader
//                    .load(getClass()
//                            .getResource("MainLayout.fxml"));
//        } catch (IOException ioe) {
//            pane = null;
//            ioe.printStackTrace();
//            System.out.println("MainLayout.fxml cannot be loaded.");
//        }
//        Scene mainScene = new Scene(pane, 700, 500);
//        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//        return mainScene;


        // New code :
        if (!mainViewInitialized) {
            mainController = new MainController(modelAccess);
            mainViewInitialized = true;
            return initializeScene(MAINLAYOUT_FXML_PATH, mainController);
        } else {
            throw new OperationNotSupportedException("{>} Main Scene is already initialized.");
        }

    }

    public Scene getEmailDetailsScene() {

        /*
         Functionality shifted towards usage of Abstract in the new code.
          */

//        Parent pane;
//
//        try {
//            pane = FXMLLoader
//                    .load(getClass()
//                            .getResource("EmailDetailsLayout.fxml"));
//        } catch (IOException ioe) {
//            pane = null;
//            ioe.printStackTrace();
//            System.out.println("{>} EmailDetailsLayout.fxml cannot be loaded.");
//        }
//        Scene detailScene = new Scene(pane, 600, 400);
//        detailScene.getStylesheets().add(getClass().getResource("detailStyle.css").toExternalForm());
//        return detailScene;

        // New code :
        emailDetailsController = new EmailDetailsController(modelAccess);
        return initializeScene(EMAILDETAILSLAYOUT_FXML_PATH, emailDetailsController);
    }

    public Scene getComposeMessageScene() {

        AbstractController composeMessageController = new ComposeMessageController(modelAccess);
        return initializeScene(COMPOSEMESSAGELAYOUT_FXML_PATH, composeMessageController);
    }


    // Method for resolving icons.
    public Node resolveIcon(String treeItemValue) {

        String lowerCaseTreeValue = treeItemValue.toLowerCase();

        ImageView returnIcon;

        try {
            if (lowerCaseTreeValue.contains("inbox")) {

                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/inbox.png")));
            } else if (lowerCaseTreeValue.contains("sent")) {

                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/sent2.png")));
            } else if (lowerCaseTreeValue.contains("spam")) {

                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/spam.png")));
            } else if (lowerCaseTreeValue.contains("@")) {

                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/email.png")));
            } else {

                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/folder.png")));
            }
        } catch (NullPointerException npe) {

            System.out.println("{X} Invalid Image Location.");
            npe.printStackTrace();
            returnIcon = new ImageView();
        }

        // For 48x48 to 16x16 *.png conversion.
        returnIcon.setFitWidth(16);
        returnIcon.setFitHeight(16);

        return returnIcon;
    }
}
