package com.emailclient.controller.services;

import com.emailclient.model.EmailMessageBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javax.mail.internet.MimeBodyPart;

public class SaveAttachmentService extends Service<Void> {

    /**
     * This class contains the framework which is used to save the attachments.
     *
     * @return
     */

    private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/"; //  Path for saving attachments.
    private EmailMessageBean messageToDownload;
    private ProgressBar progress; // Progress bar.
    private Label label;


    public SaveAttachmentService(ProgressBar progress, Label label) {
        // showVisuals(false);
        this.progress = progress;
        this.label = label;

        this.setOnRunning(
                (event) -> showVisuals(true));

        this.setOnSucceeded(
                (event) -> showVisuals(false));
    }


    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {


                try {
                    for (MimeBodyPart mbp : messageToDownload.getAttachmentsList()) {

                        updateProgress(messageToDownload.getAttachmentsList().indexOf(mbp),
                                messageToDownload.getAttachmentsList().size());
                        mbp.saveFile(LOCATION_OF_DOWNLOADS + mbp.getFileName()); // The IO is internally handled by the javax.mail Api :)
                    }
                } catch (Exception exception) {

                    System.out.println("{X} Exception in while saving the attachment : " + getClass().getSimpleName());
                    exception.printStackTrace();
                }

                return null;
            }
        };
    }

    private void showVisuals(boolean show) {
        progress.setVisible(show);
        label.setVisible(show);
    }


    // Always call before starting.
    public void setMessageToDownload(EmailMessageBean messageToDownload) {

        this.messageToDownload = messageToDownload;
    }
}
