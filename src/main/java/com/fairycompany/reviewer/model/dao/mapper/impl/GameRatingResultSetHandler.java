package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.ColumnName;
import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.GameRating;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameRatingResultSetHandler implements ResultSetHandler<GameRating> {
    @Override
    public GameRating resultSetToObject(ResultSet resultSet) throws SQLException {
        GameRating rating = new GameRating.GameRatingBuilder()
                .setGameRatingId(resultSet.getLong(GAME_RATING_ID))
                .setUserId(resultSet.getLong(GAME_RATING_USER_ID))
                .setGameId(resultSet.getLong(GAME_RATING_GAME_ID))
                .setGameplayRating(resultSet.getInt(GAMEPLAY_RATING))
                .setGraphicsRating(resultSet.getInt(GRAPHICS_RATING))
                .setSoundRating(resultSet.getInt(SOUND_RATING))
                .setPlotRating(resultSet.getInt(PLOT_RATING))
                .setReview(resultSet.getString(REVIEW))
                .setPublicationDate(resultSet.getTimestamp(PUBLICATION_DATE).toLocalDateTime())
                .createGameRating();
        return rating;
    }
}
