package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public class UpdatePhotoCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);


        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        String relativeImagePath = user.getPhoto();
        String imagePath = request.getServletContext().getRealPath(relativeImagePath);

        updateImage(request, imagePath);

        content.insertValues(request);

        return router;
    }
}
