package com.fairycompany.reviewer.model.validator;

public class CommonValidator {
    private static CommonValidator instance = new CommonValidator();
    private static final String LONG_PATTERN = "\\d{1,18}[0-7]?";

    private CommonValidator() {
    }

    public static CommonValidator getInstance() {
        return instance;
    }

    public boolean isStringLong(String string) {
        return string != null && string.matches(LONG_PATTERN);
    }
}
