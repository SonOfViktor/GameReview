package com.fairycompany.reviewer.controller.command;

public class PagePath {
    public static final String INDEX = "index.jsp";
    public static final String LOGIN = "pages/login.jsp";                               // todo delete
    public static final String MAIN_PAGE = "";
    public static final String MAIN_PAGE_REDIRECT = "/controller?command=to_main_page";
    public static final String CREATE_USER = "pages/create_user.jsp";
    public static final String CREATE_USER_REDIRECT = "/controller?command=to_sing_up_page";
    public static final String EXCEPTION_ERROR = "pages/errors/checked_exception.jsp";
    public static final String EXCEPTION_ERROR_REDIRECT = "/controller?command=to_exception_page";
    public static final String ERROR_404 = "pages/errors/error_404.jsp";

    public static final int ACCESS_ERROR_PAGE_403 = 403;

    private PagePath() {
    }
}
