<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<p>Тип: ${requestScope['jakarta.servlet.error.exception_type']}</p>
<br/>
<p>Сообщение: ${requestScope['jakarta.servlet.error.message']}</p>
<a href="${current_page}">
    <button type="button">Вернуться на последнюю страницу</button>
</a>
</body>
</html>