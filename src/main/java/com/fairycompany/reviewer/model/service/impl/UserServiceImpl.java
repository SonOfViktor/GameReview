package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.validator.UserValidator;
import com.fairycompany.reviewer.util.EmailSender;
import com.fairycompany.reviewer.util.HashGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static com.fairycompany.reviewer.controller.command.RequestParameter.LOGIN;
import static com.fairycompany.reviewer.controller.command.RequestParameter.PASSWORD;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String REGISTRATION_SUBJECT_EMAIL = "Registration";
    private static final String LETTER = """
                Greeting %s. Thanks for registering with GameReview!            
                    Click here to complete registration %s.
                    Cheers,
                        your pals at GR!
                Please do not reply to this email. Your message will not be received.
                """;
    private static final String REGISTER_LINK = "%s/controller?command=register&user_id=%d&register_code=%s";
    private static UserServiceImpl instance = new UserServiceImpl();
    private UserDaoImpl userDao = UserDaoImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    public Optional<User> authenticate (SessionRequestContent content) throws ServiceException {
        Optional<User> user = Optional.empty();

        String login = content.getRequestParameter(LOGIN);
        String password = content.getRequestParameter(PASSWORD);

        UserValidator validator = UserValidator.getInstance();

        if (validator.isLoginValid(login) && validator.isPasswordValid(password)) {
            String hashPassword = HashGenerator.hashPassword(password);
            try {
                user = userDao.findByLoginAndPassword(login, hashPassword);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Error when authenticating user with login {} and password {}. {}", login, password, e.getMessage());
                throw new ServiceException("Error when authenticating user with login " + login + " and password " + password, e);
            }
        } else {

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

        UserValidator validator = UserValidator.getInstance();

        if (!(validator.isLoginValid(login) &&
                validator.isPasswordValid(password) &&
                validator.isNameValid(name) &&
                validator.isNameValid(surname) &&
                validator.isPhoneValid(phone))) {
            content.addRequestAttribute(RequestAttribute.USER_DATA_ERROR, true);
        } else if (!validator.isDateValid(birthday)) {
            content.addRequestAttribute(RequestAttribute.BIRTHDAY_ERROR, true);
        } else if (!validator.passwordCheck(password, passwordChecker)) {
            content.addRequestAttribute(RequestAttribute.PASSWORD_DOUBLE_CHECK_FAIL, true);
        } else {
            //todo mail

            User user = new User.UserBuilder()
                    .setLogin(login)
                    .setFirstName(name)
                    .setSecondName(surname)
                    .setBirthday(birthday)
                    .setPhone(Integer.parseInt(phone))
                    .setBalance(BigDecimal.ZERO)
                    .setUserRole(User.Role.USER)
                    .setUserStatus(User.Status.NOT_CONFIRMED)
                    .createUser();

            String hashPassword = HashGenerator.hashPassword(password);

            try {
                long userId = userDao.add(user, hashPassword);
                user.setUserId(userId);

                isUserAdded = true;
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Error when adding user with name {} and password {}, {}", login, password, e.getMessage());
                throw new ServiceException("Error when adding user with login " + login + " and password " + password, e);
            }
        }
        return isUserAdded;
    }

    private LocalDate getDateFromString(String stringDate) {
        LocalDate date = LocalDate.of(1, 1,1);
        if (stringDate != null && !stringDate.isEmpty()) {
            date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        logger.log(Level.DEBUG, "Date is {}", date);
        return date;
    }

    private boolean sendRegisterEmail(SessionRequestContent content) {
        String sendTo = content.getRequestParameter(RequestParameter.LOGIN);
        String registerLink = String.format(REGISTER_LINK, content.getRequestAttribute(RequestAttribute.SOURCE_LINK),
                1, UUID.randomUUID());                              // todo токен нужно добавить в базу
        String letter = String.format(LETTER, content.getRequestParameter(RequestParameter.USER_NAME), registerLink);

        EmailSender emailSender = new EmailSender(sendTo, REGISTRATION_SUBJECT_EMAIL, letter);

        return emailSender.sendEmail();
    }
}
