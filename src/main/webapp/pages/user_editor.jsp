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

    <title><fmt:message key="user_editor_page"/></title>
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
                <input type="hidden" name="command" value="update_user">
                <div class="row mb-3">
                    <label for="inputFirstName" class="col-3 col-form-label"><fmt:message key="user_name"/></label>
                    <div class="col-8">
                        <input type="text" class="form-control" id="inputFirstName" name="name"
                            value="${user.firstName}" pattern="[\p{Alpha}А-Яа-яЁё]{2,20}" required>
                        <div class="invalid-feedback">
                            <fmt:message key="sing_up.name.invalid_message"/>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputSecondName" class="col-3 col-form-label"><fmt:message key="user_surname"/></label>
                    <div class="col-8">
                        <input type="text" class="form-control" id="inputSecondName" name="surname"
                            value="${user.secondName}" pattern="[\p{Alpha}А-Яа-яЁё]{2,20}" required>
                        <div class="invalid-feedback">
                            <fmt:message key="sing_up.surname.invalid_message"/>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputBirthday" class="col-3 col-form-label"><fmt:message key="user_birthday"/></label>
                    <div class ="col-8">
                        <input type="date" class="form-control" id="inputBirthday"
                               name="birthday" value="${user.birthday}" required>
                        <div class="invalid-feedback">
                            <fmt:message key="sing_up.birthday.invalid_message"/>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPhone" class="col-3 col-form-label"><fmt:message key="user_phone"/></label>
                    <div class="col-8">
                        <div class="input-group mb-3">
                            <span class="input-group-text" id="telephone">+375</span>
                            <input type="tel" class="form-control" id="inputPhone" placeholder="XX-XXX-XX-XX" name="phone"
                                   value="${user.phone}" aria-describedby="telephone" pattern="\d{2}-?\d{3}-?\d{2}-?\d{2}" required>
                            <div class="invalid-feedback">
                                <fmt:message key="sing_up.phone.invalid_message"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-11 d-flex justify-content-end">
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
                <h3><fmt:message key="change_password"/></h3>
            </div>

            <form class="needs-validation" novalidate method="post" action="controller">
                <input type="hidden" name="command" value="update_password">
                <div class="row mb-3">
                    <label for="newPassword" class="col-3 col-form-label"><fmt:message key="new_password"/></label>
                    <div class="col-8">
                        <input type="password" class="form-control" id="newPassword" name="password"
                               pattern="(?=.*\d)(?=.*\p{Lower})(?=.*\p{Upper})[\d\p{Alpha}]{8,30}" required>
                        <div class="invalid-feedback">
                            <fmt:message key="sing_up.password.invalid_message"/>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="repeatPassword" class="col-3 col-form-label"><fmt:message key="repeat_password"/></label>
                    <div class="col-8">
                        <input type="password" class="form-control" id="repeatPassword" name="password_check"
                               pattern="(?=.*\d)(?=.*\p{Lower})(?=.*\p{Upper})[\d\p{Alpha}]{8,30}" required>
                        <div class="invalid-feedback">
                            <fmt:message key="sing_up.password.invalid_message"/>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-11 d-flex justify-content-end">
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
                <h3><fmt:message key="change_photo"/></h3>
            </div>

            <form class="needs-validation" novalidate method="post" action="upload_image" enctype="multipart/form-data">
                <input type="hidden" name="command" value="update_photo">
                <div class="row mb-3">
                    <div class="col-3">
<%--   todo                     <jsp:useBean id="calendar" scope="page" class="java.util.GregorianCalendar"/>--%>
                        <img src="<%--http://localhost:8080/gamereview/--%>${user.photo} <%--?date=${calendar.timeInMillis}--%>" class="shadow bg-white rounded">
                    </div>
                    <div class="col-8 align-self-center">
                        <label for="inputImage" class="form-label"><fmt:message key="choose_photo"/></label>
                        <div class="input-group mb-3" id="inputImage">
                            <input type="file" class="form-control" id="inputGroupFile02" name="image">
                            <label class="input-group-text" for="inputGroupFile02"><fmt:message key="upload"/></label>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-11 d-flex justify-content-end">
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
                <form method="post" action="controller">
                    <input type="hidden" name="command" value="delete_user">
                    <div class="col-12 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary"><fmt:message key="delete_button"/></button>
                    </div>
                </form>
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
