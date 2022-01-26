<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/gr.style.css">
    <link href="css/bootstrap-icons.css" rel="stylesheet">
    <link href="css/vk.css" rel="stylesheet">

    <title><fmt:message key="user_manager_page"/></title>
</head>

<body>
<div class="wrapper">
<header>
    <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
    <%@include file= "../WEB-INF/jspf/message.jspf" %>
</header>

    <main class="content">
    <section>
        <div class="container">
            <div class="row">
                <div class="col-11">
                    <table class="table table-striped table-bordered align-middle shadow-sm p-3 mb-5 bg-white rounded">
                        <thead class="table-dark">
                        <tr>
                            <th scope="col"><fmt:message key="id"/></th>
                            <th scope="col"><fmt:message key="user_login"/></th>
                            <th scope="col"><fmt:message key="user_name"/></th>
                            <th scope="col"><fmt:message key="user_birthday"/></th>
                            <th scope="col"><fmt:message key="user_phone"/></th>
                            <th scope="col"><fmt:message key="user_role"/></th>
                            <th scope="col"><fmt:message key="user_status"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${user_list}">
                            <form id="${user.login}" method="post" action="controller">
                                <input type="hidden" name="command" value="update_user_by_admin">
                                <input type="hidden" name="user_id" value="${user.userId}">
                                <tr>
                                    <th scope="row">${user.userId}</th>
                                    <td>${user.login}</td>
                                    <td>${user.firstName} ${user.secondName}</td>
                                    <td>${user.birthday}</td>
                                    <td>${user.phone}</td>
                                    <td><select class="form-select form-select-sm" name="role">
                                            <c:choose>
                                                <c:when test="${user.userRole eq 'USER'}">
                                                    <option value="user" selected>User</option>
                                                    <option value="admin">Admin</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="user">User</option>
                                                    <option value="admin" selected>Admin</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td><select class="form-select form-select-sm text-capitalize" name="status">
                                            <c:choose>
                                                <c:when test="${user.userStatus eq 'ACTIVE'}">
                                                    <option value="active" selected>Active</option>
                                                    <option value="banned">Banned</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="active">Active</option>
                                                    <option value="banned" selected>Banned</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                </tr>
                            </form>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="col-1">
                    <table class="table table-borderless">
                        <thead class="invisible">
                        <tr>
                            <th scope="col"><button type="button" class="btn btn-primary btn-sm"><fmt:message key="update_button"/></button></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${user_list}">
                            <tr>
                                <th scope="row">
                                    <button type="submit" form="${user.login}" class="btn btn-primary btn-sm"><fmt:message key="update_button"/></button>
                                </th>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </section>

    <c:set var="command" value="to_user_manager_page"/>
    <%@include file= "../WEB-INF/jspf/pagination.jspf" %>
</main>

<%@include file= "../WEB-INF/jspf/footer.jspf" %>

<script src="js/reload.js"></script>
<script src="js/validation.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>

</div>
</body>
</html>
