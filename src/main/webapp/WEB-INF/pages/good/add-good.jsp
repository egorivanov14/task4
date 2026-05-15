<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>add-good</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="add_good"/>
    <label for="name">Название товара</label>
    <input type="text" id="name" name="name" placeholder="name" required/>
    <br/>
    <label for="price">Цена</label>
    <input type="text" id="price" name="price" placeholder="price" required/>
    <br/>
    <label for="quantity">Количество на складе</label>
    <input type="text" id="quantity" name="quantity" placeholder="quantity" required/>
    <br/>
    <label for="manufacturer">Производитель</label>
    <input type="text" id="manufacturer" name="manufacturer" placeholder="manufacturer" required/>
    <br/>
    <label for="description">Описание</label>
    <input type="text" id="description" name="description" placeholder="description" required/>
    <br/><br/>
    <input type="submit" value="добавить новый товар">
</form>
${info_msg}
<br/>
${error_msg}
<br/>
<a href="${pageContext.request.contextPath}/controller?command=go_to_main">На главную</a>
</body>
</html>