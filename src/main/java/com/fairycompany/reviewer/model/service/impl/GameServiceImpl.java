package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.GameDaoImpl;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.util.ServiceUtil;
import com.fairycompany.reviewer.model.validator.GameValidator;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class GameServiceImpl implements GameService {
    private static final String RELATIVE_IMAGE_PATH = "media" + File.separator + "game" + File.separator;
    private static final Logger logger = LogManager.getLogger();
    private GameDao gameDao = GameDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private static GameServiceImpl instance = new GameServiceImpl();

    private GameServiceImpl() {
    }

    public static GameServiceImpl getInstance(){
        return instance;
    }

    public List<Map<String, Object>> findAllGamesForMainPage(SessionRequestContent content) throws ServiceException {

        List<Map<String, Object>> games;
        try {
            transactionManager.initTransaction();

            games = gameDao.findAllGamesWithRating();

//            for (Object platforms: games.get(ColumnName.PLATFORM)) {
//                System.out.println(platforms);
//            }

//            List<EnumSet<Platform>> platforms = games.get(ColumnName.PLATFORM).stream()
//                    .map(platform -> makeEnumSet(platform, Platform.class))
//                    .toList();
//            System.out.println(platforms);
//            content.addRequestAttribute(RequestAttribute.PLATFORMS, platforms);

//            content.addRequestAttribute(RequestAttribute.GAME_LIST, games);           // todo i need to think
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finding games, {}", e.getMessage());
            throw new ServiceException("Error when finding games", e);
        } finally {
            transactionManager.endTransaction();
        }

        return games;
    }

    public boolean addGame(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        boolean isGameAdded = false;

        String name = content.getRequestParameter(GAME_NAME);
        String publisher = content.getRequestParameter(PUBLISHER);
        String developer = content.getRequestParameter(DEVELOPER);
        String[] platforms = content.getRequestParameterValues(PLATFORM);
        LocalDate releaseDate = serviceUtil.getDateFromString(content.getRequestParameter(RELEASE_DATE));
        BigDecimal price = new BigDecimal(content.getRequestParameter(PRICE));
        String[] genres = content.getRequestParameterValues(GENRE);
        String youtubeUrlRaw = content.getRequestParameter(YOUTUBE_URL);
        String youtubeUrl = youtubeUrlRaw.replace("watch?v=","embed/").concat("?&autoplay=1");
        String description = content.getRequestParameter(GAME_DESCRIPTION);
        String imageUploadDirectory = (String) content.getRequestAttribute(RequestAttribute.IMAGE_UPLOAD_DIRECTORY);
        Part part = (Part) content.getRequestAttribute(RequestAttribute.PART);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isStringFieldValid(name) && validator.isStringFieldValid(publisher) &&
            validator.isStringFieldValid(developer) && validator.isCheckBoxDataValid(platforms) &&
            validator.isReleaseDateValid(releaseDate) && validator.isPriceValid(price) &&
            validator.isCheckBoxDataValid(genres) && validator.isYoutubeUrlValid(youtubeUrl) &&
            validator.isDescriptionValid(description)) {

            String image = serviceUtil.saveImage(imageUploadDirectory, part, RELATIVE_IMAGE_PATH, name);

            Game game = new Game.GameBuilder()
                    .setName(name)
                    .setPublisher(publisher)
                    .setDeveloper(developer)
                    .setPlatforms(makeEnumSet(platforms, Platform.class))
                    .setReleaseDate(releaseDate)
                    .setPrice(price)
                    .setTrailerUrl(youtubeUrl)
                    .setDescription(description)
                    .setImage(image)
                    .createGame();

            try {
                transactionManager.initTransaction();

                long gameId = gameDao.add(game);

                gameDao.addGenres(gameId, makeEnumSet(genres, Game.Genre.class));

                isGameAdded = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when adding game {}, {}", name, e.getMessage());
                throw new ServiceException("Error when adding game " + name, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isGameAdded;
    }

    private static <T extends Enum<T>> EnumSet<T> makeEnumSet(String[] array, Class<T> enumClass) {
        Set<T> set = Arrays.stream(array).map(s -> Enum.valueOf(enumClass, s.toUpperCase())).collect(Collectors.toSet());
        return EnumSet.copyOf(set);
    }

}
