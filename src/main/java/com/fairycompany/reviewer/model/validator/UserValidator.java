package com.fairycompany.reviewer.model.validator;

import com.fairycompany.reviewer.model.entity.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class UserValidator {
    private final static String LOGIN_REGEX = "[\\d\\w-]{3,25}@\\w{2,10}\\.\\w{2,5}";
    private final static String PASSWORD_REGEX = "(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})[\\d\\p{Alpha}]{8,30}";
    private final static String NAME_REGEX = "[\\p{Alpha}А-Яа-яЁё]{2,20}";
    private final static String PHONE_REGEX = "\\d{9}";
    private final static String TOKEN_REGEX = "[\\p{Lower}\\p{Digit}]{8}(-[\\p{Lower}\\p{Digit}]{4}){3}-[\\p{Lower}\\p{Digit}]{12}";
    private final static int BOTTOM_AGE_THRESHOLD = 100;
    private final static int TOP_AGE_THRESHOLD = 16;
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
        int userAge = Period.between(birthdayDate, LocalDate.now()).getYears();
        return  userAge >= TOP_AGE_THRESHOLD && userAge < BOTTOM_AGE_THRESHOLD;
    }

    public boolean isRoleValid(String role) {
        return Arrays.stream(User.Role.values()).anyMatch((rl) -> rl.name().equals(role));
    }

    public boolean isStatusValid(String status) {
        return Arrays.stream(User.Role.values()).anyMatch((st) -> st.name().equals(status));
    }

    public boolean isTokenValid(String token) {
        return token.matches(TOKEN_REGEX);
    }

}
