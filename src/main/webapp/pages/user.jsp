<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <title><fmt:message key="user_page"/></title>
</head>
<body>
<div class="wrapper">
    <%@include file="../WEB-INF/jspf/navbar.jspf" %>

    <main class="content">
        <section class="user mb-5">
            <div class="container">
                <div class="row">
                    <div class="col-2">
                        <img src="${user.photo}" class="shadow bg-white rounded">
                    </div>

                    <div class="col-6 ps-3">
                        <div class="vstack gap-4">
                            <h2 class="text-uppercase">${user.firstName} ${user.secondName}</h2>
                            <div class="row">
                                <div class="hstack gap-5">
                                    <h5><fmt:message
                                            key="rating_amount"/>: ${user_rating_amount.amount_game_rating}</h5>
                                    <h5><fmt:message key="review_amount"/>: ${user_rating_amount.amount_review}</h5>
                                </div>
                            </div>
                            <c:if test="${not empty min_max_user_rating}">
                                <div class="row">
                                    <div class="col-1 align-self-center">
                                        <div class="score_user positive_bg">${min_max_user_rating.max_rating}</div>
                                    </div>
                                    <div class="col-11 align-self-center lh-sm">
                                        <span class="fw-bold"><fmt:message key="high_rating"/>:</span></br>
                                        <span class="fw-bold fst-italic">${min_max_user_rating.max_game_name}</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-1 align-self-center">
                                        <div class="score_user negative_bg">${min_max_user_rating.min_rating}</div>
                                    </div>
                                    <div class="col-11 align-self-center lh-sm">
                                        <span class="fw-bold"><fmt:message key="low_rating"/>:</span></br>
                                        <span class="fw-bold fst-italic">${min_max_user_rating.min_game_name}</span>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <div class="col-2 align-self-center">
                        <div class="vstack gap-5">
                            <a class="btn btn-primary lh-sm mx-3"
                               href="${pageContext.request.contextPath}/controller?command=to_user_editor_page"
                               role="button">
                                <fmt:message key="edit_account"/>
                            </a>
                        </div>
                    </div>

                    <div class="col-2 align-self-center">
                        <div class="vstack gap-5">
                            <a class="btn btn-success lh-sm mx-3"
                               href="${pageContext.request.contextPath}/controller?command=to_game_manager_page&actual_page=1" role="button">
                                <fmt:message key="admin_game_manager"/>
                            </a>
                            <a class="btn btn-success lh-sm mx-3"
                               href="${pageContext.request.contextPath}/controller?command=to_user_manager_page&actual_page=1" role="button">
                                <fmt:message key="admin_user_manager"/>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="my-3">
            <div class="container">
                <div class="row">
                    <h4><fmt:message key="score_distribution"/>:</h4>
                    <div class="col-2">
                        <div class="vstack gap-3">
                            <h5><fmt:message key="positive"/>:</h5>
                            <h5><fmt:message key="mixed"/>: </h5>
                            <h5><fmt:message key="negative"/>:</h5>
                        </div>
                    </div>
                    <div class="col-1 text-center">
                        <div class="vstack gap-3">
                            <h5 class="text-success fw-bold">${user_rating_amount.positive_amount}</h5>
                            <h5 class="text-warning fw-bold">${user_rating_amount.mixed_amount}</h5>
                            <h5 class="text-danger fw-bold">${user_rating_amount.negative_amount}</h5>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <%@include file="../WEB-INF/jspf/footer.jspf" %>
</div>

<script src="js/reload.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
