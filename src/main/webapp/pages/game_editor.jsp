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

    <title><fmt:message key="game_editor_page"/></title>
</head>

<body>
<header>
    <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
    <%@include file= "../WEB-INF/jspf/message.jspf" %>
</header>

<section class="mb-3">
    <div class="container">
        <div class="col-7 mx-auto">
            <div class="row mb-3">
                <h3><fmt:message key="edit_data"/></h3>
            </div>

            <form class="needs-validation" novalidate method="post" action="controller">
                <input type="hidden" name="command" value="update_game">
                <input type="hidden" name="game_id" value="${game.gameId}">
                <div class="row mb-3">
                    <label for="inputName" class="col-3 col-form-label"><fmt:message key="game_name"/></label>
                    <div class="col-9">
                        <input type="text" class="form-control" id="inputName" name="game_name" value="${game.name}"
                               pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="add_game_name_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPublisher" class="col-3 col-form-label"><fmt:message key="game_publisher"/></label>
                    <div class="col-9">
                        <input type="text" class="form-control" id="inputPublisher" name="publisher" value="${game.publisher}"
                               pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="add_game_publisher_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputDeveloper" class="col-3 col-form-label"><fmt:message key="game_developer"/></label>
                    <div class="col-9">
                        <input type="text" class="form-control" id="inputDeveloper" name="developer" value="${game.developer}"
                               pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="add_game_developer_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputReleaseDate" class="col-3 col-form-label"><fmt:message key="game_release_date"/></label>
                    <div class ="col-9">
                        <input type="date" class="form-control" id="inputReleaseDate"
                               name="release_date" value="${game.releaseDate}" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="release_date_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPlatform" class="col-3 col-form-label"><fmt:message key="game_platform"/></label>
                    <div class="col-9">
                        <div id="inputPlatform">
                            <c:forEach var="platform" items="${platforms}">
                                <div class="form-check form-check-inline mb-2">
                                    <input class="form-check-input" type="checkbox" name="platform" id="${platform.name()}" value="${platform}"
                                           <c:if test="${game.getPlatform().contains(platform)}">checked</c:if>>
                                    <label class="form-check-label" for="${platform.name()}">
                                            ${platform.name().replace("_", " ")}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPrice" class="col-3 col-form-label"><fmt:message key="game_price"/></label>
                    <div class="col-9">
                        <input type="number" class="form-control" id="inputPrice"
                               name="price" value="${game.price}" min="0.01" max="200" step="0.01" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="add_game_price_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputTrailer" class="col-3 col-form-label"><fmt:message key="game_trailer"/></label>
                    <div class="col-9">
                        <input type="url" class="form-control" id="inputTrailer" name="trailer" value="${game.trailerUrl}"
                               pattern="https://www\.youtube\.com/watch\?v=[\w_-]{11}" required>
                        <div class="invalid-feedback">
                            <span><fmt:message key="add_game_trailer_invalid_message"/></span>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputDescription" class="col-3 col-form-label"><fmt:message key="game_description"/></label>
                    <div class="col-9">
                        <textarea class="form-control" id="inputDescription" name="game_description" rows="3"
                                  maxlength="1000" required>${game.description}
                        </textarea>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-12 d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="update_button"/></button>
                    </div>
                </div>
            </form>
        </div>
        <hr>
    </div>
</section>

<section class="mb-3">
    <div class="container">
        <div class="col-7 mx-auto">
            <div class="row mb-3">
                <h3><fmt:message key="change_genres"/></h3>
            </div>

            <form class="needs-validation" novalidate method="post" action="controller">
                <input type="hidden" name="command" value="update_genres">
                <input type="hidden" name="game_id" value="${game.gameId}">
                <div class="row mb-4">
                    <div class="col-12 d-flex justify-content-center">
                        <button class="btn btn-primary" type="button" data-bs-toggle="collapse"
                                data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                            <fmt:message key="game_genre"/>
                        </button>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-12">
                        <div class="collapse" id="collapseExample">
                            <c:forEach var="genre" items="${genres}">
                                <div class="form-check form-check-inline mb-2" id="inputGenre">
                                    <input class="form-check-input" type="checkbox" name="genre" id="${genre.name()}" value="${genre}"
                                           <c:if test="${game.getGenres().contains(genre)}">checked</c:if>>
                                    <label class="form-check-label text-capitalize" for="${genre.name()}">
                                        ${genre.name().replace("_", " ").toLowerCase()}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-12 d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="update_button"/></button>
                    </div>
                </div>
            </form>
        </div>
        <hr>
    </div>
</section>

<section class="mb-5">
    <div class="container">
        <div class="col-7 mx-auto">
            <div class="row mb-5">
                <h3><fmt:message key="change_image"/></h3>
            </div>

            <form class="needs-validation" novalidate method="post" action="upload_image" enctype="multipart/form-data">
                <input type="hidden" name="command" value="update_game_image">
                <input type="hidden" name="game_image" value="${game.image}">
                <div class="row mb-3">
                    <div class="col-3">
                        <img src="${game.image}" class="shadow bg-white rounded">
                    </div>
                    <div class="col-9 align-self-center">
                        <label for="inputImage" class="form-label"><fmt:message key="choose_photo"/></label>
                        <div class="input-group mb-3" id="inputImage">
                            <input type="file" class="form-control" id="inputGroupFile02" name="image">
                            <label class="input-group-text" for="inputGroupFile02"><fmt:message key="upload"/></label>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-12 d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="update_button"/></button>
                    </div>
                </div>
            </form>
        </div>
        <hr>
    </div>
</section>
<section class="mb-5">
    <div class="container">
        <div class="col-7 mx-auto">
            <div class="row">
                <div class="col-12 d-flex justify-content-center">
                    <form method="post" action="controller">
                        <input type="hidden" name="command" value="delete_game">
                        <input type="hidden" name="game_id" value="${game.gameId}">
                        <input type="hidden" name="image" value="${game.image}">
                        <input type="hidden" name="actual_page" value="1">
                        <div class="row mb-3">
                            <button type="submit" class="btn btn-primary mb-3"><fmt:message key="delete_game"/></button>
                        </div>
                    </form>
                </div>
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
