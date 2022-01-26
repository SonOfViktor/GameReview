package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public class UpdateGameImageCommand extends AbstractCommand{

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        String relativeImagePath = request.getParameter(RequestParameter.GAME_IMAGE);
        String imagePath = request.getServletContext().getRealPath(relativeImagePath);
        updateImage(request, imagePath);

        content.insertValues(request);

        return router;
    }
}
