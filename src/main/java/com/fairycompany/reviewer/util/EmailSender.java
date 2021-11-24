package com.fairycompany.reviewer.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class  EmailSender {
    private static final Logger logger = LogManager.getLogger();
    private static final String PROPERTY_FILE = "properties\\mail.properties";
    private static final String USER_NAME_KEY = "mail.user.name";
    private static final String USER_PASSWORD_KEY = "mail.user.password";
    private final Properties properties = new Properties();
    private String sendToEmail;
    private String mailSubject;
    private String mailText;

    public EmailSender(String sendToEmail, String mailSubject, String mailText) {
        this.sendToEmail = sendToEmail;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
    }

    public boolean sendEmail() {
        boolean result = false;

        try {
            readEmailProperty(properties);
            Session mailSession = createSession(properties);
            MimeMessage message = initMessage(mailSession);
            Transport.send(message);
            logger.log(Level.INFO, "Email was sent to {}", sendToEmail);
            result = true;
        } catch (MessagingException e) {
            logger.log(Level.ERROR, "Generating of sending message failed. {}", e.getMessage());
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error when reading {}. {}", PROPERTY_FILE, e.getMessage());
        }

        return result;
    }

    private void readEmailProperty(Properties properties) throws IOException {
        try (InputStream propertyReader = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            if (propertyReader == null) {
                logger.log(Level.ERROR, "File {} is not found", PROPERTY_FILE);
                throw new IOException();
            }

            properties.load(propertyReader);
        }

        logger.log(Level.DEBUG, "Properties was read successfully");
    }

    private Session createSession(Properties properties) {
        String userName = properties.getProperty(USER_NAME_KEY);
        String userPassword = properties.getProperty(USER_PASSWORD_KEY);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, userPassword);
            }
        });

        logger.log(Level.DEBUG, "Session was created");

        return session;
    }

    private MimeMessage initMessage(Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setSubject(mailSubject);
        message.setContent(mailText, "text/plain");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));

        logger.log(Level.DEBUG, "Message was generated");

        return message;
    }
}
