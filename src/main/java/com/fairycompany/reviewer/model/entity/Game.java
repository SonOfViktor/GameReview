package com.fairycompany.reviewer.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

public class Game extends Entity {
    private long gameId;
    private String name;
    private String publisher;
    private String developer;
    private LocalDate releaseDate;
    private EnumSet<Platform> platforms;
    private EnumSet<Genre> genres;
    private String description;
    private String image;
    private String trailerUrl;
    private BigDecimal price;

    public enum Genre {
        ACTION, SHOOTER, STRATEGY, PLATFORMER, FIGHTING, RACING, RPG, HORROR, SLASHER, MMO, ADVENTURE, QUEST,
        STEALTH, TOP_DOWN, THIRD_PERSON, FIRST_PERSON, TWO_DIMENSIONAL, METROIDVANIA, SOULS_LIKE, OPEN_WORLD
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public EnumSet<Platform> getPlatform() {
        return EnumSet.copyOf(platforms);
    }

    public void setPlatforms(EnumSet<Platform> platform) {
        this.platforms= platform;
    }

    public EnumSet<Genre> getGenres() {
        return EnumSet.copyOf(genres);
    }

    public void setGenres(EnumSet<Genre> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameId != game.gameId) return false;
        if (name != null ? !name.equals(game.name) : game.name != null) return false;
        if (publisher != null ? !publisher.equals(game.publisher) : game.publisher != null) return false;
        if (developer != null ? !developer.equals(game.developer) : game.developer != null) return false;
        if (releaseDate != null ? !releaseDate.equals(game.releaseDate) : game.releaseDate != null) return false;
        if (platforms != null ? !platforms.equals(game.platforms) : game.platforms != null) return false;
        if (genres != null ? !genres.equals(game.genres) : game.genres != null) return false;
        if (description != null ? !description.equals(game.description) : game.description != null) return false;
        if (image != null ? !image.equals(game.image) : game.image != null) return false;
        if (trailerUrl != null ? !trailerUrl.equals(game.trailerUrl) : game.trailerUrl != null) return false;
        return price != null ? price.equals(game.price) : game.price == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (gameId ^ (gameId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (developer != null ? developer.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (platforms != null ? platforms.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (trailerUrl != null ? trailerUrl.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("Game{ ")
                .append("gameId = ").append(gameId)
                .append(", name = ").append(name)
                .append(", publisher = ").append(publisher)
                .append(", developer = ").append(developer)
                .append(", releaseDate = ").append(releaseDate)
                .append(", platforms = ").append(platforms)
                .append(", genres = ").append(genres)
                .append(", description = ").append(description)
                .append(", price = ").append(price)
                .append(" }");

        return builder.toString();
    }

    public static class GameBuilder {
        private Game game;

        public GameBuilder() {
            game = new Game();
        }

        public GameBuilder setGameId(long gameId) {
            game.setGameId(gameId);
            return this;
        }

        public GameBuilder setName(String name) {
            game.setName(name);
            return this;
        }

        public GameBuilder setPublisher(String publisher) {
            game.setPublisher(publisher);
            return this;
        }

        public GameBuilder setDeveloper(String developer) {
            game.setDeveloper(developer);
            return this;
        }

        public GameBuilder setReleaseDate(LocalDate releaseDate) {
            game.setReleaseDate(releaseDate);
            return this;
        }

        public GameBuilder setPlatforms(EnumSet<Platform> platforms) {
            game.setPlatforms(platforms);
            return this;
        }

        public GameBuilder setGenres(EnumSet<Genre> genres) {
            game.setGenres(genres);
            return this;
        }

        public GameBuilder setDescription(String description) {
            game.setDescription(description);
            return this;
        }

        public GameBuilder setImage(String image) {
            game.setImage(image);
            return this;
        }

        public GameBuilder setTrailerUrl(String trailerUrl) {
            game.setTrailerUrl(trailerUrl);
            return this;
        }

        public GameBuilder setPrice(BigDecimal price) {
            game.setPrice(price);
            return this;
        }

        public Game createGame() {
            return game;
        }
    }
}
