package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.TokenDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.dao.impl.TokenDaoImpl;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.entity.UserToken;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.validator.CommonValidator;
import com.fairycompany.reviewer.model.util.ServiceUtil;
import com.fairycompany.reviewer.model.validator.UserValidator;
import com.fairycompany.reviewer.model.util.EmailSender;
import com.fairycompany.reviewer.model.util.HashGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String RELATIVE_PHOTO_DIR = "media\\people\\";
    private static final String DEFAULT_FILE = "pic\\default_user.jpg";
    private static final String HYPHEN = "-";
    private static final String EMPTY_LINE = "";
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
    private UserDao userDao = UserDaoImpl.getInstance();
    private TokenDao tokenDao = TokenDaoImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    public List<User> findAllUsers(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();

        long actualPage = serviceUtil.takeActualPage(content);
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        List<User> users;

        try {
            transactionManager.initTransaction();

            long skippedUsers = actualPage * rowAmount - rowAmount;
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
        User.UserBuilder userBuilder = new User.UserBuilder();
        boolean isUserAdded = false;

        String hashPassword = makeUserWithHashPassword(content, userBuilder);
        if (!hashPassword.isEmpty()) {
            User user = userBuilder.createUser();

            String uploadDirectory = (String) content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY);
            String fileExtension = (String) content.getRequestAttribute(RequestAttribute.FILE_EXTENSION);
            Optional<InputStream> imageStream = (Optional<InputStream>) content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM);

            String photoPath = RELATIVE_PHOTO_DIR + user.getLogin();
            serviceUtil.makeDir(uploadDirectory + RELATIVE_PHOTO_DIR);

            try {
                photoPath = (imageStream.isPresent()) ?
                        serviceUtil.saveImage(uploadDirectory, photoPath, fileExtension, imageStream.get()) :
                        serviceUtil.saveDefaultImage(uploadDirectory, photoPath, DEFAULT_FILE);
                user.setPhoto(photoPath);

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
                serviceUtil.deleteFile(uploadDirectory + photoPath);
                logger.log(Level.ERROR, "Error when adding user with name {}, {}", user.getLogin(), e.getMessage());
                throw new ServiceException("Error when adding user with login " + user.getLogin(), e);
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
        String phone = content.getRequestParameter(RequestParameter.PHONE).replace(HYPHEN, EMPTY_LINE);

        UserValidator validator = UserValidator.getInstance();

        if (!(validator.isNameValid(name) && validator.isNameValid(surname) && validator.isPhoneValid(phone))) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_DATA_ERROR);
        } else if (!validator.isDateValid(birthday)) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.BIRTHDAY_ERROR);
        } else {
            try {
                transactionManager.initTransaction();

                User updatedUser = user.clone();

                updatedUser.setFirstName(name);
                updatedUser.setSecondName(surname);
                updatedUser.setBirthday(birthday);
                updatedUser.setPhone(Integer.parseInt(phone));

                userDao.update(updatedUser);

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.USER, updatedUser);
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
        boolean isUpdated = false;
        String userIdString = content.getRequestParameter(USER_ID);
        String roleString = content.getRequestParameter(USER_ROLE).toUpperCase();
        String statusString = content.getRequestParameter(USER_STATUS).toUpperCase();

        CommonValidator commonValidator = CommonValidator.getInstance();
        UserValidator userValidator = UserValidator.getInstance();

        if (commonValidator.isStringLong(userIdString) &&
                userValidator.isRoleValid(roleString) &&
                userValidator.isStatusValid(statusString)) {
            long userId = Long.parseLong(userIdString);
            int role = User.Role.valueOf(roleString).ordinal();
            int status = User.Status.valueOf(statusString).ordinal();

            try {
                transactionManager.initTransaction();

                userDao.updateRoleAndStatus(userId, role, status);

                transactionManager.commit();

                isUpdated = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating status and role, {}", e.getMessage());
                throw new ServiceException("Error when updating status and role", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isUpdated;
    }

    public boolean finishRegistration(SessionRequestContent content) throws ServiceException {
        boolean isRegistered = false;

        String registerCode = content.getRequestParameter(REGISTER_CODE);
        String tokenIdString = content.getRequestParameter(TOKEN_ID);

        CommonValidator commonValidator = CommonValidator.getInstance();
        UserValidator userValidator = UserValidator.getInstance();

        if (userValidator.isTokenValid(registerCode) && commonValidator.isStringLong(tokenIdString)) {
            long tokenId = Long.parseLong(tokenIdString);

            try {
                transactionManager.initTransaction();

                Optional<UserToken> userToken = tokenDao.findTokenById(tokenId);

                if (userToken.isPresent() && userToken.get().getToken().equals(registerCode)) {
                    userDao.updateStatus(userToken.get().getUserId(), User.Status.ACTIVE);
                    tokenDao.deleteToken(tokenId);

                    transactionManager.commit();

                    isRegistered = true;
                }
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Error when finishing registration {}. ", e.getMessage());
                throw new ServiceException("Error when finishing registration", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isRegistered;
    }

    @Override
    public boolean deleteUser(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        String uploadDirectory = (String) content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY);

        long userId = user.getUserId();

        try {
            transactionManager.initTransaction();

            userDao.delete(userId);
            serviceUtil.deleteFile(uploadDirectory + user.getPhoto());

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

    private boolean sendRegisterEmail(SessionRequestContent content, long tokenId, String token) {
        String sendTo = content.getRequestParameter(RequestParameter.LOGIN);
        String registerLink = String.format(REGISTER_LINK, content.getRequestAttribute(RequestAttribute.SOURCE_LINK),
                tokenId, token);
        String letter = String.format(LETTER, content.getRequestParameter(RequestParameter.USER_NAME), registerLink);

        EmailSender emailSender = new EmailSender(sendTo, REGISTRATION_SUBJECT_EMAIL, letter);

        return emailSender.sendEmail();
    }

    private String makeUserWithHashPassword(SessionRequestContent content, User.UserBuilder userBuilder) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        String hashPassword = "";

        String login = content.getRequestParameter(RequestParameter.LOGIN);
        String name = content.getRequestParameter(RequestParameter.USER_NAME);
        String surname = content.getRequestParameter(RequestParameter.SURNAME);
        String password = content.getRequestParameter(RequestParameter.PASSWORD);
        String passwordChecker = content.getRequestParameter(RequestParameter.PASSWORD_CHECK);
        LocalDate birthday = serviceUtil.getDateFromString(content.getRequestParameter(RequestParameter.BIRTHDAY));
        String phone = content.getRequestParameter(RequestParameter.PHONE).replace(HYPHEN, EMPTY_LINE);

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
        } else if(isUserExist(login)) {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_ALREADY_EXIST);
        } else {
            userBuilder.setLogin(login)
                    .setFirstName(name)
                    .setSecondName(surname)
                    .setBirthday(birthday)
                    .setPhone(Integer.parseInt(phone))
                    .setUserRole(User.Role.USER)
                    .setUserStatus(User.Status.NOT_CONFIRMED);

            hashPassword = HashGenerator.hashPassword(password);
        }

        return hashPassword;
    }

    private boolean isUserExist(String name) throws ServiceException {
        Optional<User> user;

        try {
            transactionManager.initTransaction();

            user = userDao.findUserByLogin(name);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finding user, {}", e.getMessage());
            throw new ServiceException("Error when finding user", e);
        }

        return user.isPresent();
    }
}
