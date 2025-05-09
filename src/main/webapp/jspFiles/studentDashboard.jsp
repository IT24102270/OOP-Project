<%@ page import="com.drivingschool.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("currentUser"); // replace with real session user
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard | Driving School</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 bg-dark text-white p-3 min-vh-100">
            <h4 class="text-center"><i class="bi bi-person-circle"></i> Student Panel</h4>
            <ul class="nav nav-pills flex-column mt-4">
                <li class="nav-item">
                    <a class="nav-link active" data-bs-toggle="tab" href="#profileTab">
                        <i class="bi bi-person-badge-fill me-2"></i> Profile
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#settingsTab">
                        <i class="bi bi-gear-fill me-2"></i> Settings
                    </a>
                </li>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 p-4">
            <div class="tab-content">
                <!-- Profile Tab -->
                <div class="tab-pane fade show active" id="profileTab">
                    <h2><i class="bi bi-person-badge-fill"></i> My Profile</h2>
                    <table class="table table-bordered mt-3">
                        <tr><th>User ID</th><td><%= user.getUserid() %></td></tr>
                        <tr><th>Name</th><td><%= user.getUsername() %></td></tr>
                        <tr><th>Address</th><td><%= user.getUserAddress() %></td></tr>
                        <tr><th>Phone</th><td><%= user.getPhoneNumber() %></td></tr>
                        <tr><th>Email</th><td><%= user.getUserEmail() %></td></tr>
                        <tr><th>Role</th><td><%= user.getRole() %></td></tr>
                    </table>
                    <button class="btn btn-primary mt-2" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                        <i class="bi bi-pencil"></i> Edit Profile
                    </button>
                </div>

                <!-- Settings Tab -->
                <div class="tab-pane fade" id="settingsTab">
                    <h2><i class="bi bi-gear-fill"></i> Account Settings</h2>
                    <button class="btn btn-warning mt-3" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                        <i class="bi bi-key"></i> Change Password
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Edit Profile Modal -->
<div class="modal fade" id="editProfileModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="UserServlet" method="post" class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Edit Profile</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="updateProfile">
                <input type="hidden" name="userID" value="<%= user.getUserid() %>">
                <div class="mb-3">
                    <label class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" value="<%= user.getUsername() %>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Address</label>
                    <input type="text" name="address" class="form-control" value="<%= user.getUserAddress() %>">
                </div>
                <div class="mb-3">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" class="form-control" value="<%= user.getPhoneNumber() %>">
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" value="<%= user.getUserEmail() %>" required>
                </div>
                <!-- Note: Role is not editable -->
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<!-- Change Password Modal -->
<div class="modal fade" id="changePasswordModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="UserServlet" method="post" class="modal-content">
            <div class="modal-header bg-warning">
                <h5 class="modal-title">Change Password</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="changePassword">
                <input type="hidden" name="userID" value="<%= user.getUserid() %>">
                <div class="mb-3">
                    <label class="form-label">Current Password</label>
                    <input type="password" name="currentPassword" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">New Password</label>
                    <input type="password" name="newPassword" class="form-control" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-warning">Change Password</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>