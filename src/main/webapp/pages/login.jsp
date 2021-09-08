<%--
  Created by IntelliJ IDEA.
  User: punks
  Date: 01.09.2021
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>

<html>
<head>
    <title><fmt:message key="login.title"/></title>
</head>
<body>
<form name="LoginForm" method="post" action="controller">
    <input type="hidden" name="command" value="login" />
    <fmt:message key="login.login"/>: <br/>
    <label>
        <input type="text" name="login" value=""/>
    </label>
    <br/><fmt:message key="login.password"/>:<br/>
    <input type="password" name="password" value=""/>
    <br/>
    ${errorLoginPassMessage}
    <br/>
    ${wrongAction}
    <br/>
    ${nullPage}
    <br/>
    <input type="submit" value="<fmt:message key="login.log_in"/>"/>
</form> <hr/>
</body>
</html>
