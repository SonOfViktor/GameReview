package com.fairycompany.reviewer.util;

import com.fairycompany.reviewer.exception.ServiceException;

public class _UtilMain {
    public static void main(String[] args) throws ServiceException {
        // password
//        String password = "zxc123asd";
//        String hashPassword = HashGenerator.hashPassword(password);
//        System.out.println(hashPassword);

        EmailSender emailSender = new EmailSender("punksim@mail.ru", "Fuck you", "Fuck you");
        System.out.println(emailSender.sendEmail());

    }
}
