package com.emailclient.controller;

import com.emailclient.controller.services.EmailSenderService;
import com.emailclient.model.EmailConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends AbstractController implements Initializable {

    /*
    Controller class for Compose message functionality.
     */

    private List<File> attachments = new ArrayList<>();

    @FXML
    private Label attachmentLabel;

    @FXML
    private ChoiceBox<String> senderChoice;

    @FXML
    private TextField recipientField;

    @FXML
    private TextField subjectField;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor composeArea;

    @FXML
    void attachBtnAction() {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null); // Open dialogue box to choose attachments.

        if (selectedFile != null) {

            attachments.add(selectedFile);
            attachmentLabel.setText(attachmentLabel.getText() + selectedFile.getName() + " ; ");

        }
    }

    @FXML
    void sendBtnAction() {

        errorLabel.setText(""); // Clears the error label.

        EmailSenderService emailSenderService = new EmailSenderService(getModelAccess().getEmailAccountByName(senderChoice.getValue()),
                subjectField.getText(),
                recipientField.getText(),
                composeArea.getHtmlText(),
                attachments
        );
        emailSenderService.restart();
        emailSenderService.setOnSucceeded((event) -> {

            if (emailSenderService.getValue() != EmailConstants.MESSAGE_SENT_OK) {
                errorLabel.setText("[*] Message sent successfully");
            } else {

                errorLabel.setText("[X] Error while sending email");
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        senderChoice.setItems(getModelAccess().getEmailAccountsNames());
        senderChoice.setValue(getModelAccess().getEmailAccountsNames().get(0));
    }

    public ComposeMessageController(ModelAccess modelAccess) {
        super(modelAccess);
    }
}
