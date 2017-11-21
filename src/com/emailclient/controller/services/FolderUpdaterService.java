package com.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class FolderUpdaterService extends Service<Void> {

    /**
     * This class checks for any new message dynamically :
     */

    private List<Folder> foldersList;

    public FolderUpdaterService(List<Folder> foldersList) {
        this.foldersList = foldersList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                for (; ; ) {

                    try {
                        Thread.sleep(10000);
                        if (FetchFoldersService.noServicesActive()) {

                            System.out.println("{?} Checking for new emails.");
                            for (Folder folder : foldersList) {

                                if (folder.getType() != Folder.HOLDS_MESSAGES && folder.isOpen()) {

                                    folder.getMessageCount();
                                }
                            }
                        }


                    } catch (Exception exception) {

                        System.out.println("{X} Exception while updating message list : " + getClass().toString());
                        exception.printStackTrace();
                    }
                }
            }
        };
    }
}
