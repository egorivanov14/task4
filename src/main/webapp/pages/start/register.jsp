<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>registration</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <h1>Регистрация</h1>
    <input type="hidden" name="command" value="register"/>
    <input type="text" name="username" placeholder="user name"/>
    <br/>
    <input type="password" name="password" placeholder="password"/>
    <br/>
    <input type="submit" value="Зарегистрироваться"/>
    <br/>
    ${error_msg}
</form>
<a href="${pageContext.request.contextPath}/pages/start/login.jsp">Войти</a>
</body>
</html>