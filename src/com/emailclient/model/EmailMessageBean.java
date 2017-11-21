package com.emailclient.model;

import com.emailclient.model.table.AbstractTableItem;
import com.emailclient.model.table.FormatableInteger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.*;

public class EmailMessageBean extends AbstractTableItem {

    /*
      Java Beans class for Emails.
     */

   // public static Map<String, Integer> formattedValues = new HashMap<>();


    private SimpleStringProperty sender;
    private SimpleStringProperty subject;
    private SimpleObjectProperty<FormatableInteger> size;
   // private String content;
    private Message messageReference;
    private SimpleObjectProperty<Date> date;


    // Attachment Handling :

    private List<MimeBodyPart> attachmentsList = new ArrayList<>();
    private StringBuffer attachmentsNames = new StringBuffer();

    public EmailMessageBean(String subject, String sender, int size, boolean isRead,Date date, Message messageReference ) {

        super(isRead);
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.size = new SimpleObjectProperty<>(new FormatableInteger(size));
     // this.content = content;
        this.date = new SimpleObjectProperty<>(date);
        this.messageReference = messageReference;
    }


    // For size conversion : // Functionality shifted to FormatableInteger.java class.
//    private String formatSize(int size) {
//
//        String returnValue;
//
//        if (size <= 0) {
//            returnValue = "0";
//        } else if (size < 1024) {
//            returnValue = size + " B";
//        } else if (size < 1048576) {
//            returnValue = size / 1024 + " kB";
//        } else {
//            returnValue = size / 1048576 + " mB";
//        }
//        formattedValues.put(returnValue, size);
//        return returnValue;
//    }


    // For attachments handling :

    public List<MimeBodyPart> getAttachmentsList() {
        return attachmentsList;
    }

    public String getAttachmentsNames() {
        return attachmentsNames.toString();
    }

    public void addAttachment(MimeBodyPart mbp) {

        attachmentsList.add(mbp);

        try {
            attachmentsNames.append(mbp.getFileName() + ";");
        } catch (MessagingException me) {

            System.out.println("{X} Messaging exception caused while reading attachments. Class : " + getClass().getSimpleName());
            me.printStackTrace();
        }
    }

public boolean hasAttachments() {

        return attachmentsList.size() > 0;
}
// Clear method for attachments :
public void clearAttachments() {

        attachmentsList.clear();
        attachmentsNames.setLength(0);
}

    public String getSender() {
        return sender.get();
    }

    public String getSubject() { return subject.get(); }

    public FormatableInteger getSize() {
        return size.get();
    }

   // public String getContent() { return content; }

    public Date getDate() {
        return date.get();
    }

    public Message getMessageReference() {
        return messageReference;
    }

    @Override
    public String toString() {

        return "EmailMessageBean{" +
                "sender=" + sender.get() +
                ", subject=" + subject.get() +
                ", size=" + size.get();
    }
}
