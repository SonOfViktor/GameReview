package com.fairycompany.reviewer.controller.command;

public class PagePath {
    public static final String INDEX = "index.jsp";
    public static final String LOGIN = "pages/login.jsp";                               // todo delete
    public static final String MAIN_PAGE = "pages/main.jsp";
    public static final String MAIN_PAGE_REDIRECT = "/controller?command=to_main_page&actual_page=1";
    public static final String SEARCH_REDIRECT = "/controller?command=search_game&actual_page=%s&search_field=%s";
    public static final String CREATE_USER = "pages/create_user.jsp";
    public static final String CREATE_USER_REDIRECT = "/controller?command=to_sing_up_page";
    public static final String CREATE_GAME = "pages/create_game.jsp";
    public static final String CREATE_GAME_REDIRECT = "/controller?command=to_add_game_page";
    public static final String GAME_PAGE = "pages/game.jsp";
    public static final String GAME_PAGE_REDIRECT = "/controller?command=to_game_page&game_id=%s&actual_page=%s";
    public static final String USER_PAGE = "pages/user.jsp";
    public static final String USER_PAGE_REDIRECT = "/controller?command=to_user_page";
    public static final String USER_EDITOR_PAGE = "pages/user_editor.jsp";
    public static final String USER_EDITOR_REDIRECT = "/controller?command=to_user_editor_page";
    public static final String PAYMENT_PAGE = "/pages/payments.jsp";
    public static final String PAYMENT_REDIRECT = "/controller?command=to_payment_page&actual_page=";
    public static final String USER_MANAGER_PAGE = "/pages/user_manager.jsp";
    public static final String USER_MANAGER_REDIRECT = "/controller?command=to_user_manager_page&actual_page=";
    public static final String GAME_MANAGER_PAGE = "/pages/game_manager.jsp";
    public static final String GAME_MANAGER_REDIRECT = "/controller?command=to_game_manager_page&actual_page=";
    public static final String GAME_EDITOR_PAGE = "pages/game_editor.jsp";
    public static final String SHOPPING_CART_PAGE = "pages/shopping_cart.jsp";
    public static final String SHOPPING_CART_REDIRECT = "/controller?command=to_shopping_cart";
    public static final String GAME_EDITOR_PAGE_REDIRECT = "/controller?command=to_game_editor_page&game_id=";
    public static final String LOGOUT_REDIRECT = "/controller?command=logout";

    public static final String EXCEPTION_ERROR = "pages/errors/checked_exception.jsp";
    public static final String EXCEPTION_ERROR_REDIRECT = "/controller?command=to_exception_page";
    public static final String ERROR_404 = "pages/errors/error_404.jsp";

    public static final int ACCESS_ERROR_PAGE_403 = 403;

    private PagePath() {
    }
}
