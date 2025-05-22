<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin Registration | IgnitionHub</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/style.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-image: url('https://www.sterling.lk/wp-content/uploads/2024/01/tile.jpg');
      background-size: cover;
      background-position: center;
      background-repeat: no-repeat;
      margin: 0;
      min-height: 100vh;
    }

    .header {
      position: relative;
    }

    .container {
      max-width: 500px;
      background: rgba(255, 255, 255, 0.95);
      padding: 30px;
      margin: 80px auto 30px auto;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #1E3A8A;
    }

    label {
      display: block;
      margin-top: 15px;
      font-weight: bold;
      color: #333;
    }

    input[type="text"],
    input[type="date"],
    input[type="email"],
    input[type="tel"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border: 1px solid #ccc;
      border-radius: 5px;
      font-size: 15px;
    }

    button {
      width: 100%;
      margin-top: 25px;
      padding: 12px;
      background-color: #1E3A8A;
      color: white;
      border: none;
      border-radius: 5px;
      font-size: 16px;
      cursor: pointer;
    }

    button:hover {
      background-color: #3B82F6;
    }

    .error {
      color: red;
      text-align: center;
      margin-bottom: 15px;
    }

    .login-link {
      text-align: center;
      margin-top: 10px;
    }

    .login-link a {
      color: #007bff;
      text-decoration: none;
    }

    .login-link a:hover {
      text-decoration: underline;
    }
  </style>
  <script>
    function validateForm() {
      var password = document.getElementById("password").value;
      var confirmPassword = document.getElementById("confirmPassword").value;
      var number = document.getElementById("number").value;
      var phonePattern = /^\d{10}$/;

      if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return false;
      }
      if (!phonePattern.test(number)) {
        alert("Phone number must be 10 digits!");
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
      <a href="index.php.html"><img src="img/IgnitionHub-logo.png" /></a>
    </div>
    <div style="clear:both;"></div>
  </div>
</div>
<header class="header">
  <div class="padded_area">
    <nav>
      <ul>
        <li><a href="index.php.html">HOME</a></li>
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
  <h2>Admin Registration</h2>
  <c:if test="${not empty errorMessage}">
    <div class="error"><c:out value="${errorMessage}" /></div>
  </c:if>
  <form action="<c:url value='/user'/>" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="action" value="register">
    <input type="hidden" name="role" value="admin">
    <label for="name">Full Name</label>
    <input type="text" id="name" name="name" value="<c:out value='${param.name}'/>" required>

    <label for="dob">Date of Birth</label>
    <input type="date" id="dob" name="dob" value="<c:out value='${param.dob}'/>" required>

    <label for="number">Phone Number</label>
    <input type="tel" id="number" name="number" value="<c:out value='${param.number}'/>" required>

    <label for="address">Address</label>
    <input type="text" id="address" name="address" value="<c:out value='${param.address}'/>" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="password" required>

    <label for="confirmPassword">Confirm Password</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required>

    <button type="submit">Register</button>
  </form>
  <div class="login-link">
    <p>Already have an account? <a href="<c:url value='/jsp/adminLogin.jsp'/>">Login here</a></p>
    <p>Student? <a href="<c:url value='/jsp/studentLogin.jsp'/>">Login as Student</a></p>
    <p>Instructor? <a href="<c:url value='/jsp/instructorLogin.jsp'/>">Login as Instructor</a></p>
  </div>
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
        <a href="index.php.html"><img src="img/circle.png" /></a>
      </div>
    </div>
    <div style="clear:both;"></div>
  </div>
</footer>
<div class="after-foooter">
  <div class="padded_area">
    <div class="col2">
      <h5>&copy; <script>document.write(new Date().getFullYear());</script> IgnitionHub</h5>
    </div>
    <div style="clear:both;"></div>
  </div>
</div>
<!-- FOOTER END -->

</body>
</html>
