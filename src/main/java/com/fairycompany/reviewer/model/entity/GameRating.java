package com.fairycompany.reviewer.model.entity;

import java.time.LocalDateTime;

public class GameRating extends Entity {
    private long gameRatingId;
    private long userId;
    private long gameId;
    private int gameplayRating;
    private int graphicsRating;
    private int soundRating;
    private int plotRating;
    private String review;
    private LocalDateTime publicationDate;

    public long getGameRatingId() {
        return gameRatingId;
    }

    public void setGameRatingId(long gameRatingId) {
        this.gameRatingId = gameRatingId;
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

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRating rating = (GameRating) o;

        if (gameRatingId != rating.gameRatingId) return false;
        if (userId != rating.userId) return false;
        if (gameId != rating.gameId) return false;
        if (gameplayRating != rating.gameplayRating) return false;
        if (graphicsRating != rating.graphicsRating) return false;
        if (soundRating != rating.soundRating) return false;
        if (plotRating != rating.plotRating) return false;
        if (review != null ? !review.equals(rating.review) : rating.review != null) return false;
        return publicationDate != null ? publicationDate.equals(rating.publicationDate) : rating.publicationDate == null;
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
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
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
                .append(", publication date = ").append(publicationDate)
                .append(" }");

        return builder.toString();
    }

    public static class GameRatingBuilder {
        private GameRating rating;

        public GameRatingBuilder() {
            rating = new GameRating();
        }

        public GameRatingBuilder setGameRatingId(long gameRatingId) {
            rating.setGameRatingId(gameRatingId);
            return this;
        }

        public GameRatingBuilder setUserId(long userId) {
            rating.setUserId(userId);
            return this;
        }

        public GameRatingBuilder setGameId(long gameId) {
            rating.setGameId(gameId);
            return this;
        }

        public GameRatingBuilder setGameplayRating(int gameplayRating) {
            rating.setGameplayRating(gameplayRating);
            return this;
        }

        public GameRatingBuilder setGraphicsRating(int graphicsRating) {
            rating.setGraphicsRating(graphicsRating);
            return this;
        }

        public GameRatingBuilder setSoundRating(int soundRating) {
            rating.setSoundRating(soundRating);
            return this;
        }

        public GameRatingBuilder setPlotRating(int plotRating) {
            rating.setPlotRating(plotRating);
            return this;
        }

        public GameRatingBuilder setReview(String review) {
            rating.setReview(review);
            return this;
        }

        public GameRatingBuilder setPublicationDate(LocalDateTime publicationDate) {
            rating.setPublicationDate(publicationDate);
            return this;
        }

        public GameRating createGameRating() {
            return rating;
        }
    }
}
