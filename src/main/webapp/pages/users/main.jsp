<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>main page</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/pages/admin/edit-role.jsp">Страница смены роли</a>
<br/>
<a href="${pageContext.request.contextPath}/controller?command=get_users_list">Страница всех пользователей</a>
<br/>
<a href="${pageContext.request.contextPath}/controller?command=get_profile">Профиль пользователя</a>
<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="logout">
    <input type="submit" value="Выйти"/>
</form>
</body>
</html>