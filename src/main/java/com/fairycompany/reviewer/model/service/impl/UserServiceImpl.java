package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.TokenDaoImpl;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.entity.UserToken;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.util.ServiceUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String HYPHEN = "-";
    private static final String EMPTY_LINE = "";
    private static final String RELATIVE_IMAGE_PATH = "media" + File.separator + "people" + File.separator;
    private static final String DEFAULT_FILE = "pic\\default_user.jpg";
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

    public List<User> findAllUsers(SessionRequestContent content) throws ServiceException {
        int actualPage = Integer.parseInt(content.getRequestParameter(ACTUAL_PAGE));
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        List<User> users;

        try {
            transactionManager.initTransaction();

            long skippedUsers = (long) actualPage * rowAmount - rowAmount;
            users = userDao.findAll(skippedUsers, rowAmount);

            long totalUserAmount = userDao.findTotalUserAmount();
            int pageAmount = (int) Math.ceil((double) totalUserAmount / rowAmount);

            content.addRequestAttribute(RequestAttribute.PAGE_AMOUNT, pageAmount);
            content.addRequestAttribute(RequestAttribute.ACTUAL_PAGE, actualPage);
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finding users, {}", e.getMessage());
            throw new ServiceException("Error when finding users", e);
        } finally {
            transactionManager.endTransaction();
        }

        return users;
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
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        boolean isUserAdded = false;

        String login = content.getRequestParameter(RequestParameter.LOGIN);
        String name = content.getRequestParameter(RequestParameter.USER_NAME);
        String surname = content.getRequestParameter(RequestParameter.SURNAME);
        String password = content.getRequestParameter(RequestParameter.PASSWORD);
        String passwordChecker = content.getRequestParameter(RequestParameter.PASSWORD_CHECK);
        LocalDate birthday = serviceUtil.getDateFromString(content.getRequestParameter(RequestParameter.BIRTHDAY));
        String phone = content.getRequestParameter(RequestParameter.PHONE);
        String photoFile = (String) content.getRequestAttribute(RequestAttribute.USER_PHOTO);

        UserValidator validator = UserValidator.getInstance();

        if (!(validator.isLoginValid(login) &&
                validator.isPasswordValid(password) &&
                validator.isNameValid(name) &&
                validator.isNameValid(surname) &&
                validator.isPhoneValid(phone))) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_DATA_ERROR);
        } else if (!validator.isDateValid(birthday)) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.BIRTHDAY_ERROR);
        } else if (!validator.passwordCheck(password, passwordChecker)) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.PASSWORD_DOUBLE_CHECK_ERROR);
        } else {
            User user = new User.UserBuilder()
                    .setLogin(login)
                    .setFirstName(name)
                    .setSecondName(surname)
                    .setBirthday(birthday)
                    .setPhone(getPhoneFromString(phone))
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

    @Override
    public boolean updateUser(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        boolean isUserUpdated = false;

        User user = (User) content.getSessionAttribute(SessionAttribute.USER);

        String login = user.getLogin();
        String name = content.getRequestParameter(RequestParameter.USER_NAME);
        String surname = content.getRequestParameter(RequestParameter.SURNAME);
        LocalDate birthday = serviceUtil.getDateFromString(content.getRequestParameter(RequestParameter.BIRTHDAY));
        String phone = content.getRequestParameter(RequestParameter.PHONE);

        UserValidator validator = UserValidator.getInstance();

        if (!(validator.isNameValid(name) && validator.isNameValid(surname) && validator.isPhoneValid(phone))) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_DATA_ERROR);
        } else if (!validator.isDateValid(birthday)) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.BIRTHDAY_ERROR);
        } else {
            try {
                transactionManager.initTransaction();

                user.setFirstName(name);
                user.setSecondName(surname);
                user.setBirthday(birthday);
                user.setPhone(getPhoneFromString(phone));               //todo спросить про Cloneable и что делать с его исключением

                userDao.update(user);

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.UPDATE_USER_SUCCESS);
                isUserUpdated = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating user with login {}, {}", login, e.getMessage());
                throw new ServiceException("Error when updating user with login " + login, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isUserUpdated;
    }

    @Override
    public boolean updatePassword(SessionRequestContent content) throws ServiceException {
        boolean isPasswordUpdated = false;
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long userId = user.getUserId();

        String newPassword = content.getRequestParameter(PASSWORD);
        String passwordChecker = content.getRequestParameter(PASSWORD_CHECK);

        try {
            if (validator.isPasswordValid(newPassword) && validator.passwordCheck(newPassword, passwordChecker)) {
                String hashPassword = HashGenerator.hashPassword(newPassword);

                transactionManager.initTransaction();
                userDao.updatePassword(userId, hashPassword);
                transactionManager.commit();

                isPasswordUpdated = true;
            }
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when updating password, {}", e.getMessage());
            throw new ServiceException("Error when updating password", e);
        } finally {
            transactionManager.endTransaction();
        }

        return isPasswordUpdated;
    }

    @Override
    public boolean updateUserByAdmin(SessionRequestContent content) throws ServiceException {
        long userId = Long.parseLong(content.getRequestParameter(USER_ID));
        int role = User.Role.valueOf(content.getRequestParameter(USER_ROLE).toUpperCase()).ordinal();
        int status = User.Status.valueOf(content.getRequestParameter(USER_STATUS).toUpperCase()).ordinal();

        try {
                transactionManager.initTransaction();

                userDao.updateRoleAndStatus(userId, role, status);

                transactionManager.commit();
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when updating status and role, {}", e.getMessage());
            throw new ServiceException("Error when updating status and role", e);
        } finally {
            transactionManager.endTransaction();
        }
        return true;
    }

    //    @Override         //todo update не доходит до сервиса (пока)
    //    public boolean updatePhoto(SessionRequestContent content) throws ServiceException {
    //        return false;
    //    }

    public boolean finishRegistration(SessionRequestContent content) throws ServiceException {
        boolean isRegistered = false;

        String registerCode = content.getRequestParameter(REGISTER_CODE);
        long tokenId = Long.parseLong(content.getRequestParameter(TOKEN_ID));
        //todo validation

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

    @Override
    public boolean deleteUser(SessionRequestContent content) throws ServiceException {
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long userId = user.getUserId();

        try {
            transactionManager.initTransaction();

            userDao.delete(userId);

            transactionManager.commit();
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finishing registration {}. ", e.getMessage());
            throw new ServiceException("Error when finishing registration", e);
        } finally {
            transactionManager.endTransaction();
        }

        return true;
    }

    private int getPhoneFromString(String phone) {
        return Integer.parseInt(phone.replace(HYPHEN, EMPTY_LINE));
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
