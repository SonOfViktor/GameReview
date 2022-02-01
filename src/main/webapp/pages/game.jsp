<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-icons.css" rel="stylesheet">
    <link href="css/vk.css" rel="stylesheet">
    <link rel="stylesheet" href="css/gr.style.css">



    <title>${game.name}</title>
</head>
<body>
    <header>
        <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
        <%@include file= "../WEB-INF/jspf/message.jspf" %>
    </header>



    <section class="games">
    <div class="container">
        <div class="row mt-3">
            <h3>${game.name}</h3>
        </div>

        <div class="row">
            <div class="hstack gap-3 mb-4">
                <strong><fmt:message key="game_publisher"/>:</strong>
                <span class="text-secondary">${game.publisher}</span>
                <div class="vr"></div>
                <strong><fmt:message key="game_release_date"/>:</strong>
                <span class="text-secondary">${game.releaseDate}</span>
                <div class="vr"></div>
                <strong><fmt:message key="game_platform"/>:</strong>
                <span class="text-secondary">${game.platform}</span>
            </div>
        </div>

        <div class="row">
            <div class="col-2">
                <img src="${game.image}" alt="" class="shadow bg-white rounded">
            </div>

            <div class="col-6">
                <div class="row">
                    <div class="hstack gap-2">
                        <div class="score_game">
                            <%@include file= "../WEB-INF/jspf/total_rating.jspf" %>
                        </div>
                        <span><fmt:message key="based_on"/> <strong>
                            ${user_amount} <fmt:message key="critic_review"/></strong></span>
                    </div>
                </div>
                <div class="row mt-2">
                    <div class="description overflow-auto">${game.description}</div>
                </div>
            </div>

            <div class="col-4 position-relative">
                <c:if test="${not empty user_total_rating}">
                    <div class="position-absolute top-0 start-0 translate-middle">
                        <c:choose>
                            <c:when test="${user_total_rating ge 75}">
                                <div class="score_user positive_bg">
                                    <span>${user_total_rating}</span>
                                </div>
                            </c:when>
                            <c:when test="${user_total_rating ge 30}">
                                <div class="score_user mixed_bg">
                                    <span>${user_total_rating}</span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="score_user negative_bg">
                                    <span>${user_total_rating}</span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
                <form id="form_review" class="needs-validation" novalidate method="post" action="controller">
                    <fieldset <c:if test="${empty user}"> disabled </c:if>>
                        <legend class="px-3"><strong><fmt:message key="your_game_rating"/></strong></legend>
                        <div>
                            <c:choose>
                                <c:when test="${empty user_total_rating}">
                                    <input type="hidden" name="command" value="create_game_rating">
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="command" value="update_game_rating">
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="game_id" value="${param.game_id}">
                        </div>
                        <div class="row">
                            <div class="col-6">
                                <div class="mt-2 row">
                                    <label for="inputGraphics" class="col-6 col-form-label fw-bold">
                                        <fmt:message key="game_rating_graphics"/>
                                    </label>
                                    <div class="col-6">
                                        <input type="number" id="inputGraphics" class="form-control form-control-sm"
                                               name="graphics" value="${user_rating.graphicsRating}"
                                               min="0" max="100" step="1" required>
                                        <div class="invalid-feedback">
                                            <span>0 - 100 <fmt:message key="step"/> 1</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-2 row">
                                    <label for="inputPlot" class="col-6 col-form-label fw-bold">
                                        <fmt:message key="game_rating_plot"/>
                                    </label>
                                    <div class="col-6">
                                        <input type="number" id="inputPlot" class="form-control form-control-sm"
                                               name="plot" value="${user_rating.plotRating}" min="0" max="100" step="1" required>
                                        <div class="invalid-feedback">
                                            <span>0 - 100 <fmt:message key="step"/> 1</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-2 row">
                                    <label for="inputGameplay" class="col-6 col-form-label fw-bold">
                                        <fmt:message key="game_rating_gameplay"/>
                                    </label>
                                    <div class="col-6">
                                        <input type="number" id="inputGameplay" class="form-control form-control-sm"
                                               name="gameplay" value="${user_rating.gameplayRating}" min="0" max="100" step="1" required>
                                        <div class="invalid-feedback">
                                            <span>0 - 100 <fmt:message key="step"/> 1</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-2 row">
                                    <label for="inputSound" class="col-6 col-form-label fw-bold">
                                        <fmt:message key="game_rating_sound"/>
                                    </label>
                                    <div class="col-6">
                                        <input type="number" id="inputSound" class="form-control form-control-sm"
                                               name="sound" value="${user_rating.soundRating}" min="0" max="100" step="1" required>
                                        <div class="invalid-feedback">
                                            <span>0 - 100 <fmt:message key="step"/> 1</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6 align-self-center text-center">
                                <c:choose>
                                    <c:when test="${empty user_total_rating}">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <fmt:message key="public_button"/>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" class="btn btn-primary btn-lg mb-3">
                                            <fmt:message key="update_button"/>
                                        </button> </br>
                                        <a class="btn btn-primary btn-lg" role="button"
                                           href="${pageContext.request.contextPath}/controller?command=delete_rating&game_id=${game.gameId}">
                                            <fmt:message key="delete_button"/>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>

            <div class="row">
                <div class="hstack gap-3 mb-2">
                    <strong><fmt:message key="game_developer"/>:</strong>
                    <span class="text-secondary">${game.developer}</span>
                    <div class="vr"></div>
                    <strong><fmt:message key="game_genre"/>:</strong>
                    <span class="text-secondary">${game.genres}</span>
                </div>
            </div>

            <div class="row mt-5 justify-content-center">
                <div class="col-8">
                    <div class="ratio ratio-16x9">
                        <iframe src="${game.trailerUrl}" title="YouTube video player"
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
                        </iframe>
                    </div>
                </div>
            </div>

            <div class="row mt-5 mb-5">
                    <fieldset <c:if test="${empty user}"> disabled </c:if>>
                        <div class="col-6">
                            <div class="row justify-content-between mb-0 review_label">
                                <div class="col-6">
                                    <span>${user.firstName} ${user.secondName}</span>
                                </div>
                                <div class="col-6 text-end">
                                    <span><ctg:show-date dateTime="${user_rating.publicationDate}"/></span>
                                </div>
                            </div>
                            <textarea form="form_review" class="form-control overflow-auto" id="inputTextReview" name="review" rows="4"
                                      maxlength="1000">${user_rating.review}</textarea>
                        </div>
                        <div class="col-6 align-self-center text-end mt-2">
                            <button type="submit" class="btn btn-primary btn-sm" form="form_review">
                                <c:choose>
                                    <c:when test="${empty user_total_rating}">
                                        <fmt:message key="public_button"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="update_button"/>
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </fieldset>
            </div>

            <div class="row d-flex d-flex justify-content-between">
                <c:forEach var="map" items="${reviews_for_game}">
                    <div class="col-6 mb-5">
                        <div class="row">
                            <div class="col-11">
                                <div class="row justify-content-between mb-0 review_label">
                                    <div class="col-6">
                                        <span>${map.full_name}</span>
                                    </div>
                                    <div class="col-6 text-end">
                                        <span><ctg:show-date dateTime="${map.publication_date}"/></span>
                                    </div>
                                </div>
                                <textarea class="form-control overflow-auto" id="inputReview" rows="4"
                                          maxlength="1000" readonly required>${map.review}
                                </textarea>
                            </div>
                            <c:if test="${user.userRole eq 'ADMIN'}">
                                <div class="col-1 align-self-center">
                                    <a href="${pageContext.request.contextPath}/controller?command=delete_review&game_rating_id=${map.game_rating_id}"
                                       class="link-dark fs-5">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>

                <c:set var="extra_param" value="&game_id=${game.gameId}"></c:set>
                <c:set var="command" value="to_game_page"/>

                <%@include file= "../WEB-INF/jspf/pagination.jspf" %>
            </div>
        </div>
    </div>
    </section>

    <%@include file= "../WEB-INF/jspf/footer.jspf" %>

    <script src="js/reload.js"></script>
    <script src="js/validation.js"></script>
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>