package com.fairycompany.reviewer.model.entity;

import java.util.List;

public class GameRating extends Entity {
    private long gameRatingId;
    private long userId;
    private long gameId;
    private int gameplayRating;
    private int graphicsRating;
    private int soundRating;
    private int plotRating;
    private String review;

    private GameRating(long gameRatingId, long userId, long gameId, int gameplayRating, int graphicsRating,
                      int soundRating, int plotRating, String review) {
        this.gameRatingId = gameRatingId;
        this.userId = userId;
        this.gameId = gameId;
        this.gameplayRating = gameplayRating;
        this.graphicsRating = graphicsRating;
        this.soundRating = soundRating;
        this.plotRating = plotRating;
        this.review = review;
    }

    public long getGameRatingId() {
        return gameRatingId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getGameplayRating() {
        return gameplayRating;
    }

    public void setGameplayRating(int gameplayRating) {
        this.gameplayRating = gameplayRating;
    }

    public int getGraphicsRating() {
        return graphicsRating;
    }

    public void setGraphicsRating(int graphicsRating) {
        this.graphicsRating = graphicsRating;
    }

    public int getSoundRating() {
        return soundRating;
    }

    public void setSoundRating(int soundRating) {
        this.soundRating = soundRating;
    }

    public int getPlotRating() {
        return plotRating;
    }

    public void setPlotRating(int plotRating) {
        this.plotRating = plotRating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRating that = (GameRating) o;

        if (gameRatingId != that.gameRatingId) return false;
        if (userId != that.userId) return false;
        if (gameId != that.gameId) return false;
        if (gameplayRating != that.gameplayRating) return false;
        if (graphicsRating != that.graphicsRating) return false;
        if (soundRating != that.soundRating) return false;
        if (plotRating != that.plotRating) return false;
        return review != null ? review.equals(that.review) : that.review == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (gameRatingId ^ (gameRatingId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        result = 31 * result + gameplayRating;
        result = 31 * result + graphicsRating;
        result = 31 * result + soundRating;
        result = 31 * result + plotRating;
        result = 31 * result + (review != null ? review.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("GameRating { ")
                .append("gameRatingId = ").append(gameRatingId)
                .append(", userId = ").append(userId)
                .append(", gameId = ").append(gameId)
                .append(", gameplayRating = ").append(gameplayRating)
                .append(", graphicsRating = ").append(graphicsRating)
                .append(", soundRating = ").append(soundRating)
                .append(", plotRating = ").append(plotRating)
                .append(", review = ").append(review)
                .append(" }");

        return builder.toString();
    }

    public static class GameReviewBuilder {

        private long gameRatingId;
        private long userId;
        private long gameId;
        private int gameplayRating;
        private int graphicsRating;
        private int soundRating;
        private int plotRating;
        private String review;

        public GameReviewBuilder setGameRatingId(long gameRatingId) {
            this.gameRatingId = gameRatingId;
            return this;
        }

        public GameReviewBuilder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public GameReviewBuilder setGameId(long gameId) {
            this.gameId = gameId;
            return this;
        }

        public GameReviewBuilder setGameplayRating(int gameplayRating) {
            this.gameplayRating = gameplayRating;
            return this;
        }

        public GameReviewBuilder setGraphicsRating(int graphicsRating) {
            this.graphicsRating = graphicsRating;
            return this;
        }

        public GameReviewBuilder setSoundRating(int soundRating) {
            this.soundRating = soundRating;
            return this;
        }

        public GameReviewBuilder setPlotRating(int plotRating) {
            this.plotRating = plotRating;
            return this;
        }

        public GameReviewBuilder setReview(String review) {
            this.review = review;
            return this;
        }

        public GameRating createGameRating() {
            return new GameRating(gameRatingId, userId, gameId, gameplayRating, graphicsRating, soundRating,
                    plotRating, review);
        }
    }
}
