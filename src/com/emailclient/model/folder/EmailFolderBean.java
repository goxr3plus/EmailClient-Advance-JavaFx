package com.emailclient.model.folder;

import com.emailclient.model.EmailMessageBean;
import com.emailclient.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailFolderBean<T> extends TreeItem<String> {

    private boolean topElement = false;
    private int unreadMessageCount;
    private String name;
    private String completeName;
    private ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();

    /**
     * Constructor for top elements.
     *
     * @param value
     */

    public EmailFolderBean(String value) {

        super(value, ViewFactory.defaultFactory.resolveIcon(value));
        this.name = value;
        this.completeName = value;
        data = null;
        topElement = true;
        this.setExpanded(true);
    }

    public EmailFolderBean(String value, String completeName) {

        super(value, ViewFactory.defaultFactory.resolveIcon(value));
        this.name = value;
        this.completeName = completeName;
    }

    private void updateValue() {

        if (unreadMessageCount > 0) {

            this.setValue((String) (name + " (" + unreadMessageCount + ")"));
        } else {

            this.setValue(name);
        }

    }

    public void incrementUnreadMessageCount(int newMessages) {

        unreadMessageCount += newMessages;
        updateValue();
    }

    public void decrementUnreadMessageCount(int newMessages) {

        unreadMessageCount--;
        updateValue();
    }

    // New method with better implementation :

//    public void addEmail(EmailMessageBean message) {
//
//        data.add(message);
//        if (!message.isRead()) {
//            incrementUnreadMessageCount(1);
//        }
//    }

    public void addEmail(int position, Message message) throws MessagingException {
        boolean isRead = message.getFlags().contains(Flags.Flag.SEEN);

        EmailMessageBean emailMessageBean = new EmailMessageBean(message.getSubject(),
                message.getFrom()[0].toString(),
                message.getSize()
                , isRead,
                message.getSentDate(),
                message);

        if (position < 0) {

            data.add(emailMessageBean);
        } else {

            data.add(position, emailMessageBean);
        }

        if (!isRead) {
            incrementUnreadMessageCount(1);
        }

    }


    public boolean isTopElement() {
        return topElement;
    }

    public void setTopElement(boolean topElement) {
        this.topElement = topElement;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public ObservableList<EmailMessageBean> getData() {
        return data;
    }

    public void setData(ObservableList<EmailMessageBean> data) {
        this.data = data;
    }
}
