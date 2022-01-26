package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class CreateUserCommand extends AbstractCommand {
    private static final String EMPTY_LINE = "";
    private static final String SOURCE_LINK_FORMAT = "%s://%s:%s/%s";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        if (extractImageInputStream(request)) {
            String uploadDirectory = request.getServletContext().getRealPath(EMPTY_LINE);
            String sourceLink = String.format(SOURCE_LINK_FORMAT, request.getScheme(), request.getServerName(),
                    request.getServerPort(), request.getContextPath());
            content.addRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY, uploadDirectory);
            content.addRequestAttribute(RequestAttribute.SOURCE_LINK, sourceLink);

            UserService userService = UserServiceImpl.getInstance();

            try {
                if (userService.addUser(content)) {
                    router.setPage(PagePath.MAIN_PAGE_REDIRECT);
                    content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.USER_CREATION_SUCCESSFUL);
                }
            } catch (ServiceException e) {
                logger.log(Level.ERROR, "Adding user failed. {}", e.getMessage());
                throw new CommandException("Adding user failed", e);
            }
        }
        content.insertValues(request);

        return router;
    }
}
