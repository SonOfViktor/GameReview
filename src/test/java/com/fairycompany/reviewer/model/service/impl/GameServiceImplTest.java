package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.util.ServiceUtil;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class GameServiceImplTest {
    MockitoSession mockito;

    @Spy
    private SessionRequestContent content;

    @Spy
    private ServiceUtil serviceUtil;

    @Mock
    private GameDao gameDao;

    @Mock
    private TransactionManager transactionManager;

    @InjectMocks
    private GameService gameService = GameServiceImpl.getInstance();

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
    public void testFindAllGamesWithRating() throws DaoException, ServiceException {
        List games = createGamesWithRating();
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.findAllGamesWithRating(anyLong(), anyInt())).thenReturn(games);
        when(gameDao.findTotalGameAmount()).thenReturn(9L);

        List actualGames = gameService.findAllGamesWithRating(content);
        long actualPageAmount = (Long) content.getRequestAttribute(RequestAttribute.PAGE_AMOUNT);
        long actualActualPage = (Long) content.getRequestAttribute(RequestAttribute.ACTUAL_PAGE);

        assertEquals(actualGames, games);
        assertEquals(actualPageAmount, 3);
        assertEquals(actualActualPage, 1);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindAllGamesWithRatingDaoFindGamesException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.findAllGamesWithRating(0, 3)).thenThrow(DaoException.class);

        gameService.findAllGamesWithRating(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindAllGamesWithRatingGameAmountException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.findTotalGameAmount()).thenThrow(DaoException.class);

        gameService.findAllGamesWithRating(content);
    }

    @Test
    public void testSearchGamesWithRating() throws DaoException, ServiceException {
        List games = createGamesWithRating();
        when(content.getRequestParameter(SEARCH_FIELD)).thenReturn("Super");
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.searchGamesWithRating(eq("Super%"), anyLong(), anyInt())).thenReturn(games);
        when(gameDao.findSearchedGameAmount(anyString())).thenReturn(9L);

        List actualGames = gameService.findSearchedGamesWithRating(content);
        long actualPageAmount = (Long) content.getRequestAttribute(RequestAttribute.PAGE_AMOUNT);
        long actualActualPage = (Long) content.getRequestAttribute(RequestAttribute.ACTUAL_PAGE);

        assertEquals(actualGames, games);
        assertEquals(actualPageAmount, 3);
        assertEquals(actualActualPage, 1);
    }

    @Test
    public void testSearchGamesInvalidSearchField() throws DaoException, ServiceException {
        List games = createGamesWithRating();
        String expectedMessage = "add_game_name_invalid_message";
        when(content.getRequestParameter(SEARCH_FIELD)).thenReturn("<script>alert(Hi)</script>");
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.searchGamesWithRating(eq("%"), anyLong(), anyInt())).thenReturn(games);
        when(gameDao.findSearchedGameAmount(anyString())).thenReturn(9L);

        List actualGames = gameService.findSearchedGamesWithRating(content);
        String actualMessage = (String) content.getSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR);
        long actualPageAmount = (Long) content.getRequestAttribute(RequestAttribute.PAGE_AMOUNT);
        long actualActualPage = (Long) content.getRequestAttribute(RequestAttribute.ACTUAL_PAGE);

        assertEquals(actualGames, games);
        assertEquals(actualMessage, expectedMessage);
        assertEquals(actualPageAmount, 3);
        assertEquals(actualActualPage, 1);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testSearchGamesDaoSearchGamesException() throws DaoException, ServiceException {
        when(content.getRequestParameter(SEARCH_FIELD)).thenReturn("Super");
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("2");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.searchGamesWithRating(anyString(), anyLong(), anyInt())).thenThrow(DaoException.class);

        gameService.findSearchedGamesWithRating(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testSearchGamesDaoFindGameAmountException() throws DaoException, ServiceException {
        when(content.getRequestParameter(SEARCH_FIELD)).thenReturn("Super");
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("2");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(gameDao.findSearchedGameAmount(anyString())).thenThrow(DaoException.class);

        gameService.findSearchedGamesWithRating(content);
    }

    @Test
    public void testFindGame() throws DaoException, ServiceException {
        Optional<Game> game = Optional.of(new Game.GameBuilder().setGameId(1).createGame());
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(gameDao.findEntityById(eq(1L))).thenReturn(game);

        Optional<Game> actualGame = gameService.findGame(content);

        assertEquals(actualGame, game);
    }

    @Test
    public void testFindGameInvalidGameId() throws DaoException, ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("Hello world");

        Optional<Game> actualGame = gameService.findGame(content);

        assertTrue(actualGame.isEmpty());
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindGameDaoException() throws DaoException, ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(gameDao.findEntityById(eq(1L))).thenThrow(DaoException.class);

        gameService.findGame(content);
    }

    @Test
    public void testAddGame() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"ACTION", "SHOOTER"});
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.FILE_EXTENSION)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM)).thenReturn(Optional.empty());
        when(gameDao.findGameName(anyString())).thenReturn(List.of());
        doNothing().when(serviceUtil).makeDir(anyString());
        doReturn("").when(serviceUtil).saveDefaultImage(anyString(), anyString(), anyString());

        assertTrue(gameService.addGame(content));
        verify(content).addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.ADD_GAME_SUCCESSFUL);
    }

    @Test
    public void testAddGameInvalidGameData() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{});
        when(content.getRequestParameter(PRICE)).thenReturn("9.999");

        assertFalse(gameService.addGame(content));
        verify(content).addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_FAILED);
    }

    @Test
    public void testAddGameInvalidGenres() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{});

        assertFalse(gameService.addGame(content));
        verify(content).addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_FAILED);
    }

    @Test
    public void testAddExistGame() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"ACTION", "SHOOTER"});
        when(gameDao.findGameName(anyString())).thenReturn(List.of(Map.of("name", "Doom")));

        assertFalse(gameService.addGame(content));
        verify(content).addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.GAME_ALREADY_EXIST);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddGameDaoFindGame() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"ACTION", "SHOOTER"});
        when(gameDao.findGameName(anyString())).thenThrow(DaoException.class);

        gameService.addGame(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddGameDaoAddException() throws DaoException, ServiceException {
        initGameData();
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"ACTION", "SHOOTER"});
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.FILE_EXTENSION)).thenReturn("");
        when(content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM)).thenReturn(Optional.empty());
        when(gameDao.findGameName(anyString())).thenReturn(List.of());
        doNothing().when(serviceUtil).makeDir(anyString());
        doReturn("").when(serviceUtil).saveDefaultImage(anyString(), anyString(), anyString());
        when(gameDao.add(any(Game.class))).thenThrow(DaoException.class);

        gameService.addGame(content);
    }

    @Test
    public void testAddGameToShoppingCart() throws ServiceException, DaoException {
        when(content.getRequestParameter(PLATFORM)).thenReturn("PLAYSTATION_4");
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(gameDao.findEntityById(eq(1L))).thenReturn(Optional.of(new Game.GameBuilder().setName("Doom").createGame()));
        Map<Order, Game> shoppingCart = new HashMap<>();
        shoppingCart.put(new Order(), new Game());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);

        gameService.addGameToShoppingCart(content);

        verify(content).getSessionAttribute(SessionAttribute.SHOPPING_CART);
    }

    @Test
    public void testAddGameToShoppingCartInvalidData() throws ServiceException, DaoException {
        when(content.getRequestParameter(PLATFORM)).thenReturn("playstation_4");
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(gameDao.findEntityById(eq(1L))).thenReturn(Optional.of(new Game.GameBuilder().setName("Doom").createGame()));

        gameService.addGameToShoppingCart(content);

        verify(content, never()).getSessionAttribute(SessionAttribute.SHOPPING_CART);
    }

    @Test
    public void testDeleteGameFromShoppingCart() {
        when(content.getRequestParameter(GAME_NAME)).thenReturn("Doom");
        when(content.getRequestParameter(PLATFORM)).thenReturn("PLAYSTATION_4");
        Map<Order, Game> shoppingCart = new HashMap<>();
        shoppingCart.put(new Order(), new Game());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);

        gameService.deleteGameFromShoppingCart(content);

        verify(content).getSessionAttribute(SessionAttribute.SHOPPING_CART);
    }

    @Test
    public void testDeleteGameFromShoppingCartInvalidData() {
        when(content.getRequestParameter(GAME_NAME)).thenReturn("<Doom>");
        when(content.getRequestParameter(PLATFORM)).thenReturn("PLAYSTATION_4");

        gameService.deleteGameFromShoppingCart(content);

        verify(content, never()).getSessionAttribute(SessionAttribute.SHOPPING_CART);
    }

    @Test
    public void testUpdateGame() throws ServiceException, DaoException {
        initGameData();
        when(content.getRequestParameter(GAME_ID)).thenReturn("202");

        assertTrue(gameService.updateGame(content));
        verify(gameDao).update(any(Game.class));
    }

    @Test
    public void testUpdateGameInvalidData() throws ServiceException, DaoException {
        initGameData();
        when(content.getRequestParameter(GAME_NAME)).thenReturn("<script>");
        when(content.getRequestParameter(GAME_ID)).thenReturn("202");

        assertFalse(gameService.updateGame(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdateGameDaoException() throws ServiceException, DaoException {
        initGameData();
        when(content.getRequestParameter(GAME_ID)).thenReturn("202");
        when(gameDao.update(any(Game.class))).thenThrow(DaoException.class);

        gameService.updateGame(content);
    }

    @Test
    public void testUpdateGenres() throws ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("5");
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"RPG", "MMO"});

        assertTrue(gameService.updateGenres(content));
    }

    @Test
    public void testUpdateGenresInvalidData() throws ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("5");
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"GENRE", "MMO"});

        assertFalse(gameService.updateGenres(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdateGenresDaoDeleteGenreException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("5");
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"RPG", "MMO"});
        when(gameDao.deleteGenres(anyLong())).thenThrow(DaoException.class);

        gameService.updateGenres(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testUpdateGenresDaoAddGenreException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("5");
        when(content.getRequestParameterValues(GENRE)).thenReturn(new String[]{"RPG", "MMO"});
        when(gameDao.addGenres(anyLong(), any(EnumSet.class))).thenThrow(DaoException.class);

        gameService.updateGenres(content);
    }

    @Test
    public void testDeleteGame() throws ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestParameter(IMAGE)).thenReturn("media\\game\\Default game.jpg");

        assertTrue(gameService.deleteGame(content));
    }

    @Test
    public void testDeleteGameInvalidData() throws ServiceException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestParameter(IMAGE)).thenReturn("media\\people\\Default game.jpg");

        assertFalse(gameService.deleteGame(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testDeleteGameDaoException() throws ServiceException, DaoException {
        when(content.getRequestParameter(GAME_ID)).thenReturn("1");
        when(content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY)).thenReturn("");
        when(content.getRequestParameter(IMAGE)).thenReturn("media\\game\\Default game.jpg");
        when(gameDao.delete(anyLong())).thenThrow(DaoException.class);

        gameService.deleteGame(content);
    }

    private List<Map<String, Object>> createGamesWithRating() {
        List games = List.of(Map.of("game", new Game.GameBuilder().setName("Super Mario")),
                Map.of("total_rating", 100),
                Map.of("game", new Game.GameBuilder().setName("Super Contra")),
                Map.of("total_rating", 3),
                Map.of("game", new Game.GameBuilder().setName("Super Chip and Dale")),
                Map.of("total_rating", 50));

        return games;
    }

    private void initGameData() {
        when(content.getRequestParameter(GAME_NAME)).thenReturn("Doom");
        when(content.getRequestParameter(PUBLISHER)).thenReturn("Bethesda");
        when(content.getRequestParameter(DEVELOPER)).thenReturn("Id Software");
        when(content.getRequestParameterValues(PLATFORM)).thenReturn(new String[]{"PLAYSTATION_4", "PC"});
        when(content.getRequestParameter(RELEASE_DATE)).thenReturn("2015-11-11");
        when(content.getRequestParameter(PRICE)).thenReturn("9.99");
        when(content.getRequestParameter(YOUTUBE_URL)).thenReturn("https://www.youtube.com/watch?v=S0AwwvtwDyk");
        when(content.getRequestParameter(GAME_DESCRIPTION)).thenReturn("asdfjkl32349safdasdf");
    }
}