package com.fairycompany.reviewer.model.util;

import com.fairycompany.reviewer.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * This class is used to encrypt text
 */
public class HashGenerator {
    private static final Logger logger = LogManager.getLogger();
    private static final String ALGORITHM_SHA256 = "SHA-256";


    /**
     * Encrypt specified password with algorithm SHA-256
     *
     * @param password the password
     * @return encrypted password
     * @throws ServiceException when NoSuchAlgorithmException occurred
     */
    public static String hashPassword(String password) throws ServiceException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA256);
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] bytesEncoded = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, bytesEncoded);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.ERROR, "Encryption isn't successful. Algorithm is not supported {}", e.getMessage());
            throw new ServiceException("Encryption isn't successful. Algorithm is not supported", e);
        }
    }
}