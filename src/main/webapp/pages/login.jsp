<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <h1>Вход</h1>
    <br/>
    <input type="hidden" name="command" value="login"/>
    <input type="text" name="username" placeholder="user name"/>
    <br/>
    <input type="password" name="password" placeholder="password"/>
    <br/>
    <input type="submit" value="Войти"/>
    <br/>
    ${error_msg}
</form>
<h2>Нет аккаунта?</h2>
<a href="${pageContext.request.contextPath}/pages/register.jsp">Зарегистрироваться</a>
</body>
</html>