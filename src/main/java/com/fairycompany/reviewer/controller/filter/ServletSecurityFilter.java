package com.fairycompany.reviewer.controller.filter;

import com.fairycompany.reviewer.controller.command.CommandType;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "ServletSecurityFilter",
            urlPatterns = "/controller",
            initParams = { @WebInitParam(name = "index_path", value = "index.jsp") })
public class ServletSecurityFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String command = request.getParameter(RequestParameter.COMMAND);
        CommandType type = CommandType.valueOf(command.toUpperCase());
        User user = (User) session.getAttribute(SessionAttribute.USER);
        User.Role role = (user != null) ? user.getUserRole() : User.Role.GUEST;

        if (!type.isValidRole(role)) {
            httpResponse.sendError(PagePath.ACCESS_ERROR_PAGE_403);
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
