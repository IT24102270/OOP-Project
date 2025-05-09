<%@ page import="main.java.com.drivingschool.model.Review" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Review Management</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="tabs">
    <button class="tab active-tab" onclick="showTab('reviewTab')">Review Management</button>
</div>

<div id="reviewTab" class="tab-content active-content">
    <h1>Review Management</h1>

    <form action="ReviewServlet" method="post">
        <input type="hidden" name="action" value="addReview">
        <input type="text" name="reviewID" placeholder="Review ID" required>
        <input type="text" name="userID" placeholder="User ID" required>
        <textarea name="comment" placeholder="Comment" required></textarea>
        <input type="number" name="rating" placeholder="Rating (1-5)" min="1" max="5" required>
        <button type="submit">Submit Review</button>
    </form>

    <h2>Existing Reviews</h2>
    <table>
        <tr>
            <th>Review ID</th>
            <th>User ID</th>
            <th>Comment</th>
            <th>Rating</th>
            <th>Actions</th>
        </tr>
        <%
            for (Review review : Review.getAllReviews()) {
        %>
        <tr>
            <td><%= review.getReviewID() %></td>
            <td><%= review.getUserID() %></td>
            <td><%= review.getComment() %></td>
            <td><%= review.getRating() %></td>
            <td>
                <form action="ReviewServlet" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="deleteReview">
                    <input type="hidden" name="reviewID" value="<%= review.getReviewID() %>">
                    <button type="submit" class="delete-btn">Delete</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>

<script>
    function showTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active-content'));
        document.getElementById(tabId).classList.add('active-content');
        document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active-tab'));
        document.querySelector(`[onclick="showTab('${tabId}')"]`).classList.add('active-tab');
    }
</script>
</body>
</html>
