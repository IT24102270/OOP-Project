<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.drivingschool.model.Admin" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Assign Instructors</title>
    <link rel="stylesheet" href="<c:url value='css/lessonBookingStyles.css'/>">
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
            max-width: 600px;
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
        .assign-button {
            padding: 12px 20px;
            background-color: #1E3A8A;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            margin: 10px;
        }
        .assign-button:hover {
            background-color: #3B82F6;
        }
        .error, .success {
            margin-bottom: 15px;
            font-size: 14px;
        }
        .error {
            color: red;
        }
        .success {
            color: green;
        }
        .error:empty, .success:empty {
            display: none;
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
    </style>
</head>
<body>
<!-- HEADER START -->
<div class="upper-header">
    <div class="padded_area">
        <div class="logo-div">
            <a href="index.jsp"><img src="img/IgnitionHub-logo.png" alt="IgnitionHub Logo" /></a>
        </div>
        <div style="clear:both;"></div>
    </div>
</div>
<header class="header">
    <div class="padded_area">
        <nav>
            <ul>
                <li><a href="index.jsp">HOME</a></li>
                <li><a href="about-us.php.html">ABOUT US</a></li>
                <li><a href="services.php.html">SERVICES</a></li>
                <li><a href="prices.php.html">PRICES</a></li>
                <li><a href="contact.php.html">CONTACT</a></li>
            </ul>
        </nav>
        <div style="clear:both;"></div>
    </div>
</header>
<!-- HEADER END -->

<div class="container">
    <h1>Assign Instructors</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="success"><c:out value="${successMessage}" /></div>
    </c:if>

    <%
        Admin admin = (Admin) session.getAttribute("user");
        if (admin == null) {
    %>
    <div class="error">Please login to access this page.</div>
    <p class="login-link"><a href="adminLogin.jsp">Go to Login</a></p>
    <%
    } else {
    %>
    <p>Click below to assign instructors to confirmed bookings.</p>
    <form action="<c:url value='/assignInstructors'/>" method="post">
        <button type="submit" class="assign-button">Assign Instructors</button>
    </form>
    <p class="login-link"><a href="<c:url value='/jsp/adminDashboard.jsp'/>">Back to Dashboard</a></p>
    <%
        }
    %>
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
                <a href="index.jsp"><img src="img/circle.png" /></a>
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