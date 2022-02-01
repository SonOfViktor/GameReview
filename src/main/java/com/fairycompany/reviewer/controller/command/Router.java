package com.fairycompany.reviewer.controller.command;


/**
 * Class contain location URL and the way of transition to this location
 */
public class Router {
    /**
     * Type of transition
     */
    public enum RouterType {
        REDIRECT,
        FORWARD
    }

    private RouterType type = RouterType.FORWARD;
    private String page;


    /**
     * Instantiates a new Router. Default transition type is forward
     *
     * @param page location URL
     */
    public Router (String page) {
        this.page = page;
    }

    /**
     * Gets transition type.
     *
     * @return transition type
     */
    public RouterType getType() {
        return type;
    }

    /**
     * Sets transition type.
     *
     * @param type transition type
     */
    public void setType(RouterType type) {
        this.type = type;
    }

    /**
     * Gets location page.
     *
     * @return location page
     */
    public String getPage() {
        return page;
    }

    /**
     * Sets location page.
     *
     * @param page location page
     */
    public void setPage(String page) {
        this.page = page;
    }
}
