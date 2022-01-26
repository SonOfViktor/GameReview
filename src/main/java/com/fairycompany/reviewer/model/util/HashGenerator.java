package com.fairycompany.reviewer.model.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
    private static final Logger logger = LogManager.getLogger();
    private static final String ALGORITHM_SHA256 = "SHA-256";

    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA256);
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] bytesEncoded = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, bytesEncoded);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.ERROR, "Encoding password isn't successful. {}", e.getMessage(), e);
        }
        return password;
    }
}