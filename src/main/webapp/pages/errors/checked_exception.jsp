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
    <link href="css/bootstrap-icons.css" rel="stylesheet">
    <link href="css/vk.css" rel="stylesheet">

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
            <a href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1" class="link-primary">
                <fmt:message key="error.back"/>
            </a>
        </h4>
    </div>

    <%@include file= "../../WEB-INF/jspf/footer.jspf" %>

    <<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>