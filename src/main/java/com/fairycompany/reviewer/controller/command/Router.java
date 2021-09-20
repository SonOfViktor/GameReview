package com.fairycompany.reviewer.controller.command;

public class Router {
    public enum RouterType {
        REDIRECT,
        FORWARD
    }

    private RouterType type = RouterType.FORWARD;
    private String page;


    public Router (String page) {
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
