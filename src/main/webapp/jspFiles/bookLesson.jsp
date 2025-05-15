<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Lesson - Driving School</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            background-color: #f4f7fa;
            color: #333;
            line-height: 1.6;
            /* Background image related to driving */
            background-image: url('https://images.unsplash.com/photo-1502877338535-766e1452684a?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            position: relative;
        }

        /* Overlay for blur effect */
        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            backdrop-filter: blur(8px); /* Apply blur effect */
            z-index: -1; /* Place behind content */
        }

        /* Navigation Bar */
        .navbar {
            background-color: rgba(44, 62, 80, 0.9); /* Semi-transparent for blur visibility */
            padding: 1rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .navbar ul {
            list-style: none;
            display: flex;
            justify-content: center;
            gap: 2rem;
        }

        .navbar li a {
            color: #fff;
            text-decoration: none;
            font-weight: 500;
            font-size: 1.1rem;
            transition: color 0.3s ease;
        }

        .navbar li a:hover {
            color: #3498db;
        }

        /* Container */
        .container {
            max-width: 600px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: rgba(255, 255, 255, 0.95); /* Semi-transparent white for readability */
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            position: relative;
            z-index: 1; /* Ensure above blurred background */
        }

        .container h1 {
            font-size: 1.8rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 1.5rem;
            text-align: center;
        }

        /* Form Styling */
        form {
            display: flex;
            flex-direction: column;
            gap: 1.2rem;
        }

        label {
            font-size: 1rem;
            font-weight: 500;
            color: #2c3e50;
        }

        input[type="text"],
        input[type="date"],
        input[type="time"],
        select {
            width: 100%;
            padding: 0.8rem;
            font-size: 1rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            transition: border-color 0.3s ease;
            background-color: #fff; /* Ensure inputs are opaque */
        }

        input:focus,
        select:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }

        select {
            appearance: none;
            background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24"><path fill="%23333" d="M7 10l5 5 5-5z"/></svg>') no-repeat right 0.75rem center/12px;
        }

        button {
            background-color: #3498db;
            color: #fff;
            padding: 0.8rem;
            font-size: 1.1rem;
            font-weight: 500;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #2980b9;
        }

        /* Error Message (for future use) */
        .error {
            color: #e74c3c;
            font-size: 0.9rem;
            margin-top: 0.2rem;
        }

        /* Responsive Design */
        @media (max-width: 600px) {
            .container {
                margin: 1rem;
                padding: 1.5rem;
            }

            .navbar ul {
                flex-direction: column;
                align-items: center;
                gap: 1rem;
            }

            .navbar li a {
                font-size: 1rem;
            }

            .container h1 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
<nav class="navbar">
    <ul>
        <li><a href="lesson">Book Lesson</a></li>
        <li><a href="lesson?action=view">View Lessons</a></li>
    </ul>
</nav>
<div class="container">
    <h1>Book a New Lesson</h1>
    <form action="lesson" method="post" aria-label="Book Lesson Form">
        <input type="hidden" name="action" value="book">
        <label for="studentId">Student ID:</label>
        <input type="text" id="studentId" name="studentId" required aria-required="true">
        <label for="instructorId">Instructor ID:</label>
        <input type="text" id="instructorId" name="instructorId" required aria-required="true">
        <label for="vehicleType">Vehicle Type:</label>
        <select id="vehicleType" name="vehicleType" required aria-required="true">
            <option value="" disabled selected>Select Vehicle Type</option>
            <option value="Light Vehicle">Light Vehicle</option>
            <option value="Heavy Vehicle">Heavy Vehicle</option>
            <option value="Two-Wheeler">Two-Wheeler</option>
            <option value="Tricycle">Tricycle</option>
            <option value="Disabled">Disabled</option>
        </select>
        <label for="lessonType">Lesson Type:</label>
        <select id="lessonType" name="lessonType" required aria-required="true">
            <option value="" disabled selected>Select Lesson Type</option>
            <option value="Basic Handling">Basic Handling</option>
            <option value="Road Rules">Road Rules</option>
            <option value="Advanced Maneuvers">Advanced Maneuvers</option>
        </select>
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" required aria-required="true">
        <label for="time">Time:</label>
        <input type="time" id="time" name="time" required aria-required="true">
        <button type="submit">Book Lesson</button>
    </form>
</div>
<script src="../js/script.js"></script>
</body>
</html>