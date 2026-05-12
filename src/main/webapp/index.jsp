<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<p class="welcome-message">
    Добро пожаловать!
</p>
<p class="message">
    Есть аккаунт?
</p>
<a href="pages/start/login.jsp">
    <button type="button">Войти</button>
</a>
<p class="message">
    Впервые на сайте?
</p>
<a href="pages/start/register.jsp">
    <button type="button">Зарегистрироваться</button>
</a>
</body>
</html>