package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.GameRatingDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.entity.GameRating;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameRatingService;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class GameRatingServiceImplTest {
    private final User user = new User.UserBuilder().setUserId(1).createUser();
    private MockitoSession mockito;

    @Spy
    private SessionRequestContent content;

    @Mock
    private GameRatingDao gameRatingDao;

    @Mock
    private TransactionManager transactionManager;

    @InjectMocks
    private GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

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
    public void testFindGameRating() throws DaoException, ServiceException {
        Optional<GameRating> gameRating = Optional.of(new GameRating.GameRatingBuilder()
                .setGameplayRating(20)
                .setGraphicsRating(30)
                .setSoundRating(50)
                .setPlotRating(60)
                .createGameRating());
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER))
                .thenReturn(user);
        when(gameRatingDao.findGameRatingById(eq(1L), eq(1L))).thenReturn(gameRating);
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");

        Optional<GameRating> actualGameRating = gameRatingService.findGameRating(content);

        assertEquals(actualGameRating, gameRating);
        verify(content).addRequestAttribute(RequestAttribute.USER_TOTAL_RATING, 40);
    }

    @Test
    public void testFindGameRatingWrongGameId() throws ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("id");

        Optional<GameRating> actualGameRating = gameRatingService.findGameRating(content);

        assertTrue(actualGameRating.isEmpty());
    }

    @Test
    public void testFindGameRatingNoUser() throws ServiceException, DaoException {
        Optional<GameRating> expectedGameRating = Optional.of(new GameRating());
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(null);
        when(gameRatingDao.findGameRatingById(eq(0L), eq(1L))).thenReturn(Optional.empty());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");

        Optional<GameRating> actualGameRating = gameRatingService.findGameRating(content);

        assertEquals(actualGameRating, expectedGameRating);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindGameRatingDaoFindGRException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findGameRatingById(eq(1L), eq(1L))).thenThrow(DaoException.class);

        gameRatingService.findGameRating(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindGameRatingDaoFindTotalRatingException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findTotalGameRating(eq(1L))).thenThrow(DaoException.class);

        gameRatingService.findGameRating(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindGameRatingDaoFindUserAmountException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findUserAmount(eq(1L))).thenThrow(DaoException.class);

        gameRatingService.findGameRating(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindGameRatingDaoFindReviewException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findGameRatingById(eq(1L), eq(1L))).thenReturn(Optional.empty());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameRatingDao.findReviewsForGame(anyLong(), anyLong(), anyLong(), anyInt())).thenThrow(DaoException.class);

        gameRatingService.findGameRating(content);
    }

    @Test
    public void testFindUserRatingAmount() throws DaoException, ServiceException {
        List<Map<String, Object>> minMaxRating = List.of(Map.of("max_game_name", "Doom", "max_rating", 98,
                "min_game_name", "Rage", "min_rating", 2));
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findMinMaxUserRating(eq(1L))).thenReturn(minMaxRating);

        gameRatingService.findUserRatingAmount(content);

        verify(content).addRequestAttribute(RequestAttribute.MIN_MAX_USER_RATING, minMaxRating.get(0));
    }

    @Test
    public void testFindUserRatingAmountNoMinMax() throws DaoException, ServiceException {
        List<Map<String, Object>> minMaxRating = List.of();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findMinMaxUserRating(eq(1L))).thenReturn(minMaxRating);

        gameRatingService.findUserRatingAmount(content);

        verify(content, never()).addRequestAttribute(eq(RequestAttribute.MIN_MAX_USER_RATING), anyMap());
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindUserRatingAmountDaoFindAmountException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findRatingAmount(eq(1L))).thenThrow(DaoException.class);

        gameRatingService.findUserRatingAmount(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindUserRatingAmountDaoFindMinMaxException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(gameRatingDao.findMinMaxUserRating(eq(1L))).thenThrow(DaoException.class);

        gameRatingService.findUserRatingAmount(content);
    }

    @Test
    public void testAddUpdateGameReview() throws DaoException, ServiceException {
        initGameRatingField();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when((String) content.getRequestAttribute(RequestAttribute.SWITCH)).thenReturn("create");

        assertTrue(gameRatingService.addUpdateGameReview(content));
        verify(gameRatingDao).add(any(GameRating.class));
    }

    @Test
    public void testAddUpdateNullGameReview() throws DaoException, ServiceException {
        initGameRatingField();
        when(content.getRequestParameter(REVIEW)).thenReturn(null);
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when((String) content.getRequestAttribute(RequestAttribute.SWITCH)).thenReturn("update");

        assertTrue(gameRatingService.addUpdateGameReview(content));
        verify(gameRatingDao).update(any(GameRating.class));
    }

    @Test
    public void testAddUpdateGameReviewInvalidData() throws ServiceException {
        initGameRatingField();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(GRAPHICS_RATING)).thenReturn("fifty");

        assertFalse(gameRatingService.addUpdateGameReview(content));
    }

    @Test
    public void testAddUpdateGameReviewInvalidSwitchParameter() throws ServiceException {
        initGameRatingField();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when((String) content.getRequestAttribute(RequestAttribute.SWITCH)).thenReturn("invalid parameter");

        assertFalse(gameRatingService.addUpdateGameReview(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddUpdateDaoAddException() throws DaoException, ServiceException {
        initGameRatingField();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
    when(content.getRequestAttribute(RequestAttribute.SWITCH)).thenReturn("create");
        when(gameRatingDao.add(any(GameRating.class))).thenThrow(DaoException.class);

        gameRatingService.addUpdateGameReview(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddUpdateDaoUpdateException() throws DaoException, ServiceException {
        initGameRatingField();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
    when((String) content.getRequestAttribute(RequestAttribute.SWITCH)).thenReturn("update");
        when(gameRatingDao.update(any(GameRating.class))).thenThrow(DaoException.class);

        gameRatingService.addUpdateGameReview(content);
    }

    @Test
    public void testDeleteUserRating() throws ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");

        assertTrue(gameRatingService.deleteUserRating(content));
    }

    @Test
    public void testDeleteUserRatingInvalidData() throws ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(GAME_ID)).thenReturn("<script>");

        assertFalse(gameRatingService.deleteUserRating(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testDeleteUserRatingDaoException() throws ServiceException, DaoException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(user);
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(gameRatingDao.deleteUserRating(eq(1L), eq(1L))).thenThrow(DaoException.class);

        gameRatingService.deleteUserRating(content);
    }

    @Test
    public void testDeleteUserReview() throws ServiceException {
        when(content.getRequestParameter(GAME_RATING_ID)).thenReturn("1");

        assertTrue(gameRatingService.deleteUserReview(content));
    }

    @Test
    public void testDeleteUserReviewInvalidData() throws ServiceException {
        when(content.getRequestParameter(GAME_RATING_ID)).thenReturn("I");

        assertFalse(gameRatingService.deleteUserReview(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testDeleteUserReviewDaoException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_RATING_ID)).thenReturn("1");
        when(gameRatingDao.delete(eq(1L))).thenThrow(DaoException.class);

        gameRatingService.deleteUserReview(content);
    }

    private void initGameRatingField() {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getRequestParameter(GAMEPLAY_RATING)).thenReturn("10");
        when(content.getRequestParameter(GRAPHICS_RATING)).thenReturn("20");
        when(content.getRequestParameter(SOUND_RATING)).thenReturn("30");
        when(content.getRequestParameter(PLOT_RATING)).thenReturn("40");
        when(content.getRequestParameter(REVIEW)).thenReturn("normal review");
    }
}