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
        <title><fmt:message key="login.title"/></title>
    </head>
    <body>
        <%@include file= "../WEB-INF/jspf/navbar.jspf" %>

        <%@include file= "../WEB-INF/jspf/message.jspf" %>

<%--        Перенести эту кнопку--%>
<%--        <c:if test="${user.getUserRole().name().toLowerCase() eq 'admin'}">--%>
            <div class="container">
                <div class="row justify-content-md-center">
                    <div class="col-2">
                        <a class="btn btn-primary"
                           href="${pageContext.request.contextPath}/controller?command=to_add_game_page" role="button">
                            Add Game
                        </a>
                    </div>
                </div>
            </div>
<%--        </c:if>--%>
    <section class="games">
        <div class="container mb-4">
            <c:forEach var="game_map" items="${game_list}">
                <c:set var="game" value="${game_map.game}"/>
                <div class="row mt-4">
                    <div class="col-2">
                        <a href="${pageContext.request.contextPath}/controller?command=to_game_page&game_id=${game.gameId}">
                            <img src="${game.image}" alt="">
                        </a>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <h4><a href="${pageContext.request.contextPath}/controller?command=to_game_page&game_id=${game.gameId}"
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
                    <div class="col-3 align-self-center">
                        <div class = "mx-auto score">
                            <%@include file= "../WEB-INF/jspf/total_rating.jspf" %>
                        </div>
<%--                        <c:choose>--%>
<%--                            <c:when test="${empty game_map.total_rating}">--%>
<%--                                <div class=" positive_bg">--%>
<%--                                        N/A--%>
<%--                                </div>--%>
<%--                            </c:when>--%>
<%--                            <c:when test="${game_map.total_rating ge 75}">--%>
<%--                                <div class="mx-auto score positive_bg">--%>
<%--                                   ${game_map.total_rating}--%>
<%--                                </div>--%>
<%--                            </c:when>--%>
<%--                            <c:when test="${game_map.total_rating ge 30}">--%>
<%--                                <div class="mx-auto score mixed_bg">--%>
<%--                                   ${game_map.total_rating}--%>
<%--                                </div>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <div class="mx-auto score negative_bg">--%>
<%--                                   ${game_map.total_rating}--%>
<%--                                </div>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

        <section>
            <div class="container">
            <nav aria-label="Navigation for countries">
                <ul class="pagination  justify-content-center">
                    <c:if test="${actual_page ne 1}">
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=${actual_page-1}">
                            Previous
                        </a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${page_amount}" var="i">
                        <c:choose>
                            <c:when test="${actual_page eq i}">
                                <li class="page-item active"><a class="page-link">
                                        ${i}</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link"
                                                         href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${actual_page lt page_amount}">
                        <li class="page-item"><a class="page-link"
                                                 href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=${actual_page+1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
            </div>
        </section>

        <script src="js/reload.js"></script>
        <script src="js/validation.js"></script>
        <script src="js/bootstrap.bundle.min.js"></script>
    </body>
</html>