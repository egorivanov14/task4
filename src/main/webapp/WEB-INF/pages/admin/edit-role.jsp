<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Изменение роли</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="action-card">
    <p class="main-message">Изменение роли</p>
    <p class="message">Введите имя пользователя и назначьте новую роль</p>

    <form action="${pageContext.request.contextPath}/controller" method="post" class="action-form">
        <input type="hidden" name="command" value="edit_role"/>

        <label for="username" class="form-label">Имя пользователя</label>
        <input type="text" id="username" class="form-input" name="to_username" placeholder="username" required/>

        <label for="role" class="form-label">Новая роль</label>
        <select id="role" name="role" class="form-input" required>
            <option value="" disabled selected>Выберите роль</option>
            <option value="ROLE_USER">Пользователь (USER)</option>
            <option value="ROLE_ADMIN">Администратор (ADMIN)</option>
        </select>

        <button type="submit" class="btn btn-dark">Сохранить изменения</button>

        <p class="error-message">${error_msg}</p>
    </form>

    <a href="${pageContext.request.contextPath}/controller?command=go_to_main" class="btn btn-light">На главную</a>
</div>
</body>
</html>