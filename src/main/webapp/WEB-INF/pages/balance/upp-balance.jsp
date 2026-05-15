<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>upp balance</title>
</head>
<body>
<h1>Пополнение баланса</h1>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="upp_balance"/>
    <input type="text" name="amount"/>
    <input type="submit" value="пополнить баланс">
</form>
${error_msg}
${message}
<hr/>
<a href="${pageContext.request.contextPath}/controller?command=go_to_main">На главную</a>
</body>
</html>