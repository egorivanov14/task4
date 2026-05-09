<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>shopping cart</title>
</head>
<body>
<h1>Ваша корзина</h1>
<c:choose>
    <c:when test="${empty shopping_cart_item_dto_list}">
        <p>Корзина пуста</p>
        <a href="${pageContext.request.contextPath}/pages/good/good-list.jsp">Добавить товары</a>
    </c:when>
    <c:otherwise>
        <table>
            <tr>
                <td>name</td>
                <td>quantity</td>
                <td>price</td>
            </tr>
            <hr/>
            <c:forEach var="item" items="${shopping_cart_item_dto_list}">
                <tr>
                    <td>${item.goodName}</td>
                    <td>${item.quantity}</td>
                    <td>${item.fullPrice}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/controller" method="post">
                            <input type="hidden" name="command" value="add_shopping_cart_item_from_cart"/>
                            <input type="hidden" name="good_id" value="${item.goodId}"/>
                            <input type="submit" value="+"/>
                        </form>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/controller" method="post">
                            <input type="hidden" name="command" value="remove_item_from_shopping_cart"/>
                            <input type="hidden" name="good_id" value="${item.goodId}"/>
                            <input type="submit" value="-"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        ${error_msg}
    </c:otherwise>
</c:choose>
<hr/>
<a href="${pageContext.request.contextPath}/pages/user/main.jsp">На главную</a>
</body>
</html>