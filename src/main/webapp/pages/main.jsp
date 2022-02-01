<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>
<!doctype html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/gr.style.css">
        <link href="css/bootstrap-icons.css" rel="stylesheet">
        <link href="css/vk.css" rel="stylesheet">
        <title><fmt:message key="main_title"/></title>
    </head>

    <body>
    <div class="wrapper">
        <header>
            <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
            <%@include file= "../WEB-INF/jspf/message.jspf" %>
        </header>

        <main class="content">
        <section class="games">
            <div class="container mb-4">
                <c:forEach var="game_map" items="${game_list}">
                    <c:set var="game" value="${game_map.game}"/>
                    <div class="row mt-4">
                        <div class="col-2">
                            <a href="${pageContext.request.contextPath}/controller?command=to_game_page&game_id=${game.gameId}&actual_page=1">
                                <img src="${game.image}" alt="" class="shadow bg-white rounded">
                            </a>
                        </div>
                        <div class="col-7">
                            <div class="row">
                                <h4><a href="${pageContext.request.contextPath}/controller?command=to_game_page&game_id=${game.gameId}&actual_page=1"
                                       class="link-dark">${game.name}</a></h4>
                            </div>
                            <div class="row">
                                <span><strong><fmt:message key="add_game_platform"/>: </strong>
                                    <span class="text-secondary">${game.platform}</span></span>
                                <span><strong><fmt:message key="add_game_release_date"/>: </strong>
                                    <span class="text-secondary">${game.releaseDate}</span></span>
                                <p class="summary">
                                    ${game.description}
                                </p>
                            </div>
                        </div>
                        <div class="col-1 align-self-center">
                            <div class="mx-auto score">
                                <c:set var="total_game_rating" value="${game_map.total_rating}"/>
                                <%@include file= "../WEB-INF/jspf/total_rating.jspf" %>
                            </div>
                        </div>

                        <div class="col-2 align-self-center">
                            <div class="dropdown">
                                <button class="btn bnt-link fs-2 fw-bold link-dark text-decoration-none position-relative dropdown-toggle"
                                        role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false"
                                        <c:if test="${empty user or user.getUserRole().name().toLowerCase() eq 'guest'}">disabled</c:if>>
                                    ${game.price}
                                    <i class="bi bi-cash-coin link-success "></i>
                                </button>

                                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                    <c:forEach var="platform" items="${game.platform}">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?command=add_game_to_shopping_cart&game_id=${game.gameId}&platform=${platform}">
                                                ${platform}
                                        </a></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

            <c:choose>
                <c:when test="${empty param.search_field}">
                    <c:set var="command" value="to_main_page"/>
                </c:when>
                <c:otherwise>
                    <c:set var="extra_param" value="&search_field=${param.search_field}"></c:set>
                    <c:set var="command" value="search_game"/>
                </c:otherwise>
            </c:choose>

            <%@include file= "../WEB-INF/jspf/pagination.jspf" %>
        </main>

        <%@include file= "../WEB-INF/jspf/footer.jspf" %>
    </div>

        <script src="js/reload.js"></script>
        <script src="js/validation.js"></script>
        <script src="js/bootstrap.bundle.min.js"></script>
    </body>
</html>