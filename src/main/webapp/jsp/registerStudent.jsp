<%--
  Created by IntelliJ IDEA.
  User: Rajika
  Date: 2025-05-13
  Time: 11:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register Student - Driving School</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
<div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
    <h2 class="text-2xl font-bold mb-6 text-center">Register Student</h2>
    <form action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="register">
        <input type="hidden" name="role" value="student">
        <div class="mb-4">
            <label for="userName" class="block text-sm font-medium text-gray-700">Name</label>
            <input type="text" id="userName" name="userName" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="userDOB" class="block text-sm font-medium text-gray-700">Date of Birth (YYYY-MM-DD)</label>
            <input type="text" id="userDOB" name="userDOB" pattern="\d{4}-\d{2}-\d{2}" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="userAddress" class="block text-sm font-medium text-gray-700">Address</label>
            <input type="text" id="userAddress" name="userAddress" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="phoneNumber" class="block text-sm font-medium text-gray-700">Phone Number</label>
            <input type="tel" id="phoneNumber" name="phoneNumber" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="userEmail" class="block text-sm font-medium text-gray-700">Email</label>
            <input type="email" id="userEmail" name="userEmail" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <div class="mb-4">
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input type="password" id="password" name="password" class="mt-1 block w-full p-2 border border-gray-300 rounded" required>
        </div>
        <button type="submit" class="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">Register</button>
    </form>
    <% String error = request.getParameter("error"); if (error != null) { %>
    <p class="mt-4 text-red-500 text-center"><%= error %></p>
    <% } %>
    <p class="mt-4 text-center"><a href="login.jsp" class="text-blue-600 hover:underline">Already registered? Login here</a></p>
</div>
</body>
</html>