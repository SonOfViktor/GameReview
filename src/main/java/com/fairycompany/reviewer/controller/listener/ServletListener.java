package com.fairycompany.reviewer.controller.listener;

import com.fairycompany.reviewer.model.pool.ConnectionPool;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;


@WebListener
public class ServletListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().destroyPool();
    }
}
