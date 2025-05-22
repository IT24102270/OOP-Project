<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>All Reviews | IgnitionHub</title>
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
    }
    h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #1E3A8A;
    }
    .review {
      border-bottom: 1px solid #ccc;
      padding: 15px 0;
    }
    .review:last-child {
      border-bottom: none;
    }
    .review h3 {
      margin: 0;
      color: #007bff;
    }
    .review p {
      margin: 5px 0;
      color: #555;
    }
    .rating {
      font-size: 18px;
      color: #f39c12;
    }
    .star-filled::before {
      content: '★';
    }
    .star-empty::before {
      content: '☆';
    }
    .no-reviews {
      text-align: center;
      color: #555;
      font-style: italic;
    }
    .back-link {
      text-align: center;
      margin-top: 20px;
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
  <h2>All Reviews</h2>
  <c:choose>
    <c:when test="${empty reviews}">
      <p class="no-reviews">No reviews available.</p>
    </c:when>
    <c:otherwise>
      <c:forEach var="review" items="${reviews}">
        <div class="review">
          <h3><c:out value="${review.studentName}"/></h3>
          <p class="rating">
            <c:forEach begin="1" end="5" var="i">
              <span class="${i <= review.rating ? 'star-filled' : 'star-empty'}"></span>
            </c:forEach>
          </p>
          <p><c:out value="${review.comment}"/></p>
        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
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