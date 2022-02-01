package com.fairycompany.reviewer.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contain request parameters and attributes and session attributes content.
 */
public class SessionRequestContent {
    private Map<String, String[]> requestParameters = new HashMap<>();
    private Map<String, Object> requestAttributes = new HashMap<>();
    private Map<String, Object> sessionAttributes = new HashMap<>();

    /**
     * Extract parameters and attributes from request.
     *
     * @param request servlet request
     */
    public void extractValues(HttpServletRequest request) {
        requestParameters = request.getParameterMap();
        requestAttributes = extractRequestAttribute(request);
        sessionAttributes = extractSessionAttribute(request);
    }

    /**
     * Insert parameters and attributes into request.
     *
     * @param request http request
     */
    public void insertValues(HttpServletRequest request) {
        HttpSession session = request.getSession();
        requestAttributes.forEach(request::setAttribute);
        sessionAttributes.forEach(session::setAttribute);
    }

    /**
     * Add request attribute.
     *
     * @param name      attribute name
     * @param attribute attribute value
     */
    public void addRequestAttribute(String name, Object attribute) {
        requestAttributes.put(name, attribute);
    }

    /**
     * Get request attribute.
     *
     * @param name attribute name
     * @return attribute by specified name
     */
    public Object getRequestAttribute(String name) {
        return requestAttributes.get(name);
    }

    /**
     * Get array values of request parameter.
     *
     * @param name parameter name
     * @return array values of specified parameter
     */
    public String[] getRequestParameterValues(String name) {
        return requestParameters.get(name);
    }

    /**
     * Gets request parameter.
     *
     * @param name parameter name
     * @return request parameter value or empty string if parameter null
     */
    public String getRequestParameter(String name) {
        String[] values = requestParameters.get(name);
        return (values != null) ? values[0] : "";
    }

    /**
     * Add session attribute.
     *
     * @param name      attribute name
     * @param attribute attribute value
     */
    public void addSessionAttribute(String name, Object attribute) {
        sessionAttributes.put(name, attribute);
    }

    /**
     * Gets session attribute.
     *
     * @param name attribute name
     * @return session attribute value
     */
    public Object getSessionAttribute(String name) {
        return sessionAttributes.get(name);
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
