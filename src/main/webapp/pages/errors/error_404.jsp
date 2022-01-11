<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>
<html>
<head>
    <title>Error 404</title>
</head>
<body>
    <h1>Error 404</h1>
    <h2>This page is not found</h2>
    <h4>
        <a href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1" class="link-primary">
            <fmt:message key="error.back"/>
        </a>
    </h4>
</body>
</html>
