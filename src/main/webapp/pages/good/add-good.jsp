<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>add-good</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="add_good"/>
    <input type="text" name="name" placeholder="name"/>
    <input type="text" name="price" placeholder="price"/>
    <input type="text" name="quantity" placeholder="quantity"/>
    <input type="text" name="manufacturer" placeholder="manufacturer"/>
    <input type="text" name="description" placeholder="description"/>
    <hr/>
    <input type="submit" value="добавить новый товар">
</form>
${info_msg}
<hr/>
${error_msg}
<hr/>
<a href="${pageContext.request.contextPath}/pages/user/main.jsp">На главную</a>
</body>
</html>