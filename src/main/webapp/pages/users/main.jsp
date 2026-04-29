<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>main page</title>
</head>
<body>
<c:if test="${user.admin}">
    <ul>
        <li><a href="${pageContext.request.contextPath}/pages/admin/edit-role.jsp">Страница смены роли</a></li>
        <li><a href="${pageContext.request.contextPath}/controller?command=get_users_list">Страница всех пользователей</a></li>
    </ul>
</c:if>
<ul>
    <li><a href="${pageContext.request.contextPath}/controller?command=get_profile">Профиль пользователя</a></li>
</ul>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="logout">
    <input type="submit" value="Выйти"/>
</form>
</body>
</html>