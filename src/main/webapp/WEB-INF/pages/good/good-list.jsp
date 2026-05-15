<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>goods</title>
</head>
<body>
<h1>Маркетплейс</h1>
<c:choose>
    <c:when test="${empty good_dto_list}">
        <p>Нет товаров</p>
        <a href="${pageContext.request.contextPath}/controller?command=go_to_add_good">добавить товар</a>
    </c:when>
    <c:otherwise>
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
                    <td>
                        <form action="${pageContext.request.contextPath}/controller" method="post">
                            <input type="hidden" name="command" value="add_shopping_cart_item_from_marketplace"/>
                            <input type="hidden" name="good_id" value="${good.id}"/>
                            <input type="submit" value="добавить в корзину"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        ${error_msg}
    </c:otherwise>
</c:choose>
<hr/>
<a href="${pageContext.request.contextPath}/controller?command=go_to_main">На главную</a>
</body>
</html>