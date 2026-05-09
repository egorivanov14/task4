<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>goods-detail</title>
</head>
<body>
<h1>Список всех товаров</h1>
<table>
    <tr>
        <td>id</td>
        <td>name</td>
        <td>price</td>
        <td>quantity</td>
        <td>manufacturer</td>
        <td>description</td>
        <td>added by</td>
    </tr>
    <hr/>
    <c:forEach var="good" items="${good_detail_dto_list}">
        <tr>
            <td>${good.id}</td>
            <td>${good.name}</td>
            <td>${good.price}</td>
            <td>${good.quantity}</td>
            <td>${good.manufacturer}</td>
            <td>${good.description}</td>
            <td>${good.addedByUsername}</td>
            <td>
                <form action="${pageContext.request.contextPath}/controller" method="post">
                    <input type="hidden" name = "command" value="delete_good_by_admin"/>
                    <input type="hidden" name="good_id"  value="${good.id}"/>
                    <input type="submit" value="удалить"/>
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