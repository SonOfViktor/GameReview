
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <title><fmt:message key="sing_up.title"/></title>
</head>
<body>

<%@include file= "../WEB-INF/jspf/navbar_light.jspf" %>

<%@include file= "../WEB-INF/jspf/message.jspf" %>

<section>
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
            <label for="repeatPassword" class="form-label"><fmt:message key="repeat_password"/></label>
            <input type="password" class="form-control" id="repeatPassword" name="password_check"
                   pattern="(?=.*\d)(?=.*\p{Lower})(?=.*\p{Upper})[\d\p{Alpha}]{8,30}" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.password.invalid_message"/>
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
                   name="birthday" required>
            <div class="invalid-feedback">
                <fmt:message key="sing_up.birthday.invalid_message"/>
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

        <div class="w-100"></div>
        <div class="col-5">
            <label for="inputImage" class="form-label"><fmt:message key="choose_photo"/></label>
            <div class="input-group mb-3" id="inputImage">
                <input type="file" class="form-control" id="inputGroupFile02" name="image" accept="image/*">
                <label class="input-group-text" for="inputGroupFile02"><fmt:message key="upload"/></label>
            </div>
        </div>

        <div class="w-100"></div>
        <div class="col-12 text-center">
            <button type="submit" class="btn btn-primary"><fmt:message key="login.sing_up"/></button>
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
