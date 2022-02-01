package com.fairycompany.reviewer.controller.tag;

import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Class for mapping provided LocalDateTime object on the html page in accordance with localization.
 */
public class DateTimeMappingTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATE_TIME_PATTERN = "dd MMM yyyy HH:mm:ss";
    private static final String EMPTY_LINE = "";
    private LocalDateTime dateTime;

    /**
     * Sets date and time value from tag attribute.
     *
     * @param dateTime date and time from tag attribute
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Process provided date time value from tag attribute and map it on the page
     *
     * @return skip body constant
     * @throws JspException the jsp exception
     */
    @Override
    public int doStartTag() throws JspException {
        Locale locale = takeLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, locale);
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
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private Locale takeLocale() {
        HttpSession session = pageContext.getSession();
        String str = (String) session.getAttribute(SessionAttribute.SESSION_LOCALE);
        String[] localeRaw = str.split("_");
        return new Locale(localeRaw[0], localeRaw[1]);
    }
}

