package com.fairycompany.reviewer.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

/**
 * Filter that manage encoding for request and response
 */
@WebFilter(filterName = "EncodingFilter",
            urlPatterns = "/*",
            initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8")
            })
public class EncodingFilter implements Filter {
    private String code;

    public void init(FilterConfig config) {
        code = config.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String codeRequest = request.getCharacterEncoding();

        if (code != null && !code.equalsIgnoreCase(codeRequest)) {
            request.setCharacterEncoding(code);
            response.setCharacterEncoding(code);
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
        code = null;
    }
}
