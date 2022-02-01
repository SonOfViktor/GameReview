package com.fairycompany.reviewer.model.validator;

import com.fairycompany.reviewer.model.entity.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

/**
 *  User validator is used to validate request parameters for user services.
 */
public class UserValidator {
    private final static String LOGIN_REGEX = "[\\d\\w-]{3,25}@\\w{2,10}\\.\\w{2,5}";
    private final static String PASSWORD_REGEX = "(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})[\\p{Alnum}]{8,30}";
    private final static String NAME_REGEX = "\\p{Upper}\\p{Lower}{1,19}|[А-ЯЁ][а-яё]{1,19}";
    private final static String PHONE_REGEX = "\\d{9}";
    private final static String TOKEN_REGEX = "[\\p{Lower}\\p{Digit}]{8}(-[\\p{Lower}\\p{Digit}]{4}){3}-[\\p{Lower}\\p{Digit}]{12}";
    private final static int BOTTOM_AGE_THRESHOLD = 100;
    private final static int TOP_AGE_THRESHOLD = 16;
    private static UserValidator instance = new UserValidator();

    private UserValidator() {
    }

    /**
     * Gets instance of UserValidator.
     *
     * @return the instance of UserValidator
     */
    public static UserValidator getInstance() {
        return instance;
    }

    /**
     * Check whether login is valid.
     *
     * @param login the login
     * @return {@code true} if login is valid
     */
    public boolean isLoginValid(String login) {
        return login != null && !login.isBlank() && login.matches(LOGIN_REGEX);
    }

    /**
     * Check whether password is valid.
     *
     * @param password password
     * @return {@code true} if password is valid
     */
    public boolean isPasswordValid(String password) {
        return password != null && !password.isBlank() && password.matches(PASSWORD_REGEX);
    }

    /**
     * Check whether name is valid.
     *
     * @param name the username
     * @return {@code true} if name is valid
     */
    public boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.matches(NAME_REGEX);
    }

    /**
     * Check whether password and password checker are same.
     *
     * @param password        the password
     * @param passwordChecker the password checker
     * @return {@code true} if password and passwordChecker are the same
     */
    public boolean passwordCheck(String password, String passwordChecker) {
        return password.equals(passwordChecker);
    }

    /**
     * Check whether phone is valid.
     *
     * @param phone the user phone number
     * @return {@code true} if phone number is valid
     */
    public boolean isPhoneValid(String phone) {
        return phone != null && !phone.isBlank() && phone.matches(PHONE_REGEX);
    }

    /**
     * Check whether birthday date is valid.
     *
     * @param birthdayDate the user birthday date
     * @return {@code true} if user's age is between 16 and 100 years
     */
    public boolean isBirthdayValid(LocalDate birthdayDate) {
        int userAge = (birthdayDate != null) ? Period.between(birthdayDate, LocalDate.now()).getYears() : 100;
        return  userAge >= TOP_AGE_THRESHOLD && userAge < BOTTOM_AGE_THRESHOLD;
    }

    /**
     * Check whether user role exists.
     *
     * @param role the user role
     * @return {@code true} if user role exists
     */
    public boolean isRoleValid(String role) {
        return Arrays.stream(User.Role.values()).anyMatch((rl) -> rl.name().equals(role));
    }

    /**
     * Check whether user status exists.
     *
     * @param status the user status
     * @return {@code true} if user status exists
     */
    public boolean isStatusValid(String status) {
        return Arrays.stream(User.Status.values()).anyMatch((st) -> st.name().equals(status));
    }

    /**
     * Check whether token is valid.
     *
     * @param token the token
     * @return {@code true} if token is valid
     */
    public boolean isTokenValid(String token) {
        return token != null && token.matches(TOKEN_REGEX);
    }

}
