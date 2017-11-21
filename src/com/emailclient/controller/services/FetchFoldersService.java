package com.emailclient.controller.services;

import com.emailclient.controller.ModelAccess;
import com.emailclient.model.EmailAccountBean;
import com.emailclient.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

public class FetchFoldersService extends Service<Void> {

    private EmailFolderBean<String> foldersRoot;
    private EmailAccountBean emailAccount;
    private ModelAccess modelAccess;
    private static int NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE = 0;


    public FetchFoldersService(EmailFolderBean<String> foldersRoot, EmailAccountBean emailAccount, ModelAccess modelAccess) {
        this.foldersRoot = foldersRoot;
        this.emailAccount = emailAccount;
        this.modelAccess = modelAccess;

        this.setOnSucceeded((event) -> {

            NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE--;
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE++;

                if (emailAccount != null) {

                    Folder[] folders = emailAccount.getStore().getDefaultFolder().list();

                    for (Folder folder : folders) {

                        modelAccess.addFolder(folder);
                        EmailFolderBean<String> item = new EmailFolderBean<>(folder.getName(), folder.getFullName());
                        foldersRoot.getChildren().add(item);

                        item.setExpanded(true);

                        addMessageListenerToFolder(folder, item);

                        FetchMessagesOnFolderService fetchMessagesOnFolderService = new FetchMessagesOnFolderService(item, folder);
                        fetchMessagesOnFolderService.start();

                        System.out.println("{*} Added : " + folder.getName());

                        Folder[] subFolders = folder.list();
                        for (Folder subFolder : subFolders) {

                            modelAccess.addFolder(subFolder);
                            EmailFolderBean<String> subItem = new EmailFolderBean<>(subFolder.getName(), subFolder.getFullName());
                            item.getChildren().add(subItem);

                            addMessageListenerToFolder(subFolder, subItem);

                            FetchMessagesOnFolderService fetchMessagesOnSubFolderService = new FetchMessagesOnFolderService(subItem, subFolder);
                            fetchMessagesOnSubFolderService.start();

                            System.out.println("{*} Added : " + subFolder.getName());
                        }


                    }
                }
                return null;
            }
        };
    }

    // To receive messages dynamically :
    private void addMessageListenerToFolder(Folder folder, EmailFolderBean<String> item) {

        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for (int i = 0; i < e.getMessages().length; i++) {

                    try {

                        Message currentMessage = folder.getMessage(folder.getMessageCount() - i);
                        item.addEmail(0, currentMessage);
                    } catch (MessagingException me) {

                        System.out.println("{*} Exception while getting new messages : " + getClass().toString());
                        me.printStackTrace();
                    }
                }
            }
        });
    }

    // Returns the number of services currently active :
    public static boolean noServicesActive() {

        return NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE == 0;
    }
}
