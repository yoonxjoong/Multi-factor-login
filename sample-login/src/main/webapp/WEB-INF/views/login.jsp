<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
</head>
<body>

<h2>Login Form</h2>

<c:if test="${param.error != null}">
    <div style="color: red;">
        Invalid username and password.
    </div>
</c:if>
<c:if test="${param.logout != null}">
    <div style="color: green;">
        You have been logged out successfully.
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="post">
    <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" />
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" />
    </div>
    <div>
        <button type="submit">Login</button>
    </div>
</form>

<form action="${pageContext.request.contextPath}/logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>

</body>
</html>
