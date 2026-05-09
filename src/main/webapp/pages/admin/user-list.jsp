<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>users</title>
</head>
<body>
<table>
    <tr>
        <td>id</td>
        <td>username</td>
        <td>roleId</td>
    </tr>
    <hr/>
    <c:forEach var="user" items="${user_list}">
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.roleName}</td>
            <td>
                <form action="${pageContext.request.contextPath}/controller" method="post">
                    <input type="hidden" name="command" value="delete_user_by_admin"/>
                    <input type="hidden" name="user_id" value="${user.id}">
                    <input type="submit" value="удалить пользователя"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
${error_msg}
<hr/>
<a href="${pageContext.request.contextPath}/pages/user/main.jsp">На главную</a>
</body>
</html>