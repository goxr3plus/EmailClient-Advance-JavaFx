package com.emailclient.model;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class EmailAccountBean {

    /**
     * This bean class is the main javax.mail api implementation.
     */

    private String emailAddress;
    private String password;
    private Properties properties;

    private Store store;
    private Session session;
    private int loginState = EmailConstants.LOGIN_STATE_NOT_READY;

    public String getEmailAddress() {
        return emailAddress;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

    public int getLoginState() {
        return loginState;
    }

    public EmailAccountBean(String EmailAddress, String Password) {
        this.emailAddress = EmailAddress;
        this.password = Password;

        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("outgoingHost", "smtp.gmail.com");


        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailAddress, password);
            }
        };

        // Connecting:
        session = Session.getInstance(properties, auth);
        try {
            this.store = session.getStore();
            store.connect(properties.getProperty("incomingHost"), emailAddress, password);
            System.out.println("{*} EmailAccountBean constructed successfully.");
            loginState = EmailConstants.LOGIN_STATE_SUCCEDED;
        } catch (Exception e) {
            e.printStackTrace();
            loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
        }

    }

    public String getPassword() {
        return password;
    }

    // Method to test the api on console :

//    public void addEmailsToData(ObservableList<EmailMessageBean> data){
//        try {
//            Folder folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_ONLY);
//
//            for(int i = folder.getMessageCount(); i > 0; i--){
//
//                Message message = folder.getMessage(i); // This is giving the exception.
//                EmailMessageBean messageBean = new EmailMessageBean(message.getSubject(),
//                        message.getFrom()[0].toString(),
//                        message.getSize(),
//                        "",
//                        message.getFlags().contains(Flag.SEEN));
//
//                System.out.println("Got: " + messageBean);
//                data.add(messageBean);
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }
}
