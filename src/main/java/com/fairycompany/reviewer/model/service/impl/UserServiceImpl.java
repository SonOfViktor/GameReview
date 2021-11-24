package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.TokenDaoImpl;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.entity.UserToken;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.validator.UserValidator;
import com.fairycompany.reviewer.util.EmailSender;
import com.fairycompany.reviewer.util.HashGenerator;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String RELATIVE_IMAGE_PATH = "media" + File.separator + "people" + File.separator;
    private static final String DEFAULT_IMAGE_PATH = "media" + File.separator + "people" + File.separator + "default.jpg";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String REGISTRATION_SUBJECT_EMAIL = "Registration";
    private static final String LETTER = """
                Greeting %s. Thanks for registering with GameReview!            
                    Click here to complete registration %s.
                    Cheers,
                        your pals at GR!
                Please do not reply to this email. Your message will not be received.
                """;
    private static final String REGISTER_LINK = "%s/controller?command=finish_registration&token_id=%d&register_code=%s";
    private static UserServiceImpl instance = new UserServiceImpl();
    private UserValidator validator = UserValidator.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private UserDaoImpl userDao = UserDaoImpl.getInstance();
    private TokenDaoImpl tokenDao = TokenDaoImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    public Optional<User> authenticate (SessionRequestContent content) throws ServiceException {
        Optional<User> user = Optional.empty();

        String login = content.getRequestParameter(LOGIN);
        String password = content.getRequestParameter(PASSWORD);

        if (validator.isLoginValid(login) && validator.isPasswordValid(password)) {
            String hashPassword = HashGenerator.hashPassword(password);

            try {
                transactionManager.initTransaction();

                user = userDao.findByLoginAndPassword(login, hashPassword);

                transactionManager.commit();
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Error when authenticating user with login {} and password {}. {}", login, password, e.getMessage());
                throw new ServiceException("Error when authenticating user with login " + login + " and password " + password, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return user;
    }

    public boolean addUser(SessionRequestContent content) throws ServiceException {
        boolean isUserAdded = false;

        String login = content.getRequestParameter(RequestParameter.LOGIN);
        String name = content.getRequestParameter(RequestParameter.USER_NAME);
        String surname = content.getRequestParameter(RequestParameter.SURNAME);
        String password = content.getRequestParameter(RequestParameter.PASSWORD);
        String passwordChecker = content.getRequestParameter(RequestParameter.PASSWORD_CHECK);
        LocalDate birthday = getDateFromString(content.getRequestParameter(RequestParameter.BIRTHDAY));
        String phone = content.getRequestParameter(RequestParameter.PHONE);
        Part part = (Part) content.getRequestAttribute(RequestAttribute.PART);

        UserValidator validator = UserValidator.getInstance();

        if (!(validator.isLoginValid(login) &&
                validator.isPasswordValid(password) &&
                validator.isNameValid(name) &&
                validator.isNameValid(surname) &&
                validator.isPhoneValid(phone))) {
            content.addRequestAttribute(RequestAttribute.USER_DATA_MESSAGE, LocaleMessageKey.USER_DATA_ERROR); //todo переделать под одну переменную
        } else if (!validator.isDateValid(birthday)) {
            content.addRequestAttribute(RequestAttribute.BIRTHDAY_MESSAGE, LocaleMessageKey.BIRTHDAY_ERROR);
        } else if (!validator.passwordCheck(password, passwordChecker)) {
            content.addRequestAttribute(RequestAttribute.PASSWORD_MESSAGE, LocaleMessageKey.PASSWORD_DOUBLE_CHECK_ERROR);
        } else {
            String photoFile = (part != null) ? saveImage(content, part) : DEFAULT_IMAGE_PATH;      // todo util and check
            User user = new User.UserBuilder()
                    .setLogin(login)
                    .setFirstName(name)
                    .setSecondName(surname)
                    .setBirthday(birthday)
                    .setPhone(Integer.parseInt(phone))
                    .setBalance(BigDecimal.ZERO)
                    .setPhoto(photoFile)
                    .setUserRole(User.Role.USER)
                    .setUserStatus(User.Status.NOT_CONFIRMED)
                    .createUser();

            String hashPassword = HashGenerator.hashPassword(password);

            try {
                transactionManager.initTransaction();

                long userId = userDao.add(user, hashPassword);
                user.setUserId(userId);

//                String token = UUID.randomUUID().toString();                  // todo uncomment to send email
//                long tokenId = tokenDao.addRegistrationToken(userId, token);  // todo uncomment to send email
//                sendRegisterEmail(content, tokenId, token);                   // todo uncomment to send email

                transactionManager.commit();

                isUserAdded = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when adding user with name {} and password {}, {}", login, password, e.getMessage());
                throw new ServiceException("Error when adding user with login " + login + " and password " + password, e);
            } finally {
                transactionManager.endTransaction();
            }
        }
        return isUserAdded;
    }

    public boolean finishRegistration(SessionRequestContent content) throws ServiceException {
        boolean isRegistered = false;

        String registerCode = content.getRequestParameter(REGISTER_CODE);
        long tokenId = Long.parseLong(content.getRequestParameter(TOKEN_ID));
        //todo validation maybe

        try {
            transactionManager.initTransaction();
            Optional<UserToken> userToken = tokenDao.findTokenById(tokenId);
            if (userToken.isPresent()) {
                if (userToken.get().getToken().equals(registerCode)) {
                    userDao.updateStatus(userToken.get().getUserId(), User.Status.ROOKIE);
                    transactionManager.commit();
                    isRegistered = true;
                }
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finishing registration {}. ", e.getMessage());
            throw new ServiceException("Error when finishing registration", e);
        } finally {
            transactionManager.endTransaction();
        }

        return isRegistered;
    }

    private String saveImage(SessionRequestContent content, Part part) {
        String uploadFileDir = content.getRequestAttribute(RequestAttribute.IMAGE_UPLOAD_DIRECTORY) + RELATIVE_IMAGE_PATH;
        logger.log(Level.DEBUG, "UploadFileDir is {}", uploadFileDir);
//        File fileSaveDir = new File(uploadFileDir);
//        if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdirs();
//        }

        String generatedFileName = null;
        try {
            String submittedFileName = part.getSubmittedFileName();
            String fileExtension = submittedFileName.substring(submittedFileName.lastIndexOf("."));
            generatedFileName = content.getRequestParameter(LOGIN) + fileExtension;
            String imagePath = uploadFileDir + File.separator + generatedFileName;
            part.write(imagePath);
//            Path imagePath = new File(uploadFileDir + File.separator + generatedFileName).toPath();       // todo delete
            logger.log(Level.DEBUG, "Image path is {}", imagePath);
//            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);                      //todo delete
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to upload file.", e);
//            requestContent.addSessionAttribute(SessionAttribute.ERROR_KEY, BundleKey.INVALID_UPLOAD_FILE);
        }
        return RELATIVE_IMAGE_PATH + generatedFileName;
    }

    private LocalDate getDateFromString(String stringDate) {                //todo make util
        LocalDate date = LocalDate.of(1, 1,1);
        if (stringDate != null && !stringDate.isEmpty()) {
            date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        logger.log(Level.DEBUG, "Date is {}", date);
        return date;
    }

    private boolean sendRegisterEmail(SessionRequestContent content, long tokenId, String token) {
        String sendTo = content.getRequestParameter(RequestParameter.LOGIN);
        String registerLink = String.format(REGISTER_LINK, content.getRequestAttribute(RequestAttribute.SOURCE_LINK),
                tokenId, token);                              // todo токен нужно добавить в базу
        String letter = String.format(LETTER, content.getRequestParameter(RequestParameter.USER_NAME), registerLink);

        EmailSender emailSender = new EmailSender(sendTo, REGISTRATION_SUBJECT_EMAIL, letter);

        return emailSender.sendEmail();
    }
}
