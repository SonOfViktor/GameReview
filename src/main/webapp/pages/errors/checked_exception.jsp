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

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <title><fmt:message key="error.title"/></title>
</head>

<body>
    <%@include file= "../../WEB-INF/jspf/navbar_light.jspf" %>

    <div class="container" >
        <h1><fmt:message key="error.oops"/></h1>

        <table class="table table-bordered">
            <tbody>
            <tr>
                <th><fmt:message key="error.exception"/></th>
                <td>${exception}</td>
            </tr>
            <tr>
                <th><fmt:message key="error.message"/></th>
                <td>${exception.message}</td>
            </tr>
            <tr>
                <th><fmt:message key="error.status_code"/></th>
                <td>
                    <c:if test="${not empty exception}"> 500 </c:if>
                    </td>
            </tr>
            <tr>
                <th><fmt:message key="error.stack_trace"/></th>
                <td>
                    <c:forEach items="${exception.stackTrace}" var="element">
                        ${element}
                        <br/>
                    </c:forEach>
                </td>
            </tr>
            </tbody>
        </table>

        <h4>
            <a href="${pageContext.request.contextPath}/controller?command=to_main_page" class="link-primary">
                <fmt:message key="error.back"/>
            </a>
        </h4>
    </div>

<%--    <c:remove var="exception" scope="session"/>--%>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous">
    </script>
</body>
</html>