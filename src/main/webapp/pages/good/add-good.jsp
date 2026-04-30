<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="add_good"/>
    <input type="text" name="name" placeholder="name"/>
    <input type="text" name="price" placeholder="price"/>
    <input type="text" name="quantity" placeholder="start quantity"/>
    <input type="text" name="manufacturer" placeholder="manufacturer"/>
    <input type="text" name="description" placeholder="description"/>
    <hr/>
    <input type="submit" value="добавить новый товар">
</form>
<a href="${pageContext.request.contextPath}/pages/users/main.jsp">На главную</a>
</body>
</html>