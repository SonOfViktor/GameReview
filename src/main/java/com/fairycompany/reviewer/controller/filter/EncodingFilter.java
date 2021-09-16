package com.fairycompany.reviewer.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

//todo check work of class

@WebFilter(filterName = "EncodingFilter",
            urlPatterns = "/*",
            initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8")
            })
public class EncodingFilter implements Filter {
    private String code;

    public void init(FilterConfig config) throws ServletException {
        code = config.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String codeRequest = request.getCharacterEncoding();

        if (code != null && !code.equalsIgnoreCase(codeRequest)) {      // зачем code != null
            request.setCharacterEncoding(code);
            response.setCharacterEncoding(code);
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
        code = null;
    }
}
