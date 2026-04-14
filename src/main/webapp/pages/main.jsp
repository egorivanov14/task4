
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>first page</title>
</head>
<body>
<h1>
    User name: ${username}
</h1>
<form action="controller" method="get">
    <input type="hidden" name="command" value="logout">
    <input type="submit" value="Выйти"/>
</form>
</body>
</html>