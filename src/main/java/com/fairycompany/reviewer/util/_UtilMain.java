package com.fairycompany.reviewer.util;

import com.fairycompany.reviewer.exception.ServiceException;

import java.util.UUID;

public class _UtilMain {
    private static final String LETTER = """
                Greeting %s. Thanks for registering with GameReview!            
                    Click here to complete registration %s.
                    Cheers,
                        your pals at GR!
                Please do not reply to this email. Your message will not be received.
                """;
    private static final String REGISTRATION_SUBJECT_EMAIL = "Registration";
    private static final String REGISTER_LINK = "http://localhost:8080/gamereview/controller?command=register&user_id=%d&register_code=%s";

    public static void main(String[] args) throws ServiceException {

        // password
//        String password = "zxc123asd";
//        String hashPassword = HashGenerator.hashPassword(password);
//        System.out.println(hashPassword);
        String link = String.format(REGISTER_LINK, 1, UUID.randomUUID());
        String letter = String.format(LETTER, "Olga", link);
        EmailSender emailSender = new EmailSender("sonofviktor@yandex.ru", REGISTRATION_SUBJECT_EMAIL, letter);
        System.out.println(emailSender.sendEmail());

    }
}
