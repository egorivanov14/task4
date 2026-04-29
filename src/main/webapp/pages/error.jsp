<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<p>Тип: ${requestScope['jakarta.servlet.error.exception_type']}</p>
<br/>
<p>Сообщение: ${requestScope['jakarta.servlet.error.message']}</p>
<form action="${pageContext.request.contextPath}/controller" method="get">
    <input type="hidden" name="command" value="return_to_previous_page"/>
    <input type="submit" value="Вернуться на последнюю страницу"/>
</form>
</body>
</html>