<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.driving.model.Lesson" %>
<html>
<head>
    <title>Manage Lesson - Driving School</title>
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
    <h1>Edit Lesson</h1>
    <%
        Lesson lesson = (Lesson) request.getAttribute("lesson");
        if (lesson != null) {
    %>
    <form action="lesson" method="post" aria-label="Edit Lesson Form">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="<%= lesson.getId() %>">
        <label for="studentId">Student ID:</label>
        <input type="text" id="studentId" name="studentId" value="<%= lesson.getStudentId() %>" required aria-required="true">
        <label for="instructorId">Instructor ID:</label>
        <input type="text" id="instructorId" name="instructorId" value="<%= lesson.getInstructorId() %>" required aria-required="true">
        <label for="vehicleType">Vehicle Type:</label>
        <select id="vehicleType" name="vehicleType" required aria-required="true">
            <option value="Light Vehicle" <%= lesson.getVehicleType().equals("Light Vehicle") ? "selected" : "" %>>Light Vehicle</option>
            <option value="Heavy Vehicle" <%= lesson.getVehicleType().equals("Heavy Vehicle") ? "selected" : "" %>>Heavy Vehicle</option>
            <option value="Two-Wheeler" <%= lesson.getVehicleType().equals("Two-Wheeler") ? "selected" : "" %>>Two-Wheeler</option>
            <option value="Tricycle" <%= lesson.getVehicleType().equals("Tricycle") ? "selected" : "" %>>Tricycle</option>
            <option value="Disabled" <%= lesson.getVehicleType().equals("Disabled") ? "selected" : "" %>>Disabled</option>
        </select>
        <label for="lessonType">Lesson Type:</label>
        <select id="lessonType" name="lessonType" required aria-required="true">
            <option value="Basic Handling" <%= lesson.getLessonType().equals("Basic Handling") ? "selected" : "" %>>Basic Handling</option>
            <option value="Road Rules" <%= lesson.getLessonType().equals("Road Rules") ? "selected" : "" %>>Road Rules</option>
            <option value="Advanced Maneuvers" <%= lesson.getLessonType().equals("Advanced Maneuvers") ? "selected" : "" %>>Advanced Maneuvers</option>
        </select>
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" value="<%= lesson.getDate() %>" required aria-required="true">
        <label for="time">Time:</label>
        <input type="time" id="time" name="time" value="<%= lesson.getTime() %>" required aria-required="true">
        <label for="status">Status:</label>
        <select id="status" name="status" required aria-required="true">
            <option value="Scheduled" <%= lesson.getStatus().equals("Scheduled") ? "selected" : "" %>>Scheduled</option>
            <option value="Completed" <%= lesson.getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
            <option value="Cancelled" <%= lesson.getStatus().equals("Cancelled") ? "selected" : "" %>>Cancelled</option>
        </select>
        <button type="submit">Update Lesson</button>
    </form>
    <%
        }
    %>
    <a href="lesson?action=view" class="btn">View All Lessons</a>
</div>
<script src="../js/script.js"></script>
</body>
</html>