package com.fairycompany.reviewer.controller.filter;

import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter that checked provided localization
 */
@WebFilter(filterName = "LocaleFilter",
        urlPatterns = "/controller",
        initParams = {
                @WebInitParam(name = "default_locale", value = "en_EN"),
                @WebInitParam(name = "locale_regex", value = "\\p{Lower}{2}_\\p{Upper}{2}"),
        })
public class LocaleFilter implements Filter {
    private String localeRegex;
    private String defaultLocale;

    public void init(FilterConfig config) {
        defaultLocale = config.getInitParameter("default_locale");
        localeRegex = config.getInitParameter("locale_regex");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String locale = (String) session.getAttribute(SessionAttribute.SESSION_LOCALE);

        if (locale == null || !locale.matches(localeRegex)) {
            session.setAttribute(SessionAttribute.SESSION_LOCALE, defaultLocale);
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
