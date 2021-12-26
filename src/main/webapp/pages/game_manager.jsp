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
    <head>
        <title><fmt:message key="game_manager_page"/></title>
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
                <div class="row justify-content-center mb-4">
                    <div class="col-2">
                        <a class="btn btn-primary"
                           href="${pageContext.request.contextPath}/controller?command=to_add_game_page" role="button">
                            <fmt:message key="add_game"/>
                        </a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-11">
                        <table class="table table-striped table-bordered align-middle shadow-sm p-3 mb-5 bg-white rounded">
                            <thead class="table-dark">
                            <tr>
                                <th scope="col"><fmt:message key="id"/></th>
                                <th scope="col"><fmt:message key="game_name"/></th>
                                <th scope="col"><fmt:message key="game_publisher"/></th>
                                <th scope="col"><fmt:message key="game_developer"/></th>
                                <th scope="col"><fmt:message key="game_release_date"/></th>
                                <th scope="col"><fmt:message key="game_price"/></th>
                                <th scope="col"><fmt:message key="total_rating"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="game_map" items="${game_list}">
                                <c:set var="game" value="${game_map.game}"/>
                                    <tr>
                                        <th scope="row">${game.gameId}</th>
                                        <td><a href="${pageContext.request.contextPath}/controller?command=to_game_page&game_id=${game.gameId}&actual_page=1"
                                               class="text-decoration-none link-dark">
                                                ${game.name}
                                            </a>
                                        </td>
                                        <td>${game.publisher}</td>
                                        <td>${game.developer}</td>
                                        <td>${game.releaseDate}</td>
                                        <td>${game.price}</td>
                                        <td><c:choose>
                                                <c:when test="${not empty game_map.total_rating}">
                                                    ${game_map.total_rating}
                                                </c:when>
                                                <c:otherwise>
                                                    N/A
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-1">
                        <table class="table table-borderless">
                            <thead class="invisible">
                            <tr>
                                <th>Invisible</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="game_map" items="${game_list}">
                                <c:set var="game" value="${game_map.game}"/>
                                <tr>
                                    <th scope="row" style="padding-bottom: 2px">
                                        <a class="btn btn-primary btn-sm"
                                           href="${pageContext.request.contextPath}/controller?command=to_game_editor_page&game_id=${game.gameId}" role="button">
                                            <fmt:message key="update_button"/>
                                        </a>
                                    </th>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>

        <c:set var="command" value="to_game_manager_page"/>
        <%@include file= "../WEB-INF/jspf/pagination.jspf" %>
    </main>

    <%@include file= "../WEB-INF/jspf/footer.jspf" %>

    <script src="js/reload.js"></script>
    <script src="js/validation.js"></script>
    <script src="js/bootstrap.bundle.min.js"></script>
    </div>
    </body>
</html>
