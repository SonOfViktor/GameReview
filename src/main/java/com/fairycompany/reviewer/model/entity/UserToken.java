package com.fairycompany.reviewer.model.entity;


import java.time.LocalDateTime;

public class UserToken extends Entity{
    private long tokenId;
    private long userId;
    private String token;
    private LocalDateTime creationDate;

    public UserToken(long userId, String token, LocalDateTime creationDate) {
        this.userId = userId;
        this.token = token;
        this.creationDate = creationDate;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
