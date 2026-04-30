<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Список доступных товаров</h1>
<table>
    <tr>
        <td>id</td>
        <td>name</td>
        <td>price</td>
        <td>quantity</td>
        <td>manufacturer</td>
        <td>description</td>
    </tr>
    <hr/>
    <c:forEach var="good" items="${good_list}">
        <tr>
            <td>${good.id}</td>
            <td>${good.name}</td>
            <td>${good.price}</td>
            <td>${good.quantity}</td>
            <td>${good.manufacturer}</td>
            <td>${good.description}</td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/pages/users/main.jsp">На главную</a>
</body>
</html>