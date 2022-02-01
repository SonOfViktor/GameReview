package com.fairycompany.reviewer.model.validator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CommonValidatorTest {
    CommonValidator commonValidator;

    @BeforeClass
    public void init(){
        commonValidator = CommonValidator.getInstance();
    }

    @DataProvider(name = "longProvider")
    private Object[][] provideLong() {
        return new Object[][]{
                {"1", true},
                {"9223372036854775807", true},
                {"9223372036854775808", false},
                {"92233720368547758070", false},
                {"0223372036854775807", false},
                {"0", false},
                {"-1", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "dateProvider")
    private Object[][] provideDate() {
        return new Object[][]{
                {"2020-08-09", true},
                {"2021-12-31", true},
                {"2021-13-20", false},
                {"2021-00-01", false},
                {"2021-12-00", false},
                {"2021-12-32", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @Test (dataProvider = "longProvider")
    public void testIsStringLong(String longString, boolean expected) {
        boolean actual = commonValidator.isStringLong(longString);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "dateProvider")
    public void testIsStringDate(String dateString, boolean expected) {
        boolean actual = commonValidator.isStringDate(dateString);

        assertEquals(actual, expected);
    }
}