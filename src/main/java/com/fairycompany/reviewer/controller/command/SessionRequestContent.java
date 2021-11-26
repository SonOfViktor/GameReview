package com.fairycompany.reviewer.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionRequestContent {
    private Map<String, String[]> requestParameters;
    private HashMap<String, Object> requestAttributes;
    private HashMap<String, Object> sessionAttributes;

    public void extractValues(HttpServletRequest request) {
        requestParameters = request.getParameterMap();          // todo
        requestAttributes = extractRequestAttribute(request);
        sessionAttributes = extractSessionAttribute(request);
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
        return values[0];
    }

    public void addSessionAttribute(String name, Object attribute) {
        sessionAttributes.put(name, attribute);
    }

    public Object getSessionAttribute(String name) {
        return sessionAttributes.get(name);
    }

    public void removeSessionAttribute(String name) {
        sessionAttributes.put(name, null);
    }

    private HashMap<String, Object> extractRequestAttribute(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Enumeration<String> requestAttributeNames = request.getAttributeNames();
        while(requestAttributeNames.hasMoreElements()) {
            String requestAttributeName = requestAttributeNames.nextElement();
            Object requestAttribute = request.getAttribute(requestAttributeName);
            hashMap.put(requestAttributeName, requestAttribute);
        }
        return hashMap;
    }

    private HashMap<String, Object> extractSessionAttribute(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HttpSession session = request.getSession();
        Enumeration<String> sessionAttributeNames = session.getAttributeNames();
        while(sessionAttributeNames.hasMoreElements()) {
            String sessionAttributeName = sessionAttributeNames.nextElement();
            Object sessionAttribute = session.getAttribute(sessionAttributeName);
            hashMap.put(sessionAttributeName, sessionAttribute);
        }
        return hashMap;
    }
}
