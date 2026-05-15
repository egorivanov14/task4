<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добро пожаловать</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="card">
    <p class="main-message">Добро пожаловать!</p>
    <p class="message">Есть аккаунт?</p>
    <a href="${pageContext.request.contextPath}/controller?command=go_to_login" class="btn btn-dark">Войти</a>
    <p class="message">Впервые на сайте?</p>
    <a href="${pageContext.request.contextPath}/controller?command=go_to_register" class="btn btn-light">Зарегистрироваться</a>
</div>
</body>
</html>