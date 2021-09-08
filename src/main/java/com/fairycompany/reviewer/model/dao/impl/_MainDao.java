package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.entity.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class _MainDao {
    public static void main(String[] args) throws DaoException, SQLException {
//        UserDaoImpl dao = UserDaoImpl.getInstance();
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

//        User newUser = new User("Ide", "Cool", "Man", LocalDate.now(), 123098456,
//                new BigDecimal(0.00), null, User.UserRole.GUEST, User.UserStatus.BANNED);
//        dao.add(newUser, "zxc123asd");
//        dao.updatePassword(newUser, "zxc123asd");

//        List<User> users = dao.findAll();
//        for (User user : users) {
//            System.out.println(user);
//        }
//        List<String> strings = new ArrayList<>();
//        String string = strings.get(0);
//        System.out.println(string);


        GameDaoImpl gameDao = new GameDaoImpl();
//        Set<Platform> platforms = gameDao.getPlatform(2);
//        System.out.println(platforms);

//        Set<Game.Genre> genres = gameDao.getGenre(2);
//        System.out.println(genres);

        List<Game> games = gameDao.findAll();
        System.out.println(games);

    }
}
