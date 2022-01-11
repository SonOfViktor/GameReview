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
    <title><fmt:message key="shopping_cart_page"/></title>
</head>
<html>
<body>
<div class="wrapper">
<header>
    <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
</header>

<%@include file= "../WEB-INF/jspf/message.jspf" %>

<main class="content">
    <section class="shopping-cart">
        <div class="container">
            <c:forEach var="order_map" items="${shopping_cart}">
                <c:set var="order" value="${order_map.key}"/>
                <c:set var="game" value="${order_map.value}"/>
            <div class="row">
                <div class="col-1 text-center">
                    <img src="${game.image}" class="shadow bg-white rounded">
                </div>

                <div class="col-4" style="line-height: 20px;">
                    <a href="" class="link-dark text-decoration-none fw-bold fs-5">${game.name}</a><br>
                    <span class="text-secondary">${game.publisher}</span><br>
                    <span class="text-secondary">${game.developer}</span><br>
                    <span class="text-secondary">${game.releaseDate}</span>
                </div>

                <div class="col-2">
                    <span class="fw-bold">${order.platform}</span>
                </div>

                <div class="col-4 text-end">
                    <span class="fw-bold fs-5">${game.price} $</span>
                </div>

                <div class="col-1 text-end">
                    <a href="${pageContext.request.contextPath}/controller?command=delete_game_from_shopping_cart&game_name=${game.name}&platform=${order.platform}"
                       class="link-dark fs-5">
                        <i class="bi bi-trash"></i>
                    </a>
                </div>
            </div>
            <hr>
            </c:forEach>

            <c:choose>
                <c:when test="${shopping_cart.size() eq 0}">
                    <div class="row justify-content-center">
                        <div class="col-6 text-center fs-5">
                            <fmt:message key="empty_shopping_cart_message_part1"/>
                            <a class="fw-bold" href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1"><fmt:message key="error.back"/></a>
                            <fmt:message key="empty_shopping_cart_message_part2"/>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row mb-3">
                        <div class="col-12 text-end">
                            <span class="fw-bold fs-5"><fmt:message key="total_price"/> ${total_price} $</span>
                        </div>
                    </div>

                    <div class="row mb-5">
                        <div class="col-12 text-center">
                            <a class="btn btn-primary"
                               href="${pageContext.request.contextPath}/controller?command=purchase_games" role="button">
                                <fmt:message key="purchase_button"/>
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </section>
</main>

<%@include file= "../WEB-INF/jspf/footer.jspf" %>
</div>

<script src="js/reload.js"></script>
<script src="js/validation.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>

</body>
</html>
