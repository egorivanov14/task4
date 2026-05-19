<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="login-register-card">
    <p class="main-message">Регистрация</p>

    <form action="${pageContext.request.contextPath}/controller" class="login-register-form" method="post">
        <input type="hidden" name="command" value="register"/>

        <label for="username" class="form-label">Имя</label>
        <input type="text" id="username" class="form-input" name="username" placeholder="user name" required/>

        <label for="password" class="form-label">Пароль</label>
        <input type="password" id="password" class="form-input" name="password" placeholder="••••••••" required/>

        <p class="error-message">${error_msg}</p>

        <button type="submit" class="btn btn-dark">Зарегистрироваться</button>
    </form>

    <div class="divider">
        <span class="divider-text">или</span>
    </div>

    <a href="${pageContext.request.contextPath}/controller?command=go_to_login" class="btn btn-light">Войти</a>
</div>
</body>
</html>