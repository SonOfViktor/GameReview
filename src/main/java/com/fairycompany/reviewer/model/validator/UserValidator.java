package com.fairycompany.reviewer.model.validator;

import java.time.LocalDate;

public class UserValidator {
    private final static String LOGIN_REGEX = "[\\d\\w-]{3,25}@\\w{2,10}\\.\\w{2,5}";
    private final static String PASSWORD_REGEX = "(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})[\\d\\p{Alpha}]{8,30}";
    private final static String NAME_REGEX = "[\\p{Alpha}А-Яа-яЁё]{2,20}";
    private final static String PHONE_REGEX = "\\d{2}-?\\d{3}-?\\d{2}-?\\d{2}";
    private static UserValidator instance = new UserValidator();

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        return instance;
    }

    public boolean isLoginValid(String login) {
        return login != null && !login.isBlank() && login.matches(LOGIN_REGEX);
    }

    public boolean isPasswordValid(String password) {
        return password != null && !password.isBlank() && password.matches(PASSWORD_REGEX);
    }

    public boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.matches(NAME_REGEX);
    }

    public boolean passwordCheck(String password, String passwordChecker) {
        return password.equals(passwordChecker);
    }

    public boolean isPhoneValid(String phone) {
        return phone != null && !phone.isBlank() && phone.matches(PHONE_REGEX);
    }

    public boolean isDateValid(LocalDate birthdayDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lowThreshold = currentDate.minusYears(100);
        LocalDate highThreshold = currentDate.minusYears(16);
        boolean isYoungerThan100 = birthdayDate.isAfter(lowThreshold) || birthdayDate.isEqual(lowThreshold);
        boolean isOlderThan16 = birthdayDate.isBefore(highThreshold) || birthdayDate.isEqual(highThreshold);
        return isYoungerThan100 && isOlderThan16;
    }

}
