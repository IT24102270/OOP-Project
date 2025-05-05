<%@ include file="header.jsp" %>

<h2>Payment</h2>
<p>Complete your payment to begin lessons.</p>

<form action="PaymentServlet" method="post">
    <label>Card Number:</label>
    <input type="text" name="cardNumber" required><br><br>
    <label>Amount:</label>
    <input type="text" name="amount" value="5000" readonly><br><br>
    <input type="submit" value="Pay Now">
</form>

<%@ include file="footer.jsp" %>
