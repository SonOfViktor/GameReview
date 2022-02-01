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
                            <th scope="col"><span><fmt:message key="id"/></span></th>
                            <th scope="col"><span><fmt:message key="user_login"/></span></th>
                            <th scope="col"><span><fmt:message key="user_name"/></span></th>
                            <th scope="col"><span><fmt:message key="user_birthday"/></span></th>
                            <th scope="col"><span><fmt:message key="user_phone"/></span></th>
                            <th scope="col"><span><fmt:message key="user_role"/></span></th>
                            <th scope="col"><span><fmt:message key="user_status"/></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${user_list}">
                            <form id="${user.login}" method="post" action="controller">
                                <input type="hidden" name="command" value="update_user_by_admin">
                                <input type="hidden" name="user_id" value="${user.userId}">
                                <tr>
                                    <th scope="row"><span>${user.userId}</span></th>
                                    <td><span>${user.login}</span></td>
                                    <td><span>${user.firstName} ${user.secondName}</span></td>
                                    <td><span>${user.birthday}</span></td>
                                    <td><span>${user.phone}</span></td>
                                    <td><select class="form-select form-select-sm" name="role">
                                            <c:choose>
                                                <c:when test="${user.userRole eq 'USER'}">
                                                    <option value="user" selected><span>User</span></option>
                                                    <option value="admin"><span>Admin</span></option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="user"><span>User</span></option>
                                                    <option value="admin" selected><span>Admin</span></option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td><select class="form-select form-select-sm text-capitalize" name="status">
                                            <c:choose>
                                                <c:when test="${user.userStatus eq 'ACTIVE'}">
                                                    <option value="active" selected><span>Active</span></option>
                                                    <option value="banned"><span>Banned</span></option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="active"><span>Active</span></option>
                                                    <option value="banned" selected><span>Banned</span></option>
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
