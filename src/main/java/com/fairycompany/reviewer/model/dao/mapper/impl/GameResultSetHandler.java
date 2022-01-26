package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameResultSetHandler implements ResultSetHandler<Game> {
    private static final String COMMA_REGEX = ",";

    @Override
    public Game resultSetToObject(ResultSet resultSet) throws SQLException {
        Game game = new Game.GameBuilder()
                .setGameId(resultSet.getLong(GAME_ID))
                .setName(resultSet.getString(NAME))
                .setPublisher(resultSet.getString(PUBLISHER))
                .setDeveloper(resultSet.getString(DEVELOPER))
                .setReleaseDate(resultSet.getDate(RELEASE_DATE).toLocalDate())
                .setDescription(resultSet.getString(DESCRIPTION))
                .setImage(resultSet.getString(IMAGE))
                .setPrice(resultSet.getBigDecimal(PRICE))
                .setTrailerUrl(resultSet.getString(TRAILER_URL))
                .setPlatforms(makeEnumSet(resultSet.getString(PLATFORM).split(COMMA_REGEX), Platform.class))
                .setGenres(makeEnumSet(resultSet.getString(GENRE).split(COMMA_REGEX), Game.Genre.class))
                .createGame();
        return game;
    }

    private <T extends Enum<T>> EnumSet<T> makeEnumSet(String[] array, Class<T> enumClass) {
        Set<T> set = Arrays.stream(array).map(s -> Enum.valueOf(enumClass, s.toUpperCase())).collect(Collectors.toSet());
        return EnumSet.copyOf(set);
    }
}
