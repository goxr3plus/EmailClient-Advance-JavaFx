package com.emailclient.controller;

import com.emailclient.model.EmailAccountBean;
import com.emailclient.model.EmailMessageBean;
import com.emailclient.model.folder.EmailFolderBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAccess {

    /*
    This class is a new implementation for Singleton.java class following the MVC architecture pattern.
     */

    private EmailFolderBean<String> selectedFolder;
    private EmailMessageBean selectedMessage;


    // Changes done for  ComposeMessageController.java class for sending emails.
    private Map<String, EmailAccountBean> emailAccounts = new HashMap<>();
    private ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();

    public ObservableList<String> getEmailAccountsNames() {

        return emailAccountsNames;
    }

    public EmailAccountBean getEmailAccountByName(String name) {

        return emailAccounts.get(name);
    }

    public void addAccount(EmailAccountBean account) {

        emailAccounts.put(account.getEmailAddress(), account);
        emailAccountsNames.add(account.getEmailAddress());
    }


    public EmailMessageBean getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessageBean selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailFolderBean<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private List<Folder> foldersList = new ArrayList<>();

    public List<Folder> getFoldersList() {
        return foldersList;
    }

    public void addFolder(Folder folder) {

        foldersList.add(folder);
    }
}
