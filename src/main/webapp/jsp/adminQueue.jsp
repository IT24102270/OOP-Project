<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.drivingschool.model.CircularQueue" %>
<%@ page import="com.drivingschool.model.LessonBooking" %>
<%@ page import="com.drivingschool.util.FileHandler" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>IgnitionHub - Admin Queue Management</title>
  <link rel="stylesheet" href="<c:url value='css/lessonBookingStyles.css'/>">
  <style>
    .queue-container {
      max-width: 600px;
      margin: 20px auto;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }
    .queue-container h2 {
      text-align: center;
    }
    .booking-details {
      margin-bottom: 20px;
    }
    .booking-details p {
      margin: 5px 0;
    }
    .confirm-button {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .confirm-button:hover {
      background-color: #45a049;
    }
    .error, .success {
      margin-bottom: 10px;
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
  <h1>Admin Queue Management</h1>

  <c:if test="${not empty errorMessage}">
    <div class="error"><c:out value="${errorMessage}" /></div>
  </c:if>
  <c:if test="${not empty successMessage}">
    <div class="success"><c:out value="${successMessage}" /></div>
  </c:if>

  <div class="queue-container">
    <h2>Pending Booking</h2>
    <%
      CircularQueue bookingQueue = (CircularQueue) application.getAttribute("bookingQueue");
      String bookingId = (bookingQueue != null) ? bookingQueue.peek() : null;
      LessonBooking booking = null;
      if (bookingId != null) {
        String bookingFilePath = application.getRealPath("/WEB-INF/data/bookings.txt");
        FileHandler bookingFileHandler = new FileHandler(bookingFilePath);
        List<LessonBooking> bookings = bookingFileHandler.readAllLessonBookings();
        for (LessonBooking b : bookings) {
          if (b.getBookingId().equals(bookingId)) {
            booking = b;
            break;
          }
        }
      }
    %>
    <% if (booking != null) { %>
    <div class="booking-details">
      <p><strong>Booking ID:</strong> <%= booking.getBookingId() %></p>
      <p><strong>Student ID:</strong> <%= booking.getStudentId() %></p>
      <p><strong>Vehicle Type:</strong> <%= booking.getVehicleType() %></p>
      <p><strong>Lessons:</strong> <%= String.join(", ", booking.getLessons()) %></p>
      <p><strong>Written Exam Prep:</strong> <%= booking.isWrittenExamPrep() ? "Yes" : "No" %></p>
      <p><strong>Total Cost:</strong> $<%= booking.getTotalCost() %></p>
    </div>
    <form action="<c:url value='/queue'/>" method="post">
      <input type="hidden" name="action" value="confirm">
      <button type="submit" class="confirm-button">Confirm Booking</button>
    </form>
    <% } else { %>
    <p>No bookings in queue.</p>
    <% } %>
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