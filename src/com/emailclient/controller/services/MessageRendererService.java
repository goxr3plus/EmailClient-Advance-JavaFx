package com.emailclient.controller.services;

import com.emailclient.model.EmailMessageBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

public class MessageRendererService extends Service<Void> {

    /**
     * This class is used for rendering the web view of the email using a web engine.
     */

    private EmailMessageBean messageToRender;
    private WebEngine messageRendererEngine;
    private StringBuffer sb = new StringBuffer(); // For thread safety.


    public MessageRendererService(WebEngine messageRendererEngine) {

        this.messageRendererEngine = messageRendererEngine;
    }

    public void setMessageToRender(EmailMessageBean messageToRender) {

        this.messageToRender = messageToRender;

        this.setOnSucceeded(
                (event) -> showMessage()
        );
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                renderMessage(); // Moved to run method due to exception.

                return null;
            }
        };
    }

    // While testing with Runnable interface : BAD_RESULT :( freezing still occurs.
//    @Override
//    public void run() {
//        renderMessage();
//    }

    // Method to render the message : This method is pure genius, this was not implemented by me, this was taught by the instructor.
    private void renderMessage() {

        sb.setLength(0); // Clears the string buffer.
        messageToRender.clearAttachments(); // Clears the attachments.
        Message message = messageToRender.getMessageReference();

        try {

            String messageType = message.getContentType();

            if (messageType.contains("TEXT/HTML") ||
                    messageType.contains("TEXT/PLAIN") ||
                    messageType.contains("text")) {

                sb.append(message.getContent().toString());
            } else if (messageType.contains("multipart")) {

                Multipart mp = (Multipart) message.getContent();

                for (int i = mp.getCount() - 1; i >= 0; i--) {

                    BodyPart bp = mp.getBodyPart(i);
                    String contentType = bp.getContentType();

                    if (contentType.contains("TEXT/HTML") ||
                            contentType.contains("TEXT/PLAIN") ||
                            contentType.contains("mixed") ||
                            contentType.contains("text")) {

                        if (sb.length() == 0) {

                            sb.append(bp.getContent().toString());
                        }

                        // TO DO. {for attachments}
                    } else if (contentType.toLowerCase().contains("application") ||
                            contentType.toLowerCase().contains("image") ||
                            contentType.toLowerCase().contains("audio")) {

                        // Attachment Handling :
                        MimeBodyPart mbp = (MimeBodyPart) bp;
                        messageToRender.addAttachment(mbp);

                        //  Sometimes the message is encapsulated in another multipart.
                        //  So we have to iterate again through it.
                    } else if (bp.getContentType().contains("multipart")) {

                        Multipart mp2 = (Multipart) bp.getContent();

                        for (int j = mp2.getCount() - 1; j >= 0; j--) {

                            BodyPart bp2 = mp2.getBodyPart(i);

                            if (bp2.getContentType().contains("TEXT/HTML") ||
                                    bp2.getContentType().contains("TEXT/PLAIN")) {

                                sb.append(bp2.getContent().toString());

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("{X} Exception while rendering the message.In class : " + getClass().getSimpleName());
        }
    }

    /**
     * Only called once the message is loaded.
     * Handle the information about attachment.
     */
    private void showMessage() {

        messageRendererEngine.loadContent(sb.toString());
    }
}
