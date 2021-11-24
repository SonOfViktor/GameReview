package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.mapper.impl.GameResultSetHandler;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.entity.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;
import static com.fairycompany.reviewer.model.dao.ColumnName.TOTAL_RATING;

public class _MainDao {
    public static void main(String[] args) throws DaoException, SQLException, IOException {
//        UserDaoImpl userDao = UserDaoImpl.getInstance();
//
//        User user = userDao.findByLoginAndPassword("punksim@mail.ru", "zxc123asd").get();
//        System.out.println(user);
//        User newUser = new User.UserBuilder()
//                .setLogin("SonOfViktor@yandex.ru")
//                .setFirstName("Максим")
//                .setSecondName("Силевич")
//                .setBirthday(LocalDate.of(1988,02,11))
//                .setPhone(295173826)
//                .setBalance(new BigDecimal("0"))
//                .setUserRole(User.Role.USER)
//                .setUserStatus(User.Status.NOT_CONFIRMED)
//                .createUser();
//        System.out.println(newUser);
////
////
//        userDao.add(newUser, "zxc123asd");
//        userDao.updatePassword(newUser, "zxc123asd");

//        List<User> users = dao.findAll();
//        for (User user : users) {
//            System.out.println(user);
//        }
//        List<String> strings = new ArrayList<>();
//        String string = strings.get(0);
//        System.out.println(string);

//        userDao.delete(7);

        // #game dao
        GameDao gameDao = GameDaoImpl.getInstance();
//        Set<Platform> platforms = gameDao.getPlatform(2);
//        System.out.println(platforms);

//        Set<Game.Genre> genres = gameDao.getGenre(2);
//        System.out.println(genres);

        // find all
//        List<Game> games = gameDao.findAll();
//        for (Game game : games) {
//            System.out.println(game);
//        }

//        Optional<Game> game = gameDao.findGameByName("dooM");
//        System.out.println(game);

//         delete
//        System.out.println(gameDao.delete(3));

        // add
        EnumSet<Platform> platforms = EnumSet.of(Platform.XBOX_ONE, Platform.PC, Platform.PLAYSTATION_4);
        EnumSet<Game.Genre> genres = EnumSet.of(Game.Genre.HORROR, Game.Genre.ACTION, Game.Genre.SHOOTER);
//        System.out.println(gameDao.stringFromSet(platforms));
        Game DeadSpace = new Game.GameBuilder()
                .setName("Dead Space")
                .setPublisher("Electronic Arts")
                .setDeveloper("Valotion Games")
                .setReleaseDate(LocalDate.of(2008, 05, 28))
                .setPlatforms(platforms)
                .setGenres(genres)
                .setDescription("Очень страшный хорор")
                .setTrailerUrl("https://www.youtube.com/watch?v=RYaJCmJgb9A")
                .setPrice(new BigDecimal(10))
                .createGame();

//        System.out.println(DeadSpace.getClass().getSimpleName());
//        System.out.println(gameDao.add(DeadSpace));

        // Check Map methods

        final String FIND_ALL_GAMES_WITH_RATING = """
            SELECT games.*, GROUP_CONCAT(genre SEPARATOR ',') AS genre, total_rating FROM game_rating.games
            LEFT JOIN total_game_rating ON games.game_id = total_game_rating.game_id
            JOIN games_genres ON games_genres.game_id = games.game_id
            JOIN genres ON games_genres.genre_id = genres.genre_id
            GROUP BY games_genres.game_id
            ORDER BY name
            """;

        Set<String> columns;
        columns = Set.of(TOTAL_RATING);
        JdbcTemplate<Game> template = new JdbcTemplate<>(new GameResultSetHandler());

        TransactionManager transactionManager = TransactionManager.getInstance();
        transactionManager.initTransaction();

//        System.out.println("SilMethod");
//        Map<String, List<Object>> silMethod = template.executeSelectQueryFromTables(FIND_ALL_GAMES_MAIN_PAGE_SQL, columns);
//
//        int i = 0;
//        for(Object name : silMethod.get("name")) {
//            Object image = silMethod.get("release_date").get(i);
//            System.out.println(name);
//            System.out.println(image);
//            i++;
//        }




//        Iterator<Map.Entry<String, List<Object>>> iterator = silMethod.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, List<Object>> pair = iterator.next();
//            System.out.println(pair.getKey() + " " + pair.getValue());
//        }
//
        System.out.println("SolMethod");
        List<Map<String, Object>> list = template.executeSelectForList(FIND_ALL_GAMES_WITH_RATING, columns);
//        Map<String, Object> game = list.stream()
//                .filter(t -> {
//                    Game gameFromMap = (Game) t.get("game");
//                    return gameFromMap.getGameId() == 10;
//                }).findFirst()
//                .get();

        Map<String, Object> game = list.stream()
                .filter(t -> Game.class.cast(t.get("game")).getGameId() == 10)
                .findFirst()
                .get();


//        for (Map<String, Object> map : list) {
//            Game gameFromList = (Game) map.get("game");
//            if (gameFromList.getGameId() == 2) {
//                game = map;
//            }
//        }



        System.out.println(game);

//        for (Map<String, Object> map: solMethod) {
//            System.out.println(map);
//        }
    }

//    public static int getMemoryLength(Object object) throws java.io.IOException
//    {
//        ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
//        objectOutputStream.writeObject(object);
//        objectOutputStream.flush();
//        objectOutputStream.close();
//        byteObject.close();
//
//        return byteObject.toByteArray().length;
//    }
}
