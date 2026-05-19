<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Пополнение баланса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<div class="action-card">
    <p class="main-message">Пополнение баланса</p>
    <p class="message">Введите сумму на которую вы хотите пополнить баланс</p>

    <form action="${pageContext.request.contextPath}/controller" class="action-form" method="post">
        <input type="hidden" name="command" value="upp_balance"/>

        <label for="amount" class="form-label"></label>
        <input type="text" id="amount" class="form-input" name="amount"/>

        <button type="submit" class="btn btn-dark">Пополнить баланс</button>

        <p class="error-message">${error_msg}</p>
    </form>
    <hr/>
    <a href="${pageContext.request.contextPath}/controller?command=go_to_main" class="btn btn-light">На главную</a>
</div>
</body>
</html>