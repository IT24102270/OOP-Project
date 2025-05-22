<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Student Dashboard</title>
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
        .status-pending {
            color: orange;
            font-weight: bold;
        }
        .status-confirmed {
            color: blue;
            font-weight: bold;
        }
        .status-assigned {
            color: green;
            font-weight: bold;
        }
        .no-bookings {
            color: #555;
            font-style: italic;
        }
        .debug {
            color: purple;
            font-size: 12px;
            margin-bottom: 10px;
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
    <!-- Debugging: Check session and request attributes -->
    <c:if test="${pageContext.session.getAttribute('user') == null}">
        <div class="debug">DEBUG: Session attribute 'user' is null</div>
    </c:if>
    <c:if test="${requestScope.student == null}">
        <div class="debug">DEBUG: Request attribute 'student' is null</div>
    </c:if>

    <c:if test="${not empty param.successMessage}">
        <div class="success"><c:out value="${param.successMessage}" /></div>
    </c:if>
    <c:choose>
        <c:when test="${empty student}">
            <div class="error">Please login to access this page.</div>
            <p class="login-link"><a href="<c:url value='/jsp/studentLogin.jsp'/>">Go to Login</a></p>
        </c:when>
        <c:otherwise>
            <h1>Welcome, <c:out value="${student.name}"/></h1>
            <p>Manage your driving lessons and reviews below.</p>
            <button onclick="window.location.href='<c:url value='/jsp/lessonBooking.jsp'/>'">Book a Lesson</button>
            <button onclick="window.location.href='<c:url value='/jsp/studentReview.jsp'/>'">Submit Review</button>
            <button onclick="window.location.href='<c:url value='/jsp/allReviews.jsp'/>'">View All Reviews</button>
            <button onclick="window.location.href='<c:url value='/logout'/>'">Logout</button>

            <h2>Your Bookings</h2>
            <c:choose>
                <c:when test="${empty studentBookings}">
                    <p class="no-bookings">You have no confirmed bookings yet. Book a lesson to get started!</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <tr>
                            <th>Booking ID</th>
                            <th>Vehicle Type</th>
                            <th>Lessons</th>
                            <th>Written Exam Prep</th>
                            <th>Total Cost</th>
                            <th>Status</th>
                            <th>Instructor</th>
                        </tr>
                        <c:forEach var="booking" items="${studentBookings}">
                            <c:set var="isPending" value="${pendingBookingIds.contains(booking.bookingId)}"/>
                            <c:set var="isConfirmed" value="${confirmedBookingIds.contains(booking.bookingId)}"/>
                            <c:set var="assignment" value="${null}"/>
                            <c:forEach var="a" items="${assignments}">
                                <c:if test="${a.bookingId eq booking.bookingId}">
                                    <c:set var="assignment" value="${a}"/>
                                </c:if>
                            </c:forEach>
                            <c:set var="status" value="Pending"/>
                            <c:set var="statusClass" value="status-pending"/>
                            <c:set var="instructorInfo" value="Not Assigned"/>
                            <c:choose>
                                <c:when test="${assignment != null}">
                                    <c:set var="status" value="Assigned"/>
                                    <c:set var="statusClass" value="status-assigned"/>
                                    <c:forEach var="instructor" items="${instructors}">
                                        <c:if test="${instructor.id eq assignment.instructorId}">
                                            <c:set var="instructorInfo" value="${instructor.name} (${instructor.yearsOfExperience} years exp)"/>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${isConfirmed}">
                                    <c:set var="status" value="Confirmed"/>
                                    <c:set var="statusClass" value="status-confirmed"/>
                                </c:when>
                                <c:when test="${isPending}">
                                    <c:set var="status" value="Pending"/>
                                    <c:set var="statusClass" value="status-pending"/>
                                </c:when>
                            </c:choose>
                            <tr>
                                <td><c:out value="${booking.bookingId}"/></td>
                                <td><c:out value="${booking.vehicleType}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty booking.lessons}">
                                            None
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="lesson" items="${booking.lessons}" varStatus="loop">
                                                <c:out value="${lesson}"/>
                                                <c:if test="${!loop.last}">,</c:if>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><c:out value="${booking.writtenExamPrep ? 'Yes' : 'No'}"/></td>
                                <td>$<c:out value="${String.format('%.2f', booking.totalCost)}"/></td>
                                <td class="${statusClass}"><c:out value="${status}"/></td>
                                <td><c:out value="${instructorInfo}"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
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