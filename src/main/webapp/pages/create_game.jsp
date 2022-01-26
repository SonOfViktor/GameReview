<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>

<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/gr.style.css">
        <link href="css/bootstrap-icons.css" rel="stylesheet">
        <link href="css/vk.css" rel="stylesheet">

        <title><fmt:message key="add_game_title"/></title>
    </head>

<body>
    <header>
        <%@include file= "../WEB-INF/jspf/navbar.jspf" %>
        <%@include file= "../WEB-INF/jspf/message.jspf" %>
    </header>



    <section>
    <div class="container mt-5">
        <form class="row g-3 needs-validation" novalidate method="post" action="upload_image" enctype="multipart/form-data">
            <div>
                <input type="hidden" name="command" value="create_game">
            </div>
            <div class="col-md-5">
                <label for="inputName" class="form-label"><fmt:message key="add_game_name"/></label>
                <input type="text" class="form-control" id="inputName" name="game_name" value=""
                       pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                <div class="invalid-feedback">
                    <fmt:message key="add_game_name_invalid_message"/>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-5">
                <label for="inputPublisher" class="form-label"><fmt:message key="add_game_publisher"/></label>
                <input type="text" class="form-control" id="inputPublisher" name="publisher" value=""
                       pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                <div class="invalid-feedback">
                    <fmt:message key="add_game_publisher_invalid_message"/>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-md-5">
                <label for="inputDeveloper" class="form-label"><fmt:message key="add_game_developer"/></label>
                <input type="text" class="form-control" id="inputDeveloper" name="developer" value=""
                       pattern="[\p{Alpha}\d][\p{Alpha}\d\s']{1,29}" required>
                <div class="invalid-feedback">
                    <fmt:message key="add_game_developer_invalid_message"/>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-3">
                <label for="inputReleaseDate" class="form-label"><fmt:message key="add_game_release_date"/></label>
                <input type="date" class="form-control" id="inputReleaseDate"
                       name="release_date" value="" required>
            </div>


            <div class="w-100"></div>
            <div class="col-3">
                <label for="inputPlatform" class="form-label"><fmt:message key="add_game_platform"/></label>
                <div id="inputPlatform">
                    <c:forEach var="platform" items="${platforms}">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="platform" id="${platform.name()}" value="${platform}">
                            <label class="form-check-label" for="${platform.name()}">
                                    ${platform.name().replace("_", " ")}
                            </label>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-3">
                <label for="inputPrice" class="form-label"><fmt:message key="add_game_price"/></label>
                <input type="number" class="form-control" id="inputPrice" placeholder="0"
                       name="price" value="" min="0.01" max="200" step="0.01" required>
                <div class="invalid-feedback">
                    <fmt:message key="add_game_price_invalid_message"/>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-5">
                <p>
                    <button class="btn btn-primary" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                        <fmt:message key="add_game_genre"/>
                    </button>
                </p>
                <div class="collapse" id="collapseExample">
                    <c:forEach var="genre" items="${genres}">
                        <div class="form-check form-check-inline" id="inputGenre">
                            <input class="form-check-input" type="checkbox" name="genre" id="${genre.name()}" value="${genre}">
                            <label class="form-check-label text-capitalize" for="${genre.name()}">
                                    ${genre.name().replace("_", " ").toLowerCase()}
                            </label>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-5">
                <label for="inputTrailer" class="form-label"><fmt:message key="add_game_trailer"/></label>
                <input type="url" class="form-control" id="inputTrailer" name="trailer" value=""
                       pattern="https://www\.youtube\.com/watch\?v=[\w_-]{5,20}" required>
                <div class="invalid-feedback">
                    <fmt:message key="add_game_trailer_invalid_message"/>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-5">
                <label for="inputDescription" class="form-label"><fmt:message key="add_game_description"/></label>
                <textarea class="form-control" id="inputDescription" name="game_description" rows="3"
                          maxlength="1000" required></textarea>
            </div>

            <div class="w-100"></div>
            <div class="col-5">
                <label for="inputImage" class="form-label"><fmt:message key="add_game_image"/></label>
                <div class="input-group mb-3" id="inputImage">
                    <input type="file" class="form-control" id="inputGroupFile02" name="image">
                    <label class="input-group-text" for="inputGroupFile02"><fmt:message key="upload"/></label>
                </div>
            </div>

            <div class="w-100"></div>
            <div class="col-12 text-center">
                <button type="submit" class="btn btn-primary"><fmt:message key="add_game"/></button>
            </div>
        </form>
    </div>
    </section>

    <%@include file= "../WEB-INF/jspf/footer.jspf" %>

    <script src="js/reload.js"></script>
    <script src="js/validation.js"></script>
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
