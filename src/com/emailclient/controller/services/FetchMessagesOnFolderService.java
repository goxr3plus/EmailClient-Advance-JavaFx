package com.emailclient.controller.services;

import com.emailclient.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;

public class FetchMessagesOnFolderService extends Service<Void> {

    private EmailFolderBean<String> emailFolder;
    private Folder folder;

    public FetchMessagesOnFolderService(EmailFolderBean<String> emailFolder, Folder folder) {
        this.emailFolder = emailFolder;
        this.folder = folder;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (folder.getType() != Folder.HOLDS_FOLDERS) {

                    folder.open(Folder.READ_ONLY);
                }
                int folderSize = folder.getMessageCount();
                for (int i = folderSize; i > 0; i++) {
                    Message currentMessage = folder.getMessage(i);

                    // Modified position to -1 due to implementation of message listener.
                    emailFolder.addEmail(-1, currentMessage);
                }
                return null;
            }
        };
    }
}
