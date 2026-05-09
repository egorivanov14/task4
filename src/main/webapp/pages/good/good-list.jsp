<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>goods</title>
</head>
<body>
<h1>Маркетплейс</h1>
<table>
    <tr>
        <td>name</td>
        <td>price</td>
        <td>quantity</td>
        <td>description</td>
        <td>manufacturer</td>
    </tr>
    <c:forEach var="good" items="${good_dto_list}">
        <tr>
            <td>${good.name}</td>
            <td>${good.price}</td>
            <td>${good.quantity}</td>
            <td>${good.description}</td>
            <td>${good.manufacturer}</td>
            <td><form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="add_shopping_cart_item"/>
                <input type="hidden" name="good_id" value="${good.id}"/>
                <input type="submit" value="добавить в корзину"/>
            </form></td>
        </tr>
    </c:forEach>
</table>
${error_msg}
<hr/>
<a href="${pageContext.request.contextPath}/pages/user/main.jsp">На главную</a>
</body>
</html>