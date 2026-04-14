<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>First servlet</title>
</head>
<body>
<br/>
<form action="controller" method="get">
    <h1>Регистрация</h1>
    <input type="hidden" name="command" value="register"/>
    <input type="text" name="username" placeholder="User name"/>
    <br/>
    <input type="password" name="password" placeholder="Password"/>
    <br/>
    <input type="submit" value="Зарегистрироваться"/>
    <br/>
    ${error_msg}
</form>
</body>
</html>