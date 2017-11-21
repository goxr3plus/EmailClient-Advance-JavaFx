package com.emailclient.controller;

import com.emailclient.controller.services.MessageRendererService;
import com.emailclient.model.EmailMessageBean;
import com.emailclient.model.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends AbstractController implements Initializable {

/*
  Controller class for details option.
 */

    public EmailDetailsController(ModelAccess modelAccess) {

        super(modelAccess);
    }

    private Singleton singleton = Singleton.getInstance(); // Not used anymore, Functionality shifted to AbstractController.java and ModelAccess.java classes.

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    MessageRendererService rendererService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EmailMessageBean selectedMessage = getModelAccess().getSelectedMessage();

        System.out.println("[>] EmailDetailsController initialized.");

        /*
        Use of singleton was shifted to ModelAccess.java class.
         */

        // Passing the subject and sender through singleton.
        subjectLabel.setText("Subject : " + selectedMessage.getSubject());
        senderLabel.setText("Sender : " + selectedMessage.getSender());

        // Starting the WebView Engine.

        // Debug this shit.
        rendererService = new MessageRendererService(webView.getEngine());
        rendererService.setMessageToRender(selectedMessage);
        rendererService.restart();
    }
}
