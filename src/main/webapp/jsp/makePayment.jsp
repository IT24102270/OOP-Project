<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.drivingschool.model.Student" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IgnitionHub - Make Payment</title>
    <link rel="stylesheet" href="<c:url value='css/lessonBookingStyles.css'/>">
    <style>
        .payment-form {
            max-width: 500px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .payment-form label {
            display: block;
            margin: 10px 0 5px;
        }
        .payment-form input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .payment-form button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .payment-form button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-bottom: 10px;
            font-size: 14px;
        }
        .error:empty {
            display: none;
        }
        .back-link {
            display: inline-block;
            margin-top: 10px;
            color: #1E3A8A;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
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
    <h1>Make Payment</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <c:choose>
        <c:when test="${empty sessionScope.user || empty sessionScope.bookingId || empty sessionScope.bookingLessonTotalAmount}">
            <div class="error">Please complete a booking before making a payment.</div>
            <p class="login-link"><a href="<c:url value='/jsp/lessonBooking.jsp'/>">Go to Booking</a></p>
        </c:when>
        <c:otherwise>
            <div class="payment-form">
                <h2>Payment Details</h2>
                <p><strong>Total Amount:</strong> $<c:out value="${sessionScope.bookingLessonTotalAmount}" /></p>
                <p><strong>Payment Method:</strong> Card</p>
                <form id="payment-form" action="<c:url value='/payment'/>" method="post">
                    <input type="hidden" name="bookingId" value="${sessionScope.bookingId}">
                    <input type="hidden" name="studentId" value="${sessionScope.user.id}">
                    <input type="hidden" name="amount" value="${sessionScope.bookingLessonTotalAmount}">
                    <label for="cardNumber">Card Number</label>
                    <input type="text" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456" value="${param.cardNumber}" required>
                    <label for="expiry">Expiry Date (MM/YY)</label>
                    <input type="text" id="expiry" name="expiry" placeholder="MM/YY" value="${param.expiry}" maxlength="5" required>
                    <label for="cvv">CVV</label>
                    <input type="text" id="cvv" name="cvv" placeholder="123" value="${param.cvv}" required>
                    <div class="error" id="form-error"></div>
                    <button type="submit">Pay</button>
                </form>
                <a href="<c:url value='/studentDashboard'/>" class="back-link">Back to Dashboard</a>
            </div>
        </c:otherwise>
    </c:choose>
    <p class="login-link">
        <a href="mailto:support@drivingschool.com">Contact Support</a> |
        <a href="#">Cancellation Policy</a>
    </p>
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
    // Format expiry date input
    document.getElementById('expiry').addEventListener('input', function(event) {
        let value = event.target.value.replace(/[^0-9]/g, '');
        if (value.length >= 3) {
            value = value.slice(0, 2) + '/' + value.slice(2);
        }
        event.target.value = value.slice(0, 5);
    });

    // Prevent non-digit input
    document.getElementById('expiry').addEventListener('keydown', function(event) {
        const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
        if (!allowedKeys.includes(event.key) && !/^\d$/.test(event.key)) {
            event.preventDefault();
        }
    });

    // Form validation on submit
    document.getElementById('payment-form').addEventListener('submit', function(event) {
        const cardNumber = document.getElementById('cardNumber').value.replace(/[\s-]/g, '');
        const expiry = document.getElementById('expiry').value;
        const cvv = document.getElementById('cvv').value;
        const errorDiv = document.getElementById('form-error');
        errorDiv.textContent = '';

        if (!/^\d{16}$/.test(cardNumber)) {
            event.preventDefault();
            errorDiv.textContent = 'Card number must be 16 digits.';
            return;
        }

        if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiry)) {
            event.preventDefault();
            errorDiv.textContent = 'Expiry must be in MM/YY format.';
            return;
        }

        const [month, year] = expiry.split('/').map(Number);
        const currentDate = new Date();
        const currentYear = currentDate.getFullYear() % 100;
        const currentMonth = currentDate.getMonth() + 1;
        if (year < currentYear || (year === currentYear && month < currentMonth)) {
            event.preventDefault();
            errorDiv.textContent = 'Card has expired.';
            return;
        }

        if (!/^\d{3}$/.test(cvv)) {
            event.preventDefault();
            errorDiv.textContent = 'CVV must be 3 digits.';
            return;
        }
    });
</script>
</body>
</html>