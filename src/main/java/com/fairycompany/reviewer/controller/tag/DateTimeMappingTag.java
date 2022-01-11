package com.fairycompany.reviewer.controller.tag;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.model.entity.GameRating;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMappingTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
    private static final String EMPTY_LINE = "";
    private LocalDateTime dateTime;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int doStartTag() throws JspException {
//        GameRating rating = (GameRating) pageContext.getRequest().getAttribute(RequestAttribute.USER_RATING);
//        LocalDateTime date = rating.getPublicationDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        String formattedDate = (dateTime != null) ? formatter.format(dateTime) : EMPTY_LINE;

        try {
            JspWriter out = pageContext.getOut();
            out.write(formattedDate);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error when writing date. {}", e.getMessage());
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}

