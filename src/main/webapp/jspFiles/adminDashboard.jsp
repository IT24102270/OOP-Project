<%@ page import="com.drivingschool.model.Admin" %>
<%@ page import="com.drivingschool.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="tabs">
    <button class="tab active-tab" onclick="showTab('adminTab')">Admin Management</button>
    <button class="tab" onclick="showTab('userTab')">User Management</button>
</div>

<div id="adminTab" class="tab-content active-content">
    <h1>Admin Management</h1>
    <form action="AdminServlet" method="post">
        <input type="hidden" name="action" value="addAdmin">
        <input type="text" name="adminID" placeholder="Admin ID" required>
        <input type="text" name="userID" placeholder="User ID" required>
        <input type="text" name="name" placeholder="Name" required>
        <input type="text" name="address" placeholder="Address">
        <input type="text" name="phone" placeholder="Phone">
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Save Admin</button>
    </form>

    <h2>Existing Admins</h2>
    <table>
        <tr>
            <th>Admin ID</th>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        <%
            for (Admin admin : Admin.getAllAdmins()) {
        %>
        <tr>
            <td><%= admin.getAdminID() %></td>
            <td><%= admin.getUserid() %></td>
            <td><%= admin.getUsername() %></td>
            <td><%= admin.getUserEmail() %></td>
            <td>
                <form action="AdminServlet" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="deleteAdmin">
                    <input type="hidden" name="adminID" value="<%= admin.getAdminID() %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>

<div id="userTab" class="tab-content">
    <h1>User Management</h1>
    <form action="AdminServlet" method="post">
        <input type="hidden" name="action" value="addUser">
        <input type="text" name="userID" placeholder="User ID" required>
        <input type="text" name="name" placeholder="Name" required>
        <input type="text" name="address" placeholder="Address">
        <input type="text" name="phone" placeholder="Phone">
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Save User</button>
    </form>

    <h2>Existing Users</h2>
    <table>
        <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        <%
            for (User user : Admin.getAllUsers()) {
        %>
        <tr>
            <td><%= user.getUserid() %></td>
            <td><%= user.getUsername() %></td>
            <td><%= user.getUserEmail() %></td>
            <td>
                <form action="AdminServlet" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="deleteUser">
                    <input type="hidden" name="userID" value="<%= user.getUserid() %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>

<script>
    function showTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active-content'));
        document.getElementById(tabId).classList.add('active-content');
        document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active-tab'));
        document.querySelector(`[onclick="showTab('${tabId}')"]`).classList.add('active-tab');
    }
</script>
</body>
</html>
