<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Review | IgnitionHub</title>
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
            max-width: 500px;
            background: rgba(255, 255, 255, 0.95);
            padding: 30px;
            margin: 80px auto 30px auto;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        h2 {
            color: #1E3A8A;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #333;
        }
        select, textarea {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
        }
        textarea {
            height: 100px;
            resize: vertical;
        }
        button {
            width: 100%;
            margin-top: 20px;
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
            margin-bottom: 15px;
        }
        .back-link {
            margin-top: 15px;
        }
        .back-link a {
            color: #007bff;
            text-decoration: none;
        }
        .back-link a:hover {
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
    <h2>Submit Your Review</h2>
    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}" /></div>
    </c:if>
    <form action="<c:url value='/review'/>" method="post">
        <label for="rating">Rating (1-5)</label>
        <select id="rating" name="rating" required>
            <option value="">Select rating</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
        </select>
        <label for="comment">Comment</label>
        <textarea id="comment" name="comment" required placeholder="Share your experience"></textarea>
        <button type="submit">Submit Review</button>
    </form>
    <div class="back-link">
        <p><a href="<c:url value='/jsp/studentDashboard.jsp'/>">Back to Dashboard</a></p>
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