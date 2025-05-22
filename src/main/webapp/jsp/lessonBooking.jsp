<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.drivingschool.model.Student" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Book Driving Lessons</title>
    <link rel="stylesheet" href="<c:url value='css/lessonBookingStyles.css'/>">
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
    <h1>Book Your Driving Lessons</h1>

    <%-- Display error or success message --%>
    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${not empty param.successMessage}">
        <div class="success"><c:out value="${param.successMessage}" /></div>
    </c:if>

    <%-- Check if user is logged in --%>
    <%
        Student student = (Student) session.getAttribute("user");
        if (student == null) {
    %>
    <div class="error">Please login as a student to book lessons.</div>
    <p class="login-link"><a href="<c:url value='/jsp/studentLogin.jsp'/>">Go to Login</a></p>
    <%
    } else {
        // Get session attributes for form state
        String bookingVehicleType = (String) session.getAttribute("bookingVehicleType");
        String[] bookingLessons = (String[]) session.getAttribute("bookingLessons");
        String bookingExamPrep = (String) session.getAttribute("bookingExamPrep");
        Double bookingLessonTotalAmount = (Double) session.getAttribute("bookingLessonTotalAmount");

        // Clear session attributes to prevent stale data
        session.removeAttribute("bookingVehicleType");
        session.removeAttribute("bookingLessons");
        session.removeAttribute("bookingExamPrep");
        session.removeAttribute("bookingLessonTotalAmount");
    %>
    <form id="booking-form" action="<c:url value='/lessonBooking'/>" method="post">
        <input type="hidden" name="studentId" value="<%= student.getId() %>">

        <!-- Step 1: Vehicle Type Selection -->
        <div class="form-step" id="step-vehicle">
            <h2>Step 1: Choose Vehicle Type</h2>
            <label>
                <input type="radio" name="vehicle-type" value="light"
                <c:choose>
                       <c:when test="${param['vehicle-type'] == 'light'}">checked</c:when>
                       <c:when test="${empty param['vehicle-type'] && bookingVehicleType == 'light'}">checked</c:when>
                </c:choose> onchange="showLessons()"> Light Vehicle
            </label>
            <label>
                <input type="radio" name="vehicle-type" value="heavy"
                <c:choose>
                       <c:when test="${param['vehicle-type'] == 'heavy'}">checked</c:when>
                       <c:when test="${empty param['vehicle-type'] && bookingVehicleType == 'heavy'}">checked</c:when>
                </c:choose> onchange="showLessons()"> Heavy Vehicle
            </label>
        </div>

        <!-- Step 2: Lesson Selection -->
        <div class="form-step" id="step-lessons" style="display: none;">
            <h2>Step 2: Select Lessons</h2>
            <div id="light-lessons" style="display: none;">
                <c:set var="lessons" value="${paramValues.lesson != null ? paramValues.lesson : bookingLessons}" />
                <label><input type="checkbox" name="lesson" value="motorcycle" <c:forEach var="lesson" items="${lessons}"><c:if test="${lesson == 'motorcycle'}">checked</c:if></c:forEach>> Motorcycle Training ($50)</label>
                <label><input type="checkbox" name="lesson" value="car" <c:forEach var="lesson" items="${lessons}"><c:if test="${lesson == 'car'}">checked</c:if></c:forEach>> Car Training ($60)</label>
                <label><input type="checkbox" name="lesson" value="three-wheeler" <c:forEach var="lesson" items="${lessons}"><c:if test="${lesson == 'three-wheeler'}">checked</c:if></c:forEach>> Three-Wheeler Training ($55)</label>
            </div>
            <div id="heavy-lessons" style="display: none;">
                <label><input type="checkbox" name="lesson" value="truck" <c:forEach var="lesson" items="${lessons}"><c:if test="${lesson == 'truck'}">checked</c:if></c:forEach>> Truck Training ($100)</label>
                <label><input type="checkbox" name="lesson" value="bus" <c:forEach var="lesson" items="${lessons}"><c:if test="${lesson == 'bus'}">checked</c:if></c:forEach>> Bus Training ($90)</label>
            </div>
        </div>

        <!-- Step 3: Written Exam Prep -->
        <div class="form-step" id="step-exam-prep" style="display: none;">
            <h2>Step 3: Written Exam Prep</h2>
            <label>
                <input type="checkbox" name="exam-prep" value="written-exam"
                <c:choose>
                       <c:when test="${param['exam-prep'] == 'written-exam'}">checked</c:when>
                       <c:when test="${empty param['exam-prep'] && bookingExamPrep == 'written-exam'}">checked</c:when>
                </c:choose> onchange="updateTotal()"> Include Written Exam Prep ($30)
            </label>
        </div>

        <!-- Step 4: Payment -->
        <div class="form-step" id="step-payment" style="display: none;">
            <h2>Step 4: Confirm Booking</h2>
            <p>Total: <span id="total-cost">$0</span></p>
            <button type="submit">Proceed to Payment</button>
        </div>

        <button type="button" onclick="nextStep()" id="next-button" style="display: none;">Next</button>
    </form>
    <p class="login-link">
        <a href="<c:url value='/studentDashboard'/>">Back to Dashboard</a> |
        <a href="mailto:support@drivingschool.com">Contact Support</a> |
        <a href="#">Cancellation Policy</a>
    </p>
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

<script>
    let currentStep = 1;

    function showLessons() {
        const vehicleType = document.querySelector('input[name="vehicle-type"]:checked');
        if (!vehicleType) {
            console.log("No vehicle type selected");
            return;
        }
        console.log("Showing lessons for vehicle type:", vehicleType.value);
        document.getElementById('step-lessons').style.display = 'block';
        document.getElementById('next-button').style.display = 'block';
        document.getElementById('light-lessons').style.display = vehicleType.value === 'light' ? 'block' : 'none';
        document.getElementById('heavy-lessons').style.display = vehicleType.value === 'heavy' ? 'block' : 'none';
        updateTotal();
    }

    function nextStep() {
        if (currentStep === 1) {
            const vehicleSelected = document.querySelector('input[name="vehicle-type"]:checked');
            if (!vehicleSelected) {
                alert('Please select a vehicle type.');
                return;
            }
            currentStep = 2;
            document.getElementById('step-vehicle').style.display = 'none';
            document.getElementById('step-exam-prep').style.display = 'block';
        } else if (currentStep === 2) {
            const lessonsSelected = document.querySelectorAll('input[name="lesson"]:checked').length;
            if (lessonsSelected === 0) {
                alert('Please select at least one lesson.');
                return;
            }
            currentStep = 3;
            document.getElementById('step-lessons').style.display = 'none';
            document.getElementById('step-exam-prep').style.display = 'none';
            document.getElementById('step-payment').style.display = 'block';
            document.getElementById('next-button').style.display = 'none';
        }
        updateTotal();
    }

    function updateTotal() {
        console.log("Updating total...");
        const lessons = document.querySelectorAll('input[name="lesson"]:checked');
        const examPrep = document.querySelector('input[name="exam-prep"]:checked');
        let totalSum = 0;
        const prices = {
            motorcycle: 50,
            car: 60,
            'three-wheeler': 55,
            truck: 100,
            bus: 90,
            'written-exam': 30
        };
        lessons.forEach(lesson => {
            totalSum += prices[lesson.value];
            console.log(`Adding ${lesson.value}: $${prices[lesson.value]}`);
        });
        if (examPrep) {
            totalSum += prices['written-exam'];
            console.log("Adding written-exam: $30");
        }
        // Use session total if available (after error)
        const sessionTotalAmount = <%= session.getAttribute("bookingLessonTotalAmount") != null ? session.getAttribute("bookingLessonTotalAmount") : "null" %>;
        const displayTotal = sessionTotalAmount !== null ? sessionTotalAmount : totalSum;
        console.log("Session total:", sessionTotalAmount, "Calculated total:", totalSum, "Display total:", displayTotal);
        document.getElementById('total-cost').textContent = `$${displayTotal}`;
    }

    // Initialize form state on page load
    window.onload = function() {
        console.log("Window loaded, initializing form state...");
        // Check if form was submitted (error via params)
        const hasError = '<c:out value="${errorMessage != null}" />' === 'true';
        const vehicleType = document.querySelector('input[name="vehicle-type"]:checked');
        const lessonsSelected = document.querySelectorAll('input[name="lesson"]:checked').length;

        console.log("hasError:", hasError, "vehicleType:", vehicleType ? vehicleType.value : "none", "lessonsSelected:", lessonsSelected);

        if (hasError) {
            if (vehicleType) {
                showLessons();
            }
            if (lessonsSelected > 0) {
                currentStep = 3;
                document.getElementById('step-vehicle').style.display = 'none';
                document.getElementById('step-lessons').style.display = 'none';
                document.getElementById('step-exam-prep').style.display = 'none';
                document.getElementById('step-payment').style.display = 'block';
                document.getElementById('next-button').style.display = 'none';
                updateTotal();
            } else if (vehicleType) {
                currentStep = 2;
                document.getElementById('step-vehicle').style.display = 'none';
                document.getElementById('step-exam-prep').style.display = 'block';
                updateTotal();
            }
        }
    };

    // Update total when lessons or exam prep are selected
    document.querySelectorAll('input[name="lesson"]').forEach(checkbox => {
        checkbox.addEventListener('change', updateTotal);
    });
    document.querySelector('input[name="exam-prep"]').addEventListener('change', updateTotal);
</script>
</body>
</html>