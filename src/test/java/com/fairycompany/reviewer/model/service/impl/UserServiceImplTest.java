package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.TokenDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.entity.UserToken;
import com.fairycompany.reviewer.model.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import com.fairycompany.reviewer.model.util.ServiceUtil;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.testng.annotations.*;
import java.util.List;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;
import static com.fairycompany.reviewer.controller.command.RequestParameter.PASSWORD;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class UserServiceImplTest {
    MockitoSession mockito;

    @Spy
    private SessionRequestContent content;

    @Spy
    private ServiceUtil serviceUtil;

    @Mock
    private UserDao userDao;

    @Mock
    private TokenDao tokenDao;

    @Mock
    private TransactionManager transactionManager;

    @InjectMocks
    private UserService userService = UserServiceImpl.getInstance();

    @BeforeMethod
    public void setUp() {
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
    }

    @AfterMethod
    public void tearDown() {
        mockito.finishMocking();
    }

    @Test
    public void testFindAllUsers() throws DaoException, ServiceException {
        List users = List.of(new User.UserBuilder().setFirstName("Maks").createUser(),
                new User.UserBuilder().setFirstName("Olga").createUser(),
                new User.UserBuilder().setFirstName("Juliana").createUser(),
                new User.UserBuilder().setFirstName("Oksi").createUser());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("4");
        when(userDao.findAll(anyLong(), anyInt())).thenReturn(users);
        when(userDao.findTotalUserAmount()).thenReturn(9L);

        List actualUsers = userService.findAllUsers(content);
        long actualPageAmount = (Long) content.getRequestAttribute(RequestAttribute.PAGE_AMOUNT);
        long actualActualPage = (Long) content.getRequestAttribute(RequestAttribute.ACTUAL_PAGE);

        assertEquals(actualUsers, users);
        assertEquals(actualPageAmount, 3);
        assertEquals(actualActualPage, 1);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testFindAllUsersDaoFindAllException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("6");
        when(userDao.findAll(0, 6)).thenThrow(DaoException.class);

        userService.findAllUsers(content);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testFindAllUsersDaoFindTotalUserAmountException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("6");
        when(userDao.findTotalUserAmount()).thenThrow(DaoException.class);

        userService.findAllUsers(content);
    }

    @Test
    public void testAuthenticateUser() throws DaoException, ServiceException {
        Optional<User> expectedUser = Optional.of(new User.UserBuilder().setLogin("some_mail@mail.ru").createUser());
        when(content.getRequestParameter(LOGIN)).thenReturn("some_mail@mail.ru");
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");
        when(userDao.findByLoginAndPassword(anyString(), anyString())).thenReturn(expectedUser);

        Optional<User> actualUser = userService.authenticateUser(content);

        assertEquals(actualUser, expectedUser);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void testAuthenticateUserDaoFindUserException() throws DaoException, ServiceException {
        when(content.getRequestParameter(LOGIN)).thenReturn("some_mail@mail.ru");
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");
        when(userDao.findByLoginAndPassword(anyString(), anyString())).thenThrow(DaoException.class);

        userService.authenticateUser(content);
    }

    @Test
    public void testAuthenticateUserInvalidLogin() throws DaoException, ServiceException {
        Optional<User> expectedUser = Optional.empty();
        when(content.getRequestParameter(LOGIN)).thenReturn("<script>alert(Hi)</script>");
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");

        Optional<User> actualUser = userService.authenticateUser(content);

        verify(userDao, never()).findByLoginAndPassword(anyString(), anyString());
        assertEquals(actualUser, expectedUser);
    }

    @Test
    public void testAddUser() throws DaoException, ServiceException {
        initUserDataForAddUserTest();
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.FILE_EXTENSION)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM)).thenReturn(Optional.empty());
        when(userDao.add(any(User.class), anyString())).thenReturn(1L);
        doReturn("").when(serviceUtil).saveDefaultImage(anyString(), anyString(), anyString());

        assertTrue(userService.addUser(content));
        verify(serviceUtil, atMostOnce()).saveDefaultImage(anyString(), anyString(), anyString());
    }

    @Test
    public void testAddUserInvalidData() throws DaoException, ServiceException {
        String expectedMessage = "sing_up.data.invalid.message";
        initUserDataForAddUserTest();
        when(content.getRequestParameter(PASSWORD)).thenReturn("**********");

        boolean isUserAdded = userService.addUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserAdded);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testAddUserInvalidBirthday() throws ServiceException {
        String expectedMessage = "sing_up.birthday.invalid_message";
        initUserDataForAddUserTest();
        when(content.getRequestParameter(BIRTHDAY)).thenReturn("1410-07-15");

        boolean isUserAdded = userService.addUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserAdded);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testAddUserInvalidCheckPassword() throws ServiceException {
        String expectedMessage = "sing_up.password.invalid_double_check_message";
        initUserDataForAddUserTest();
        when(content.getRequestParameter(PASSWORD_CHECK)).thenReturn("*********");

        boolean isUserAdded = userService.addUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserAdded);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testAddExistUser() throws DaoException, ServiceException {
        String expectedMessage = "user_already_exist";
        initUserDataForAddUserTest();
        when(userDao.findUserByLogin("some_mail@mail.ru")).thenReturn(Optional.of(new User()));

        boolean isUserAdded = userService.addUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserAdded);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddUserDaoAddUserException() throws DaoException, ServiceException {
        initUserDataForAddUserTest();
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.FILE_EXTENSION)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM)).thenReturn(Optional.empty());
        when(userDao.add(any(User.class), anyString())).thenThrow(DaoException.class);
        doReturn("").when(serviceUtil).saveDefaultImage(anyString(), anyString(), anyString());

        userService.addUser(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddUserDaoFindUserByLoginException() throws DaoException, ServiceException {
        initUserDataForAddUserTest();
        when(userDao.findUserByLogin(anyString())).thenThrow(DaoException.class);

        userService.addUser(content);
    }

    @Test
    public void testUpdateUser() throws ServiceException {
        initDataForUpdateUserTest();
        String expectedMessage = "update_user_success";

        boolean isUserUpdated = userService.updateUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE);

        assertTrue(isUserUpdated);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testUpdateUserInvalidData() throws ServiceException {
        initDataForUpdateUserTest();
        String expectedMessage = "sing_up.data.invalid.message";
        when(content.getRequestParameter(USER_NAME)).thenReturn("Jaск");

        boolean isUserUpdated = userService.updateUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserUpdated);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testUpdateUserInvalidBirthday() throws ServiceException {
        initDataForUpdateUserTest();
        String expectedMessage = "sing_up.birthday.invalid_message";
        when(content.getRequestParameter(BIRTHDAY)).thenReturn("2022-01-29");

        boolean isUserUpdated = userService.updateUser(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);

        assertFalse(isUserUpdated);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdateUserDaoUpdateException() throws DaoException, ServiceException {
        initDataForUpdateUserTest();
        when(userDao.update(any(User.class))).thenThrow(DaoException.class);

        userService.updateUser(content);
    }

    @Test
    public void testUpdatePassword() throws ServiceException {
        User user = new User.UserBuilder().setUserId(1).createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");
        when(content.getRequestParameter(PASSWORD_CHECK)).thenReturn("zxcASD123");

        assertTrue(userService.updatePassword(content));
    }

    @Test
    public void testUpdatePasswordInvalidPasswordCheck() throws ServiceException {
        User user = new User.UserBuilder().setUserId(1).createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(PASSWORD)).thenReturn("ZxcASD123");
        when(content.getRequestParameter(PASSWORD_CHECK)).thenReturn("zxcASD123");

        assertFalse(userService.updatePassword(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdatePasswordDaoUpdatePasswordException() throws ServiceException, DaoException {
        User user = new User.UserBuilder().setUserId(1).createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");
        when(content.getRequestParameter(PASSWORD_CHECK)).thenReturn("zxcASD123");
        when(userDao.updatePassword(anyLong(), anyString())).thenThrow(DaoException.class);

        userService.updatePassword(content);
    }

    @Test
    public void testUpdateUserByAdmin() throws ServiceException {
        when(content.getRequestParameter(USER_ID)).thenReturn("10");
        when(content.getRequestParameter(USER_ROLE)).thenReturn("user");
        when(content.getRequestParameter(USER_STATUS)).thenReturn("active");

        assertTrue(userService.updateUserByAdmin(content));
    }

    @Test
    public void testUpdateUserByAdminInvalidData() throws ServiceException {
        when(content.getRequestParameter(USER_ID)).thenReturn("10");
        when(content.getRequestParameter(USER_ROLE)).thenReturn("");
        when(content.getRequestParameter(USER_STATUS)).thenReturn("active");

        assertFalse(userService.updateUserByAdmin(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdateUserByAdminDaoMethodException() throws ServiceException, DaoException {
        when(content.getRequestParameter(USER_ID)).thenReturn("10");
        when(content.getRequestParameter(USER_ROLE)).thenReturn("user");
        when(content.getRequestParameter(USER_STATUS)).thenReturn("active");
        when(userDao.updateRoleAndStatus(anyLong(), anyInt(), anyInt())).thenThrow(DaoException.class);

        userService.updateUserByAdmin(content);
    }

    @Test
    public void testFinishRegistration() throws ServiceException, DaoException {
        UserToken token = new UserToken(1, "340c5c3f-ad0d-43ea-96ce-663cfe2fd0bc", LocalDateTime.now());
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("340c5c3f-ad0d-43ea-96ce-663cfe2fd0bc");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");
        when(tokenDao.findTokenById(anyLong())).thenReturn(Optional.of(token));

        assertTrue(userService.finishRegistration(content));
    }

    @Test
    public void testFinishRegistrationAbsentToken() throws ServiceException, DaoException {
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("340c5c3f-ad0d-43ea-96ce-663cfe2fd0bc");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");
        when(tokenDao.findTokenById(anyLong())).thenReturn(Optional.empty());

        assertFalse(userService.finishRegistration(content));
    }

    @Test
    public void testFinishRegistrationNotMatchToken() throws ServiceException, DaoException {
        UserToken token = new UserToken(1, "52d7b53b-bacf-448b-b4e0-0b1a205a4127", LocalDateTime.now());
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("340c5c3f-ad0d-43ea-96ce-663cfe2fd0bc");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");
        when(tokenDao.findTokenById(anyLong())).thenReturn(Optional.of(token));

        assertFalse(userService.finishRegistration(content));
    }

    @Test
    public void testFinishRegistrationInvalidData() throws ServiceException, DaoException {
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("<script>alert(Hi)</script>");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");

        assertFalse(userService.finishRegistration(content));
        verify(tokenDao, never()).findTokenById(anyLong());
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFinishRegistrationDaoFindUserByIdException() throws ServiceException, DaoException {
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("52d7b53b-bacf-448b-b4e0-0b1a205a4127");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");
        when(tokenDao.findTokenById(anyLong())).thenThrow(DaoException.class);

        userService.finishRegistration(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFinishRegistrationDaoUpdateStatus() throws ServiceException, DaoException {
        UserToken token = new UserToken(1, "52d7b53b-bacf-448b-b4e0-0b1a205a4127", LocalDateTime.now());
        when(content.getRequestParameter(REGISTER_CODE)).thenReturn("52d7b53b-bacf-448b-b4e0-0b1a205a4127");
        when(content.getRequestParameter(TOKEN_ID)).thenReturn("1");
        when(tokenDao.findTokenById(anyLong())).thenReturn(Optional.of(token));
        when(userDao.updateStatus(anyLong(), eq(User.Status.ACTIVE))).thenThrow(DaoException.class);

        userService.finishRegistration(content);
    }

    @Test
    public void testDeleteUser() throws ServiceException, DaoException {
        User user = new User.UserBuilder().setUserId(1).createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(userDao.delete(anyLong())).thenReturn(true);
        doNothing().when(serviceUtil).deleteFile(anyString());

        assertTrue(userService.deleteUser(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testDeleteUserDaoDeleteException() throws ServiceException, DaoException {
        User user = new User.UserBuilder().setUserId(1).createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(userDao.delete(anyLong())).thenThrow(DaoException.class);

        userService.deleteUser(content);
    }

    private void initUserDataForAddUserTest() {
        when(content.getRequestParameter(LOGIN)).thenReturn("punksim@mail.ru");
        when(content.getRequestParameter(USER_NAME)).thenReturn("Алесь");
        when(content.getRequestParameter(SURNAME)).thenReturn("Белы");
        when(content.getRequestParameter(PASSWORD)).thenReturn("zxcASD123");
        when(content.getRequestParameter(PASSWORD_CHECK)).thenReturn("zxcASD123");
        when(content.getRequestParameter(BIRTHDAY)).thenReturn("1991-07-27");
        when(content.getRequestParameter(PHONE)).thenReturn("33-777-78-87");
    }

    private User initDataForUpdateUserTest() {
        User user = new User.UserBuilder().setLogin("user_login@yandex.ru").createUser();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(USER_NAME)).thenReturn("Jack");
        when(content.getRequestParameter(SURNAME)).thenReturn("Smith");
        when(content.getRequestParameter(BIRTHDAY)).thenReturn("2005-12-20");
        when(content.getRequestParameter(PHONE)).thenReturn("29-789-45-12");

        return user;
    }
}