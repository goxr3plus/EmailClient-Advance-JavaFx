package com.emailclient.view;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SetStageIcon {

    /**
     * Class to set the desired icon for stage.
     */


    public static SetStageIcon getInstance = new SetStageIcon();
    private static final String DefaultIcon_512x512 = "icon/DefaultIcon_512x512.png";

    public void setDefaultIcon(Stage stage) {

        stage.getIcons().add(new Image(getClass().getResourceAsStream(DefaultIcon_512x512)));
    }
}
