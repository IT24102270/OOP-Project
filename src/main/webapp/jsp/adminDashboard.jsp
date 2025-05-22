<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Admin Dashboard</title>
    <link rel="stylesheet" href="<c:url value='css/style.css'/>">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: url('https://www.sterling.lk/wp-content/uploads/2024/01/tile.jpg');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .container {
            max-width: 800px;
            background: rgba(255, 255, 255, 0.95);
            padding: 30px;
            margin: 80px auto 30px auto;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 {
            color: #1E3A8A;
            margin-bottom: 20px;
        }
        h2 {
            color: #1E3A8A;
            margin: 20px 0;
        }
        button {
            padding: 12px 20px;
            background-color: #1E3A8A;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            margin: 10px;
        }
        button:hover {
            background-color: #3B82F6;
        }
        .error {
            color: red;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #ffe6e6;
            border-radius: 5px;
        }
        .success {
            color: green;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #e6ffe6;
            border-radius: 5px;
        }
        .login-link a {
            color: #1E3A8A;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
        footer {
            margin-top: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #1E3A8A;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #e6f0ff;
        }
        .delete-button {
            background-color: #dc3545;
        }
        .delete-button:hover {
            background-color: #c82333;
        }
        .no-users {
            color: #555;
            font-style: italic;
        }
    </style>
    <script>
        function confirmDelete(name, role) {
            // Escape quotes to prevent JavaScript string issues
            var safeName = name.replace(/'/g, "\\'").replace(/"/g, '\\"');
            return confirm("Are you sure you want to delete " + role + " '" + safeName + "'?");
        }
    </script>
</head>
<body>
<!-- HEADER START -->
<div class="upper-header">
    <div class="padded_area">
        <div class="logo-div">
            <a href="<c:url value='/index.jsp'/>"><img src="img/IgnitionHub-logo.png" alt="IgnitionHub Logo" /></a>
        </div>
        <div style="clear:both;"></div>
    </div>
</div>
<header class="header">
    <div class="padded_area">
        <nav>
            <ul>
                <li><a href="<c:url value='/index.jsp'/>">HOME</a></li>
                <li><a href="<c:url value=''/>">ABOUT US</a></li>
                <li><a href="<c:url value=''/>">SERVICES</a></li>
                <li><a href="<c:url value=''/>">PRICES</a></li>
                <li><a href="<c:url value=''/>">CONTACT</a></li>
            </ul>
        </nav>
        <div style="clear:both;"></div>
    </div>
</header>
<!-- HEADER END -->

<div class="container">
    <!-- Messages -->
    <c:if test="${not empty param.successMessage}">
        <div class="success"><c:out value="${param.successMessage}" /></div>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <div class="error"><c:out value="${requestScope.errorMessage}" /></div>
    </c:if>

    <c:if test="${empty admin}">
        <div class="error">Please login to access this page.</div>
        <p class="login-link"><a href="<c:url value='/jsp/adminLogin.jsp'/>">Go to Login</a></p>
    </c:if>
    <c:if test="${not empty admin}">
        <h1>Welcome, <c:out value="${admin.name}"/></h1>
        <p>Manage driving school operations below.</p>
        <button onclick="window.location.href='<c:url value='/jsp/adminQueue.jsp'/>'">Manage Booking Queue</button>
        <button onclick="window.location.href='<c:url value='/jsp/adminAssignInstructors.jsp'/>'">Assign Instructors</button>
        <button onclick="window.location.href='<c:url value='/logout'/>'">Logout</button>

        <h2>Students</h2>
        <c:if test="${empty students}">
            <p class="no-users">No students registered.</p>
        </c:if>
        <c:if test="${not empty students}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="student" items="${students}">
                    <tr>
                        <td><c:out value="${student.id}"/></td>
                        <td><c:out value="${student.name}"/></td>
                        <td><c:out value="${student.email}"/></td>
                        <td><c:out value="${student.number}"/></td>
                        <td>
                            <form action="<c:url value='/adminDashboard'/>" method="post" onsubmit="return confirmDelete('<c:out value="${fn:escapeXml(student.name)}"/>', 'student')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="role" value="student">
                                <input type="hidden" name="id" value="<c:out value="${student.id}"/>">
                                <button type="submit" class="delete-button">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <h2>Instructors</h2>
        <c:if test="${empty instructors}">
            <p class="no-users">No instructors registered.</p>
        </c:if>
        <c:if test="${not empty instructors}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Years of Experience</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="instructor" items="${instructors}">
                    <tr>
                        <td><c:out value="${instructor.id}"/></td>
                        <td><c:out value="${instructor.name}"/></td>
                        <td><c:out value="${instructor.email}"/></td>
                        <td><c:out value="${instructor.number}"/></td>
                        <td><c:out value="${instructor.yearsOfExperience}"/></td>
                        <td>
                            <form action="<c:url value='/adminDashboard'/>" method="post" onsubmit="return confirmDelete('<c:out value="${fn:escapeXml(instructor.name)}"/>', 'instructor')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="role" value="instructor">
                                <input type="hidden" name="id" value="<c:out value="${instructor.id}"/>">
                                <button type="submit" class="delete-button">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:if>
</div>

<!-- FOOTER START -->
<footer class="footer">
    <div class="padded_area">
        <div class="footer3">
            <h3>IgnitionHub</h3>
            <h5>Grade "A" Driving School</h5>
            <div class="col3box">
                <img src="img/pointer11.png" class="ico" />
                <h4>67/E/B, Katuwawala, <br />Boralesgamuwa.</h4>
                <div style="clear:both;"></div>
                <img src="img/phone.png" class="ico" />
                <h4>011 2 509116<br />077 3135202</h4>
                <div style="clear:both;"></div>
                <img src="img/big-new-email.png" class="ico" />
                <h4>IgnitionHub@gmail.com</h4>
                <div style="clear:both;"></div>
                <img src="img/reg.png" class="ico" />
                <h4>Reg.No: DS 282</h4>
                <div style="clear:both;"></div>
            </div>
            <div class="social">
                <a href="https://www.facebook.com/IgnitionHub/"><img src="img/social.png" /></a>
                <a href="<c:url value='/index.jsp'/>"><img src="img/circle.png" /></a>
            </div>
        </div>
        <div style="clear:both;"></div>
    </div>
</footer>
<div class="after-foooter">
    <div class="padded_area">
        <div class="col2">
            <h5>© <script>document.write(new Date().getFullYear());</script> IgnitionHub</h5>
        </div>
        <div style="clear:both;"></div>
    </div>
</div>
<!-- FOOTER END -->
</body>
</html>