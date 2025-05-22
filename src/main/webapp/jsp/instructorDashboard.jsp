<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Instructor Dashboard</title>
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
        .status-assigned {
            color: green;
            font-weight: bold;
        }
        .status-not-assigned {
            color: orange;
            font-weight: bold;
        }
        .debug {
            color: purple;
            font-size: 12px;
            margin-bottom: 10px;
        }
        .update-form label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #333;
        }
        .update-form input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
            box-sizing: border-box;
        }
    </style>
    <script>
        function validateForm() {
            var number = document.getElementById("number").value;
            var yearsOfExperience = document.getElementById("yearsOfExperience").value;
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirmPassword").value;
            var phonePattern = /^\d{10}$/;
            var experiencePattern = /^\d+$/;

            if (!phonePattern.test(number)) {
                alert("Phone number must be 10 digits!");
                return false;
            }
            if (!experiencePattern.test(yearsOfExperience) || parseInt(yearsOfExperience) < 0) {
                alert("Years of experience must be a non-negative integer!");
                return false;
            }
            if (password && password !== confirmPassword) {
                alert("Passwords do not match!");
                return false;
            }
            return true;
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
    <!-- Debugging: Check session and request attributes -->
    <c:if test="${pageContext.session.getAttribute('user') == null}">
        <div class="debug">DEBUG: Session attribute 'user' is null</div>
    </c:if>
    <c:if test="${requestScope.instructor == null}">
        <div class="debug">DEBUG: Request attribute 'instructor' is null</div>
    </c:if>

    <c:if test="${not empty param.successMessage}">
        <div class="success"><c:out value="${param.successMessage}" /></div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <c:choose>
        <c:when test="${empty instructor}">
            <div class="error">Please login to access this page.</div>
            <p class="login-link"><a href="<c:url value='/jsp/instructorLogin.jsp'/>">Go to Login</a></p>
        </c:when>
        <c:otherwise>
            <h1>Welcome, <c:out value="${instructor.name}"/></h1>
            <p>Manage your details and view your assignment status below.</p>
            <button onclick="window.location.href='<c:url value='/logout'/>'">Logout</button>

            <h2>Your Details</h2>
            <table>
                <tr>
                    <th>Name</th>
                    <td><c:out value="${instructor.name}"/></td>
                </tr>
                <tr>
                    <th>Date of Birth</th>
                    <td><c:out value="${instructor.dob}"/></td>
                </tr>
                <tr>
                    <th>Phone Number</th>
                    <td><c:out value="${instructor.number}"/></td>
                </tr>
                <tr>
                    <th>Address</th>
                    <td><c:out value="${instructor.address}"/></td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td><c:out value="${instructor.email}"/></td>
                </tr>
                <tr>
                    <th>Years of Experience</th>
                    <td><c:out value="${instructor.yearsOfExperience}"/></td>
                </tr>
                <tr>
                    <th>Assignment Status</th>
                    <td class="${isAssigned ? 'status-assigned' : 'status-not-assigned'}">
                        <c:out value="${isAssigned ? 'Assigned' : 'Not Assigned'}"/>
                    </td>
                </tr>
            </table>

            <h2>Update Details</h2>
            <form class="update-form" action="<c:url value='/instructorDashboard'/>" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="update">
                <label for="name">Full Name</label>
                <input type="text" id="name" name="name" value="<c:out value='${instructor.name}'/>" required>

                <label for="number">Phone Number</label>
                <input type="tel" id="number" name="number" value="<c:out value='${instructor.number}'/>" required>

                <label for="address">Address</label>
                <input type="text" id="address" name="address" value="<c:out value='${instructor.address}'/>" required>

                <label for="yearsOfExperience">Years of Experience</label>
                <input type="number" id="yearsOfExperience" name="yearsOfExperience" value="<c:out value='${instructor.yearsOfExperience}'/>" required min="0">

                <label for="password">New Password (leave blank to keep current)</label>
                <input type="password" id="password" name="password" placeholder="Enter new password">

                <label for="confirmPassword">Confirm New Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password">

                <button type="submit">Update Details</button>
            </form>
        </c:otherwise>
    </c:choose>
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