<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>HTTP Request</title>
</head>
<body>

<h1>HTTP Request</h1>

<form method="POST" style="display: inline-block">
    <input type="submit" value="POST"/>
</form>
<form method="GET" style="display: inline-block">
    <input type="submit" value="GET"/>
</form>

<pre>${request}</pre>

<sec:authorize access="isAnonymous()">
    <form:form action="/login">
        <div>
            <label for="username">Username</label><input type="text" id="username" name="username"/>
        </div>
        <div>
            <label for="password">Password</label><input type="password" id="password" name="password"/>
        </div>
        <div>
            <input type="submit" value="Login"/>
        </div>
    </form:form>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <form:form action="/logout">
        <div>
            <input type="submit" value="Logout"/>
        </div>
    </form:form>
</sec:authorize>

</body>
</html>