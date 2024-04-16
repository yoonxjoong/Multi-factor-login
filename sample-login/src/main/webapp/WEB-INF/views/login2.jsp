<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            $("#google-authenticator").click(function(e){
                $.ajax({
                    type :"get",
                    url : "/google-authenticator",
                    success : function(data){
                        $("#GoogleAuthenticatorQRGenerator").attr("src", "data:image/png;base64," + data);
                    },
                    error : function(e){
                        console.log(JSON.stringify(e, null, 2))
                    }
                });
            });
        });
    </script>
</head>
<body>
<form action="${pageContext.request.contextPath}/login" method="post">
    <div>
        <label for="googleAuthCode">Google Authenticator:</label>
        <input type="text" id="googleAuthCode" name="googleAuthCode" />
    </div>
    <br>
    <div>
        <button type="submit">Login</button>
        <a href="javascript:void(0);" id="google-authenticator" style="float: right;">Reset Google Authenticator</a>
    </div>
    <div>
        <img id="GoogleAuthenticatorQRGenerator" src="" alt="">
    </div>
</form>

<br>

<form action="${pageContext.request.contextPath}/logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>

</body>
</html>
