package com.fairycompany.reviewer.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter(filterName = "PageFilter", urlPatterns = "*.jsp")
public class PageAddressFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String current = httpRequest.getContextPath();
//        System.out.println(current);
        int currentLength = current.length();
        String uri = httpRequest.getRequestURI();
//        System.out.println(uri);
        current = uri.substring(currentLength);

        logger.log(Level.DEBUG, "Address of this page is {} ", current);

        httpRequest.getSession().setAttribute("current_page", current);
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}