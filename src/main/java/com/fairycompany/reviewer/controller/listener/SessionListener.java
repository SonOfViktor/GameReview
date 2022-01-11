package com.fairycompany.reviewer.controller.listener;

import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.model.entity.Game;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.ArrayList;
import java.util.List;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final String DEFAULT_LOCALE = "en_EN";
    private static final int ROW_AMOUNT = 5;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute(SessionAttribute.SESSION_LOCALE, DEFAULT_LOCALE);
        session.setAttribute(SessionAttribute.ROW_AMOUNT, ROW_AMOUNT);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

}
