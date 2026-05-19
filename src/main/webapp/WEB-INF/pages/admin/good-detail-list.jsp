<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список товаров</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="table-card">
    <h1 class="page-title">Список всех товаров</h1>

    <c:choose>
        <c:when test="${empty good_detail_dto_list}">
            <p class="empty-message">Список товаров пуст</p>
        </c:when>
        <c:otherwise>
            <div class="table-wrapper">
                <table class="goods-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Цена</th>
                        <th>Остаток</th>
                        <th>Производитель</th>
                        <th>Описание</th>
                        <th>Добавил</th>
                        <th>Действие</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="good" items="${good_detail_dto_list}">
                        <tr>
                            <td class="cell-id">${good.id}</td>
                            <td class="cell-name">${good.name}</td>
                            <td class="cell-price">${good.price} ₽</td>
                            <td class="cell-quantity">
                                        <span class="quantity-badge ${good.quantity <= 0 ? 'out-of-stock' : ''}">
                                                ${good.quantity}
                                        </span>
                            </td>
                            <td class="cell-manufacturer">${good.manufacturer}</td>
                            <td class="cell-description">${good.description}</td>
                            <td class="cell-addedby">${good.addedByUsername}</td>
                            <td class="cell-action">
                                <form action="${pageContext.request.contextPath}/controller" method="post" class="delete-form">
                                    <input type="hidden" name="command" value="delete_good_by_admin"/>
                                    <input type="hidden" name="good_id" value="${good.id}"/>
                                    <button type="submit" class="btn-delete" onclick="return confirm('Удалить товар?')">Удалить</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <p class="error-message">${error_msg}</p>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/controller?command=go_to_main" class="back-link">← На главную</a>
</div>
</body>
</html>