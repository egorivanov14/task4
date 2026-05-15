<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="card">
    <p class="main-message">Войти</p>

    <form action="${pageContext.request.contextPath}/controller" method="post" class="login-form">
        <input type="hidden" name="command" value="login"/>

        <label for="username" class="form-label">Имя</label>
        <input type="text" id="username" name="username" class="form-input" placeholder="user name">

        <label for="password" class="form-label">Пароль</label>
        <input type="password" id="password" name="password" class="form-input" placeholder="••••••••">

        <button type="submit" class="btn btn-dark">Войти</button>
    </form>

    <div class="divider">
        <span class="divider-text">или</span>
    </div>

    <a href="${pageContext.request.contextPath}/controller?command=go_to_register" class="btn btn-light">Зарегистрироваться</a>
</div>
</body>
</html>