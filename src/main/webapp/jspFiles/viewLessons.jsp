<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.driving.model.Lesson" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>View Lessons - Driving School</title>
    <link rel="stylesheet" href="../css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<nav class="navbar">
    <ul>
        <li><a href="lesson">Book Lesson</a></li>
        <li><a href="lesson?action=view">View Lessons</a></li>
    </ul>
</nav>
<div class="container">
    <h1>All Lessons</h1>
    <table aria-label="Lessons Table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Student ID</th>
            <th>Instructor ID</th>
            <th>Vehicle Type</th>
            <th>Lesson Type</th>
            <th>Date</th>
            <th>Time</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Lesson> lessons = (List<Lesson>) request.getAttribute("lessons");
            if (lessons != null) {
                for (Lesson lesson : lessons) {
        %>
        <tr>
            <td><%= lesson.getId() %></td>
            <td><%= lesson.getStudentId() %></td>
            <td><%= lesson.getInstructorId() %></td>
            <td><%= lesson.getVehicleType() %></td>
            <td><%= lesson.getLessonType() %></td>
            <td><%= lesson.getDate() %></td>
            <td><%= lesson.getTime() %></td>
            <td><%= lesson.getStatus() %></td>
            <td>
                <a href="lesson?action rubbleedit&id=<%= lesson.getId() %>">Edit</a>
                <form action="lesson" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= lesson.getId() %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
    <a href="lesson" class="btn">Book New Lesson</a>
</div>
<script src="../js/script.js"></script>
</body>
</html>
