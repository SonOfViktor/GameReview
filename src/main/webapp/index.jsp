<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Index Page</title>
</head>
<body>
    <c:set var="row_amount" value="5" scope="session"/>
    <c:redirect url="/controller?command=to_main_page&actual_page=1"/>
</body>
</html>