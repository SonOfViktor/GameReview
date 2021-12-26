package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.Optional;

public class CreateUserCommand extends AbstractCommand {
    private static final String RELATIVE_IMAGE_PATH = "media\\people";
    private static final String DEFAULT_FILE = "pic\\default_user.jpg";
    private static final String SOURCE_LINK_FORMAT = "%s://%s:%s/%s";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        String uploadDirectory = request.getServletContext().getRealPath(RELATIVE_IMAGE_PATH);

        Optional<String> imageName = saveImage(request, uploadDirectory, DEFAULT_FILE);
        if (imageName.isPresent()) {
            content.addRequestAttribute(RequestAttribute.USER_PHOTO,
                    RELATIVE_IMAGE_PATH + File.separator + imageName.get());

            String sourceLink = String.format(SOURCE_LINK_FORMAT, request.getScheme(), request.getServerName(),
                    request.getServerPort(), request.getContextPath());
            content.addRequestAttribute(RequestAttribute.SOURCE_LINK, sourceLink);

            UserService userService = UserServiceImpl.getInstance();

            try {
                if (userService.addUser(content)) {
                    router.setPage(PagePath.MAIN_PAGE_REDIRECT);
                    content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.USER_CREATION_SUCCESSFUL);
                } else {
                    deleteFile(uploadDirectory + File.separator + imageName.get());     // todo будет время подумай, два раза повтор удаления
                }
            } catch (ServiceException e) {
                logger.log(Level.ERROR, "Adding user failed. {}", e.getMessage());
                deleteFile(uploadDirectory + File.separator + imageName.get());
                throw new CommandException("Adding user failed", e);
            }
        }
        content.insertValues(request);

        return router;
    }
}
