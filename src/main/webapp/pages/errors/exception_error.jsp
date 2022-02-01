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

    <title><fmt:message key="error_title"/></title>
</head>

<body>

<div class="container" >
    <h1><fmt:message key="error_oops"/></h1>

    <table class="table table-bordered">
        <tbody>
        <tr>
            <th><span><fmt:message key="error_request"/></span></th>
            <td><span>${pageContext.errorData.requestURI}</span></td>
        </tr>
        <tr>
            <th><span><fmt:message key="error_servlet"/></span></th>
            <td><span>${pageContext.errorData.servletName}</span></td>
        </tr>
        <tr>
            <th><span><fmt:message key="error_exception"/></span></th>
            <td><span>${pageContext.errorData.throwable}</span></td>
        </tr>
        <tr>
            <th><span><fmt:message key="error_message"/></span></th>
            <td><span>${pageContext.exception.message}</span></td>
        </tr>
        <tr>
            <th><span><fmt:message key="error_status_code"/></span></th>
            <td><span>${pageContext.errorData.statusCode}</span></td>
        </tr>
        <tr>
            <th><span><fmt:message key="error_stack_trace"/></span></th>
            <td>
                <c:forEach items="${pageContext.exception.stackTrace}" var="element">
                    <span>element = ${element}</span>
                    <br/>
                </c:forEach>
            </td>
        </tr>
        </tbody>
    </table>

    <h4>
        <a href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1" class="link-primary">
            <fmt:message key="error_back"/>
        </a>
    </h4>
</div>

<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>