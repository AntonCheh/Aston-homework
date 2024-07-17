<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spanish Words</title>
</head>
<body>
<h1>Spanish Words</h1>
<table border="1">
    <tr>
        <th>Yo</th>
        <th>Tu</th>
        <th>El/Ella</th>
        <th>Vosotros</th>
        <th>Nosotros</th>
        <th>Ellos</th>
    </tr>
    <c:forEach var="word" items="${spanishWordsList}">
        <tr>
            <td>${word}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>