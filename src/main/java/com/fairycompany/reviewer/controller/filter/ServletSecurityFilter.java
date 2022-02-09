package com.fairycompany.reviewer.controller.filter;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

/**
 * Filter command provided by request and check access for user
 */
@WebFilter(filterName = "ServletSecurityFilter",
            urlPatterns = {"/controller", "/upload_image"},
            dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST},
            initParams = { @WebInitParam(name = "index_path", value = "index.jsp") })
public class ServletSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        User user = (User) session.getAttribute(SessionAttribute.USER);
        User.Role role = (user != null) ? user.getUserRole() : User.Role.GUEST;
        String command = request.getParameter(RequestParameter.COMMAND);
        CommandType commandType;

        if (isCommandValid(command)) {
            commandType = CommandType.valueOf(command.toUpperCase());
            if (!commandType.hasUserRole(role)) {
                commandType = CommandType.TO_MAIN_PAGE;
                session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USER_ROLE);
            }
        } else {
            commandType = CommandType.TO_MAIN_PAGE;
            session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
        }

        request.setAttribute(RequestAttribute.COMMAND, commandType.getCommand());

        chain.doFilter(request, response);
    }

    private boolean isCommandValid(String command) {
        return command != null && Arrays.stream(CommandType.values())
                                        .anyMatch((commandType) -> commandType.name().equalsIgnoreCase(command));
    }
}
