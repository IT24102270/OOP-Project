<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="main.java.com.drivingschool.model.Instructor" %>
<%@ page import="java.util.List" %>
<% System.out.println("Loading instructorDashboard.jsp"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Instructor Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="tabs">
    <button class="tab active-tab" onclick="showTab('listTab')">Instructor List</button>
    <button class="tab" onclick="showTab('addTab')">Add Instructor</button>
    <button class="tab" onclick="showTab('updateTab')">Update Instructor</button>
    <button class="tab" onclick="showTab('removeTab')">Remove Instructor</button>
</div>

<div id="listTab" class="tab-content active-content">
    <h1>Instructor List</h1>
    <% String message = (String) session.getAttribute("message");
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (message != null) { %>
    <p style="color: green;"><%= message %></p>
    <% session.removeAttribute("message"); %>
    <% } %>
    <% if (errorMessage != null) { %>
    <p style="color: red;"><%= errorMessage %></p>
    <% session.removeAttribute("errorMessage"); %>
    <% } %>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Experience (Years)</th>
            <th>Vehicle Classes</th>
        </tr>
        <%
            @SuppressWarnings("unchecked")
            List<Instructor> instructors = (List<Instructor>) session.getAttribute("instructors");
            if (instructors != null) {
                for (Instructor instructor : instructors) { %>
        <tr>
            <td><%= instructor.getInstructorID() %></td>
            <td><%= instructor.getUsername() %></td>
            <td><%= instructor.getUserEmail() %></td>
            <td><%= instructor.getExperienceYear() %></td>
            <td><%= String.join(", ", instructor.getVehicleClasses()) %></td>
        </tr>
        <% } } else { %>
        <tr><td colspan="5">No instructors found.</td></tr>
        <% } %>
    </table>
</div>

<div id="addTab" class="tab-content">
    <h1>Add New Instructor</h1>
    <% if (errorMessage != null) { %>
    <p style="color: red;"><%= errorMessage %></p>
    <% session.removeAttribute("errorMessage"); %>
    <% } %>
    <form action="${pageContext.request.contextPath}/SortingServlet" method="post">
        <input type="hidden" name="action" value="add">
        <label for="addInstructorID">Instructor ID:</label><br>
        <input type="text" id="addInstructorID" name="instructorID" placeholder="Instructor ID" required><br>
        <label for="addName">Name:</label><br>
        <input type="text" id="addName" name="name" placeholder="Name" required><br>
        <label for="addEmail">Email:</label><br>
        <input type="email" id="addEmail" name="email" placeholder="Email" required><br>
        <label for="addExperienceYear">Experience (Years):</label><br>
        <input type="number" id="addExperienceYear" name="experienceYear" placeholder="Experience (Years)" required><br>
        <label for="addVehicleClasses">Vehicle Classes (comma-separated):</label><br>
        <input type="text" id="addVehicleClasses" name="vehicleClasses" placeholder="Vehicle Classes (comma-separated)" required><br>
        <button type="submit">Save Instructor</button>
    </form>
</div>

<div id="updateTab" class="tab-content">
    <h1>Update Instructor Experience</h1>
    <% if (errorMessage != null) { %>
    <p style="color: red;"><%= errorMessage %></p>
    <% session.removeAttribute("errorMessage"); %>
    <% } %>
    <form action="${pageContext.request.contextPath}/SortingServlet" method="post">
        <input type="hidden" name="action" value="update">
        <label for="updateInstructorID">Select Instructor:</label><br>
        <select id="updateInstructorID" name="instructorID" required>
            <% if (instructors != null) {
                for (Instructor instructor : instructors) { %>
            <option value="<%= instructor.getInstructorID() %>">
                <%= instructor.getUsername() %> (Exp: <%= instructor.getExperienceYear() %>)
            </option>
            <% } } %>
        </select><br>
        <label for="updateNewExperience">New Experience (Years):</label><br>
        <input type="number" id="updateNewExperience" name="newExperience" placeholder="New Experience (Years)" required><br>
        <button type="submit">Update Experience</button>
    </form>
</div>

<div id="removeTab" class="tab-content">
    <h1>Remove Instructor</h1>
    <% if (errorMessage != null) { %>
    <p style="color: red;"><%= errorMessage %></p>
    <% session.removeAttribute("errorMessage"); %>
    <% } %>
    <form action="${pageContext.request.contextPath}/SortingServlet" method="post">
        <input type="hidden" name="action" value="remove">
        <label for="removeInstructorID">Select Instructor to Remove:</label><br>
        <select id="removeInstructorID" name="instructorID" required>
            <% if (instructors != null) {
                for (Instructor instructor : instructors) { %>
            <option value="<%= instructor.getInstructorID() %>">
                <%= instructor.getUsername() %> (Exp: <%= instructor.getExperienceYear() %>)
            </option>
            <% } } %>
        </select><br>
        <button type="submit">Remove Instructor</button>
    </form>
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