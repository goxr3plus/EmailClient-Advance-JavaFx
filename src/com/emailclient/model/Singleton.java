package com.emailclient.model;

public class Singleton {
/*
Not used anymore
Functionality shifted to ModelAccess.java class
 */
    private Singleton() {};
    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {

        return instance;
    }

    private EmailMessageBean message;

    public EmailMessageBean getMessage() {
        return message;
    }

    public void setMessage(EmailMessageBean message) {
        this.message = message;
    }
}
