package com.fairycompany.reviewer.model.util;

import com.fairycompany.reviewer.exception.ServiceException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HashGeneratorTest {

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testHashPassword() throws ServiceException {
        String password = "zxcASD123";
        String expectedHashPassword = "3955a0d3d64a9357e12a4e9d369522a053aab4a248a07c93c78e22d0519cd1d0";

        String actualHashPassword = HashGenerator.hashPassword(password);
        assertEquals(actualHashPassword, expectedHashPassword);
    }
}