<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
</head>
<body>
<form action="login" method="post">
    <!-- 注意用户名和密码英文还不能乱写这个和springSecurity 源码一致 -->
    用户名：<input type="text" name="username"><br>
    密&nbsp;&nbsp;&nbsp;码: <input type="password" name="password"><br>
    <input type="submit" value="登录">
</form>
</body>
</html>