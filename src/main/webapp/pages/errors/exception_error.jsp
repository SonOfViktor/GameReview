<%--<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <title>Error Page</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--Request from ${pageContext.errorData.requestURI} is failed <br/>--%>
<%--Servlet name: ${pageContext.errorData.servletName} <br/>--%>
<%--Status code: ${pageContext.errorData.statusCode} <br/>--%>
<%--Exception: ${pageContext.errorData.throwable} <br/>--%>
<%--Message from exception: ${pageContext.exception.message}<br/>--%>
<%--stack trace : <br/>--%>
<%--<c:forEach items="${pageContext.exception.stackTrace}" var="element">--%>
<%--    element =   ${element}--%>
<%--    <br/>--%>
<%--</c:forEach>--%>
<%--<a href="index.jsp">Back to index</a>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <title><fmt:message key="error.title"/></title>
</head>

<body>

<div class="container" >
    <h1><fmt:message key="error.oops"/></h1>

    <table class="table table-bordered">
        <tbody>
        <tr>
            <th><fmt:message key="error.request"/></th>
            <td>${pageContext.errorData.requestURI}</td>
        </tr>
        <tr>
            <th><fmt:message key="error.servlet"/></th>
            <td>${pageContext.errorData.servletName}</td>
        </tr>
        <tr>
            <th><fmt:message key="error.exception"/></th>
            <td>${pageContext.errorData.throwable}</td>
        </tr>
        <tr>
            <th><fmt:message key="error.message"/></th>
            <td>${pageContext.exception.message}</td>
        </tr>
        <tr>
            <th><fmt:message key="error.status_code"/></th>
            <td>${pageContext.errorData.statusCode}</td>
        </tr>
        <tr>
            <th><fmt:message key="error.stack_trace"/></th>
            <td>
                <c:forEach items="${pageContext.exception.stackTrace}" var="element">
                    element =   ${element}
                    <br/>
                </c:forEach>
            </td>
        </tr>
        </tbody>
    </table>

    <h4>
        <a href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1" class="link-primary">
            <fmt:message key="error.back"/>
        </a>
    </h4>
</div>

<%--    <c:remove var="exception" scope="session"/>--%>

<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>