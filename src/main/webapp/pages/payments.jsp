<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
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
    <title><fmt:message key="payment_page"/></title>
</head>
<html>
<body>
<div class="wrapper">
    <header>
        <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
        <%@include file= "../WEB-INF/jspf/message.jspf" %>
    </header>

    <main class="content">
        <section class="payments">
            <div class="container">
                <c:forEach var="payment" varStatus="status" items="${payment_list}">
                    <div class="row">
                        <p>
                            <button class="btn btn-link text-decoration-none link-dark col-5" type="button"
                                    data-bs-toggle="collapse" data-bs-target="#payment${status.count}"
                                    aria-expanded="false" aria-controls="payment${status.count}">
                                <div class="row">
                                    <div class="col-6 fw-bold text-start">
                                        <span><fmt:message key="order"/>${payment.paymentId}</span>
                                    </div>
                                    <div class="col-6 text-start">
                                        <span><ctg:show-date dateTime="${payment.paymentDate}"/></span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-6 fw-bold text-start">
                                        <span><fmt:message key="total_price"/></span>
                                    </div>
                                    <div class="col-6 text-start">
                                        <span>${total_price_list[status.index]} $</span>
                                    </div>
                                </div>
                            </button>
                        </p>
                        <div class="collapse" id="payment${status.count}">
                            <div class="card card-body">
                                <c:forEach var="order" items="${payment.orders}">
                                    <div class="row">
                                        <div class="col-3 fw-bold">
                                            <span>${order.gameName}</span>
                                        </div>
                                        <div class="col-2">
                                            <span>${order.platform}</span>
                                        </div>
                                        <div class="col-5">
                                            <strong><fmt:message key="game_key"/>:</strong> ${order.gameKey}
                                        </div>
                                        <div class="col-2">
                                            <span>${order.price} $</span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <hr>
                </c:forEach>
            </div>
        </section>
        <c:choose>
            <c:when test="${payment_list.isEmpty()}">
                <div class="row justify-content-center">
                    <div class="col-6 text-center fs-5">
                        <span><fmt:message key="empty_payment"/></span>
                        <a class="fw-bold" href="${pageContext.request.contextPath}/controller?command=to_main_page&actual_page=1"><fmt:message key="error_back"/></a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:set var="command" value="to_payment_page"/>
                <%@include file= "../WEB-INF/jspf/pagination.jspf" %>
            </c:otherwise>
        </c:choose>
    </main>

    <%@include file= "../WEB-INF/jspf/footer.jspf" %>
</div>

<script src="js/reload.js"></script>
<script src="js/validation.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>

</body>
</html>
