package com.fairycompany.reviewer.model.validator;

/**
 * Common validator is used to validate request parameters for services.
 */
public class CommonValidator {
    private static CommonValidator instance = new CommonValidator();
    private static final String LONG_PATTERN = "[1-9]\\d{0,17}[0-7]?";
    private static final String DATE_PATTERN = "[1,2]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0,1])";

    private CommonValidator() {
    }

    /**
     * Gets instance of CommonValidator.
     *
     * @return the instance of CommonValidator
     */
    public static CommonValidator getInstance() {
        return instance;
    }

    /**
     * Check whether string can be parsed to long.
     *
     * @param longString string to be tested
     * @return {@code true} if string can be parsed to long
     */
    public boolean isStringLong(String longString) {
        return longString != null && longString.matches(LONG_PATTERN);
    }

    /**
     * Check whether string can be parsed to date.
     *
     * @param dateString string to be tested
     * @return {@code true} if string can be parsed to date
     */
    public boolean isStringDate(String dateString) {
        return dateString != null && dateString.matches(DATE_PATTERN);
    }
}
