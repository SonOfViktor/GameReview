package com.fairycompany.reviewer.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class SessionRequestContent {
    private final HashMap<String, Object> requestAttributes;
    private final Map<String, String[]> requestParameters; // immutable Map
    private final HashMap<String, Object> sessionAttributes;

    public SessionRequestContent(HttpServletRequest request) {
        requestAttributes = new HashMap<>();
        sessionAttributes = new HashMap<>();
        requestParameters = request.getParameterMap();

        HttpSession session = request.getSession();
        session.getAttributeNames().asIterator().forEachRemaining(name -> {
            Object value = session.getAttribute(name);
            sessionAttributes.put(name, value);
        });
    }

    public void insertValues(HttpServletRequest request) {
        HttpSession session = request.getSession();
        requestAttributes.forEach(request::setAttribute);
        sessionAttributes.forEach(session::setAttribute);
    }

    public void addRequestAttribute(String name, Object attribute) {
        requestAttributes.put(name, attribute);
    }

    public Object getRequestAttribute(String name) {
        return requestAttributes.get(name);
    }

    public String[] getRequestParameterValues(String name) {
        return requestParameters.get(name);
    }

    public String getRequestParameter(String name) {
        String[] values = requestParameters.get(name);
        return ((values != null && values.length > 0) ? values[0] : null);
    }

    public void addSessionAttribute(String name, Object attribute) {
        sessionAttributes.put(name, attribute);
    }

    public Object getSessionAttribute(String name) {
        return sessionAttributes.get(name);
    }

    public void removeSessionAttribute(String name) {
        sessionAttributes.remove(name);
    }
}
