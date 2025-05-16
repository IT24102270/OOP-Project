<%--
  Created by IntelliJ IDEA.
  User: Rajika
  Date: 2025-05-11
  Time: 6:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Driving School</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
<div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
    <h2 class="text-2xl font-bold mb-6 text-center">Login</h2>
    <form action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="login">
        <div class="mb-4">
            <label for="userIdOrEmail" class="block text-sm font-medium text-gray-700">User ID or Email</label>
            <input type="text" id="userIdOrEmail" name="userIdOrEmail" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input type="password" id="password" name="password" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="role" class="block text-sm font-medium text-gray-700">Role</label>
            <select id="role" name="role" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
                <option value="student">Student</option>
                <option value="instructor">Instructor</option>
                <option value="admin">Admin</option>
            </select>
        </div>
        <button type="submit" class="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">Login</button>
    </form>
    <% String error = request.getParameter("error"); if (error != null) { %>
    <p class="mt-4 text-red-500 text-center"><%= error %></p>
    <% } %>
    <% String success = request.getParameter("success"); if (success != null && success.equals("true")) { %>
    <p class="mt-4 text-green-500 text-center">Registration successful! Please log in.</p>
    <% } %>
    <p class="mt-4 text-center"><a href="registerStudent.jsp" class="text-blue-600 hover:underline">New student? Register here</a></p>
</div>
</body>
</html>