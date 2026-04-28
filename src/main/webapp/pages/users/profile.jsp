<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>username: ${user.username}</h2>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="update_username"/>
    <input type="text" name="username" placeholder="введите новое имя"/>
    <input type="submit" value="Изменить имя"/>
</form>
<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="delete_current_user"/>
    <input type="submit" value="Удалить профиль"/>
    <br/>
</form>
${error_msg}
<a href="${pageContext.request.contextPath}/pages/users/main.jsp">На главную</a>
</body>
</html>