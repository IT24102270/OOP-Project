<%@ include file="header.jsp" %>

<h2>Login</h2>
<form action="LoginServlet" method="post">
    <label>Email:</label>
    <input type="email" name="email" required><br><br>
    <label>Password:</label>
    <input type="password" name="password" required><br><br>
    <input type="submit" value="Login">
</form>

<%@ include file="footer.jsp" %>
