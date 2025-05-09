<%@ include file="header.jsp" %>

<h2>Submit a Review</h2>
<form action="ReviewServlet" method="post">
    <label>Your Review:</label><br>
    <textarea name="reviewText" rows="4" cols="50" required></textarea><br><br>
    <input type="submit" value="Submit Review">
</form>

<%@ include file="footer.jsp" %>
