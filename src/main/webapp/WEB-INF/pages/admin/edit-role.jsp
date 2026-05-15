<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Role Editor</title>
</head>
<body>
<h2>Введите имя пользователя и новую роль</h2>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="edit_role"/>

    <label for="username">Имя пользователя:</label>
    <input type="text" id="username" name="to_username" placeholder="username" required/>
    <br/><br/>

    <label for="role">Новая роль:</label>
    <select id="role" name="role" required>
        <option value="" disabled selected>Выберите роль</option>
        <option value="ROLE_USER">Пользователь (USER)</option>
        <option value="ROLE_ADMIN">Администратор (ADMIN)</option>
    </select>
    <br/><br/>

    <input type="submit" value="Дать новую роль"/>
    <br/><br/>
    ${error_msg}
    ${message}
</form>
<a href="${pageContext.request.contextPath}/controller?command=go_to_main">На главную</a>
</body>
</html>