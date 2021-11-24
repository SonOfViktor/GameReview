
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="local.pagecontent"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <title><fmt:message key="sing_up.title"/></title>
</head>
<body>

<%@include file= "../WEB-INF/jspf/navbar_light.jspf" %>
<c:if test="${not empty user_data_message}">
    <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
        <symbol id="exclamation-triangle-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889
                    0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1
                    5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
        </symbol>
    </svg>

    <div class="alert alert-danger d-flex align-items-center" role="alert">
        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
            <use xlink:href="#exclamation-triangle-fill"/>
        </svg>
        <div>
            <fmt:message key="${user_data_message}"/>
        </div>
    </div>
<%--    <c:remove var="sing_up_message_error" scope="session"/>--%>
</c:if>

<div class="container mt-5">
    <form class="row g-3 needs-validation" novalidate method="post" action="upload_image" enctype="multipart/form-data">
        <div>
            <input type="hidden" name="command" value="create_user">
        </div>
        <div class="col-md-5">
            <label for="inputEmail" class="form-label"><fmt:message key="login.login"/></label>
            <input type="email" class="form-control" id="inputEmail" name="login" value=""
                   pattern="[\d\w-]{3,25}@\w{2,10}\.\w{2,5}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.login.invalid_message"/>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="col-5">
            <label for="inputPassword" class="form-label"><fmt:message key="login.password"/></label>
            <input type="password" class="form-control" id="inputPassword" name="password" value=""
                   pattern="(?=.*\d)(?=.*\p{Lower})(?=.*\p{Upper})[\d\p{Alpha}]{8,30}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.password.invalid_message"/>
            </div>
        </div>
        <div class="col-5 mt-4">
            <span id="passwordHelpInline" class="form-text">
              <fmt:message key="sing_up.password_message"/>
            </span>
        </div>

        <div class="w-100"></div>
        <div class="col-md-5">
            <label for="repeatPassword" class="form-label"><fmt:message key="sing_up.repeat_password"/></label>
            <input type="password" class="form-control" id="repeatPassword" name="password_check" value=""
                   pattern="(?=.*\d)(?=.*\p{Lower})(?=.*\p{Upper})[\d\p{Alpha}]{8,30}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.password.invalid_message"/>
            </div>
            <div class="text-danger">
                <c:if test="${not empty password_message}">
                    <fmt:message key="${password_message}"/>
                </c:if>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="col-5">
            <label for="inputName" class="form-label"><fmt:message key="sing_up.name"/></label>
            <input type="text" class="form-control" id="inputName" placeholder="<fmt:message key="sing_up.name"/>"
                   name="name" value="" pattern="[\p{Alpha}А-Яа-яЁё]{2,20}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.name.invalid_message"/>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="col-5">
            <label for="inputSurname" class="form-label"><fmt:message key="sing_up.surname"/></label>
            <input type="text" class="form-control" id="inputSurname" placeholder="<fmt:message key="sing_up.surname"/>"
                   name="surname" value="" pattern="[\p{Alpha}А-Яа-яЁё]{2,20}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.surname.invalid_message"/>
            </div>
        </div>

        <!--Найти способо поставить текущую дату минус 18 лет -->
        <div class="w-100"></div>
        <div class="col-3">
            <label for="inputBirthday" class="form-label"><fmt:message key="sing_up.birthday"/></label>
            <input type="date" class="form-control" id="inputBirthday"
                   name="birthday" value="" required>
            <%--            <div class="invalid-feedback">--%>
            <%--                <fmt:message key="sing_up.birthday.invalid_message"/>--%>
            <%--            </div>--%>
            <div class="text-danger">
                <c:if test="${not empty birthday_message}">
                    <fmt:message key="${birthday_message}"/>
                </c:if>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="input-group w-25">
            <label for="inputTelephone" class="form-label col-12"><fmt:message key="sing_up.phone"/></label>
            <span class="input-group-text" id="telephone">+375</span>
            <input type="tel" class="form-control" id="inputTelephone" placeholder="XX-XXX-XX-XX" name="phone" value=""
                   aria-describedby="telephone" pattern="\d{2}-?\d{3}-?\d{2}-?\d{2}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.phone.invalid_message"/>
            </div>
        </div>

<%--        <div class="w-100"></div>--%>
<%--        <div class="col-12">--%>
<%--            <label for="inputTelephone" class="form-label"><fmt:message key="sing_up.phone"/></label>--%>
<%--        </div>--%>
<%--        <div class="col-auto pt-1">--%>
<%--            <span id="telHelp" class="form-text">--%>
<%--                <jsp:text>+375</jsp:text>--%>
<%--            </span>--%>
<%--        </div>--%>
<%--        <div class="col-3">--%>
<%--            <input type="tel" class="form-control" id="inputTelephone" placeholder="XX-XXX-XX-XX" name="phone" value=""--%>
<%--                   pattern="\d{2}-?\d{3}-?\d{2}-?\d{2}" required>--%>
<%--            <div class="invalid-feedback">--%>
<%--                <fmt:message key="sing_up.phone.invalid_message"/>--%>
<%--            </div>--%>
<%--        </div>--%>

        <div class="w-100"></div>
        <div class="col-5">
            <label for="inputImage" class="form-label">Выберите фото</label>
            <div class="input-group mb-3" id="inputImage">
                <input type="file" class="form-control" id="inputGroupFile02" name="image">
                <label class="input-group-text" for="inputGroupFile02">Upload</label>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="col-12">
            <div class="position-absolute start-50 pb-5">
                <button type="submit" class="btn btn-primary"><fmt:message key="login.sing_up"/></button>
            </div>
        </div>
    </form>
</div>

<script src="js/validation.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>
