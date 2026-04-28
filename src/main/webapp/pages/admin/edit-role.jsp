<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>role-editor</title>
</head>
<body>
<h2>
    Введите имя пользователя и новую роль
</h2>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="edit_role"/>
    <input type="text" name="to_username" placeholder="username"/>
    <input type="text" name="role" placeholder="new role"/>
    <br/>
    <input type="submit" value="Дать новую роль"/>
    <br/>
    ${error_msg}
</form>
<a href="${pageContext.request.contextPath}/pages/users/main.jsp">На главную</a>
</body>
</html>