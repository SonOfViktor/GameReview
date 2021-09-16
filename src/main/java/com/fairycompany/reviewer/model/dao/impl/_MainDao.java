package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.entity.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class _MainDao {
    public static void main(String[] args) throws DaoException, SQLException {
        UserDaoImpl userDao = UserDaoImpl.getInstance();
//
//        User user = dao.findByLoginAndPassword("punksim@mail.ru", "zxc123asd").get();
//        System.out.println(user);
//        User newUser = new User.UserBuilder()
//                .setLogin("Maksimus")
//                .setFirstName("Maks")
//                .setSecondName("Sil")
//                .setBirthday(LocalDate.now())
//                .setPhone(12345563)
//                .setBalance(new BigDecimal("100.00"))
//                .setUserRole(User.Role.GUEST)
//                .setUserStatus(User.Status.GRAND_MASTER)
//                .createUser();
//        System.out.println(newUser);
//
//
//        userDao.add(newUser, "zxc123asd");
//        dao.updatePassword(newUser, "zxc123asd");

//        List<User> users = dao.findAll();
//        for (User user : users) {
//            System.out.println(user);
//        }
//        List<String> strings = new ArrayList<>();
//        String string = strings.get(0);
//        System.out.println(string);

//        userDao.delete(7);

        // #game dao
        GameDaoImpl gameDao = GameDaoImpl.getInstance();
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
        Set<Platform> platforms = Set.of(Platform.XBOX_ONE, Platform.PC, Platform.PLAYSTATION_4);
        Set<Game.Genre> genres = Set.of(Game.Genre.HORROR, Game.Genre.ACTION, Game.Genre.SHOOTER);
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
        System.out.println(gameDao.add(DeadSpace));
    }
}
