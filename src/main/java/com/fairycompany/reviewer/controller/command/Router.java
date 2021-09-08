package com.fairycompany.reviewer.controller.command;

public class Router {
    public enum RouterType {
        REDIRECT,
        FORWARD
    }

    private RouterType type;
    private String page;


    public Router (RouterType type, String page) {
        this.type = type;
        this.page = page;
    }

    public RouterType getType() {
        return type;
    }

    public void setType(RouterType type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
