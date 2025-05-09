<%@ page import="main.java.com.drivingschool.model.Admin" %>
<%@ page import="main.java.com.drivingschool.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Driving School</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .admin-sidebar {
            background-color: #2c3e50;
            min-height: 100vh;
        }
        .admin-sidebar a {
            color: white;
        }
        .admin-sidebar .nav-link.active {
            background-color: #3498db;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 admin-sidebar p-3">
            <h4 class="text-white text-center mb-4"><i class="bi bi-speedometer2"></i> Dashboard</h4>
            <ul class="nav nav-pills flex-column">
                <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" href="#adminTab"><i class="bi bi-person-gear me-2"></i> Admins</a></li>
                <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#userTab"><i class="bi bi-people me-2"></i> Users</a></li>
                <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#allUsersTab"><i class="bi bi-table me-2"></i> All Users</a></li>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 p-4">
            <div class="tab-content">
                <!-- Admins -->
                <div class="tab-pane fade show active" id="adminTab">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2><i class="bi bi-person-gear"></i> Admin Management</h2>
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addAdminModal"><i class="bi bi-plus"></i> Add Admin</button>
                    </div>
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                        <tr><th>Admin ID</th><th>User ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                        <% for (Admin admin : Admin.getAllAdmins()) { %>
                            <tr>
                                <td><%= admin.getAdminID() %></td>
                                <td><%= admin.getUserid() %></td>
                                <td><%= admin.getUsername() %></td>
                                <td><%= admin.getUserEmail() %></td>
                                <td>
                                    <form action="AdminServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="deleteAdmin">
                                        <input type="hidden" name="adminID" value="<%= admin.getAdminID() %>">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- Users -->
                <div class="tab-pane fade" id="userTab">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2><i class="bi bi-people"></i> User Management</h2>
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addUserModal"><i class="bi bi-plus"></i> Add User</button>
                    </div>
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                        <tr><th>User ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                        <% for (User user : Admin.getAllUsers()) { %>
                            <tr>
                                <td><%= user.getUserid() %></td>
                                <td><%= user.getUsername() %></td>
                                <td><%= user.getUserEmail() %></td>
                                <td>
                                    <form action="AdminServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="deleteUser">
                                        <input type="hidden" name="userID" value="<%= user.getUserid() %>">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- All Users with Edit -->
                <div class="tab-pane fade" id="allUsersTab">
                    <h2><i class="bi bi-table"></i> All Users - Edit Info</h2>
                    <table class="table table-bordered table-striped">
                        <thead class="table-light">
                        <tr><th>User ID</th><th>Name</th><th>Address</th><th>Phone</th><th>Email</th><th>Role</th><th>Edit</th></tr>
                        </thead>
                        <tbody>
                        <% for (User user : Admin.getAllUsers()) { %>
                            <tr>
                                <form action="AdminServlet" method="post">
                                    <td><%= user.getUserid() %><input type="hidden" name="userID" value="<%= user.getUserid() %>"></td>
                                    <td><input name="name" value="<%= user.getUsername() %>" class="form-control"></td>
                                    <td><input name="address" value="<%= user.getUserAddress() %>" class="form-control"></td>
                                    <td><input name="phone" value="<%= user.getPhoneNumber() %>" class="form-control"></td>
                                    <td><input name="email" value="<%= user.getUserEmail() %>" class="form-control"></td>
                                    <td><input name="role" value="<%= user.getRole() %>" class="form-control"></td>
                                    <td>
                                        <input type="hidden" name="action" value="updateUser">
                                        <button type="submit" class="btn btn-success btn-sm"><i class="bi bi-save"></i></button>
                                    </td>
                                </form>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Admin Modal -->
<div class="modal fade" id="addAdminModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="AdminServlet" method="post" class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Add Admin</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="addAdmin">
                <div class="mb-3"><label class="form-label">Admin ID</label><input name="adminID" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">User ID</label><input name="userID" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Name</label><input name="name" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Address</label><input name="address" class="form-control"></div>
                <div class="mb-3"><label class="form-label">Phone</label><input name="phone" class="form-control"></div>
                <div class="mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Password</label><input type="password" name="password" class="form-control" required></div>
            </div>
            <div class="modal-footer"><button type="submit" class="btn btn-primary">Save</button></div>
        </form>
    </div>
</div>

<!-- Add User Modal -->
<div class="modal fade" id="addUserModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="AdminServlet" method="post" class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Add User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="addUser">
                <div class="mb-3"><label class="form-label">User ID</label><input name="userID" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Name</label><input name="name" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Address</label><input name="address" class="form-control"></div>
                <div class="mb-3"><label class="form-label">Phone</label><input name="phone" class="form-control"></div>
                <div class="mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Password</label><input type="password" name="password" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Role</label><input name="role" class="form-control" placeholder="e.g. student" required></div>
            </div>
            <div class="modal-footer"><button type="submit" class="btn btn-primary">Save</button></div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
