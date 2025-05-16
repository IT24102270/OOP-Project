package com.drivingschool.servlets;

import com.drivingschool.model.*;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("jsp/error.jsp?message=Invalid action");
            return;
        }

        switch (action) {
            case "register":
                handleRegistration(request, response);
                break;
            case "login":
                handleLogin(request, response);
                break;
            case "updateProfile":
                handleProfileUpdate(request, response);
                break;
            default:
                response.sendRedirect("jsp/error.jsp?message=Unknown action");
                break;
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = request.getParameter("role");
        String userName = request.getParameter("userName");
        String userDOB = request.getParameter("userDOB");
        String userAddress = request.getParameter("userAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String userEmail = request.getParameter("userEmail");
        String password = request.getParameter("password");

        // Basic validation
        if (role == null || userName == null || userDOB == null || userAddress == null ||
                phoneNumber == null || userEmail == null || password == null) {
            response.sendRedirect("jsp/registerStudent.jsp?error=Missing required fields");
            return;
        }

        // Validate DOB format (YYYY-MM-DD)
        if (!userDOB.matches("\\d{4}-\\d{2}-\\d{2}")) {
            response.sendRedirect("jsp/registerStudent.jsp?error=Invalid DOB format (use YYYY-MM-DD)");
            return;
        }

        // Validate phone number (10 digits)
        if (!phoneNumber.matches("\\d{10}")) {
            response.sendRedirect("jsp/registerStudent.jsp?error=Invalid phone number (10 digits required)");
            return;
        }

        // Check for unique email
        if (!isEmailUnique(userEmail)) {
            response.sendRedirect("jsp/registerStudent.jsp?error=Email already in use");
            return;
        }

        // Generate unique userId
        String userId;
        try {
            userId = generateUniqueUserId(role);
        } catch (Exception e) {
            LOGGER.severe("Failed to generate userId: " + e.getMessage());
            response.sendRedirect("jsp/registerStudent.jsp?error=Failed to generate user ID");
            return;
        }

        String redirectUrl = "jsp/registerStudent.jsp?error=Registration failed";
        try {
            switch (role) {
                case "student":
                    Student student = new Student(userName, userDOB, userAddress, phoneNumber, userEmail, password);
                    student.setUserId(userId);
                    student.setRole("student");
                    student.setPaymentStatus("unpaid");
                    student.setLessonsCompleted(0);
                    // Log student data manually
                    String studentLog = String.format("userId=%s,userName=%s,userDOB=%s,userAddress=%s,phoneNumber=%s,userEmail=%s,password=%s,role=%s,lessonsCompleted=%d,paymentStatus=%s,lessonRequests=%s",
                            student.getUserId(), student.getUserName(), student.getUserDOB(), student.getUserAddress(),
                            student.getPhoneNumber(), student.getUserEmail(), student.getPassword(), student.getRole(),
                            student.getLessonsCompleted(), student.getPaymentStatus(),
                            student.getLessonRequests().isEmpty() ? "none" : "pending");
                    LOGGER.info("Attempting to add student: " + studentLog);
                    fileHandler.addStudent(student);
                    redirectUrl = "jsp/lesson.jsp?userId=" + userId;
                    break;
                case "instructor":
                    int experienceYears = Integer.parseInt(request.getParameter("experienceYears"));
                    Instructor instructor = new Instructor(userName, userDOB, userAddress, phoneNumber, userEmail, password, experienceYears);
                    instructor.setUserId(userId);
                    instructor.setRole("instructor");
                    instructor.setAvailable(true);
                    fileHandler.addInstructor(instructor);
                    redirectUrl = "jsp/login.jsp?success=true";
                    break;
                case "admin":
                    Admin admin = new Admin(userName, userDOB, userAddress, phoneNumber, userEmail, password);
                    admin.setUserId(userId);
                    admin.setRole("admin");
                    fileHandler.addAdmin(admin);
                    redirectUrl = "jsp/login.jsp?success=true";
                    break;
                default:
                    response.sendRedirect("jsp/registerStudent.jsp?error=Invalid role");
                    return;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Validation error: " + e.getMessage());
            redirectUrl = "jsp/registerStudent.jsp?error=" + e.getMessage();
        } catch (IOException e) {
            LOGGER.severe("File operation failed: " + e.getMessage());
            redirectUrl = "jsp/registerStudent.jsp?error=File operation failed: " + e.getMessage();
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
            redirectUrl = "jsp/registerStudent.jsp?error=Unexpected error occurred";
        }

        response.sendRedirect(redirectUrl);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIdOrEmail = request.getParameter("userIdOrEmail");
        String password = request.getParameter("password");

        // Find user by userId or email
        User user = findUserByIdOrEmail(userIdOrEmail);

        if (user != null && user.verifyPassword(password)) {
            // Regenerate session to prevent fixation
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("userId", user.getUserId());
            newSession.setAttribute("role", user.getRole());

            // Redirect based on role
            switch (user.getRole()) {
                case "student":
                    response.sendRedirect("jsp/studentDashboard.jsp");
                    break;
                case "instructor":
                    response.sendRedirect("jsp/instructorDashboard.jsp");
                    break;
                case "admin":
                    response.sendRedirect("thinking://jsp/adminDashboard.jsp");
                    break;
                default:
                    response.sendRedirect("jsp/login.jsp?error=Invalid role");
                    break;
            }
        } else {
            response.sendRedirect("jsp/login.jsp?error=Invalid credentials");
        }
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("jsp/login.jsp?error=Please log in");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Retrieve updated fields
        String userName = request.getParameter("userName");
        String userAddress = request.getParameter("userAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String userEmail = request.getParameter("userEmail");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        try {
            User user;
            switch (role) {
                case "student":
                    user = fileHandler.readStudents().stream()
                            .filter(s -> s.getUserId().equals(userId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
                    break;
                case "instructor":
                    user = fileHandler.readInstructors().stream()
                            .filter(i -> i.getUserId().equals(userId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
                    break;
                case "admin":
                    user = fileHandler.readAdmins().stream()
                            .filter(a -> a.getUserId().equals(userId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role");
            }

            // Update user fields using change* methods
            if (userName != null && !userName.trim().isEmpty()) {
                try {
                    user.changeUsername(userName);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
                    return;
                }
            }
            if (userAddress != null && !userAddress.trim().isEmpty()) {
                try {
                    user.changeAddress(userAddress);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
                    return;
                }
            }
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                try {
                    user.changePhoneNumber(phoneNumber);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
                    return;
                }
            }
            if (userEmail != null && !userEmail.trim().isEmpty()) {
                try {
                    user.changeEmail(userEmail);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
                    return;
                }
            }
            if (currentPassword != null && !currentPassword.trim().isEmpty() && newPassword != null && !newPassword.trim().isEmpty()) {
                try {
                    user.changePassword(currentPassword, newPassword);
                } catch (IllegalArgumentException e) {
                    response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
                    return;
                }
            }

            // Save updated user
            switch (role) {
                case "student":
                    fileHandler.addStudent((Student) user);
                    break;
                case "instructor":
                    fileHandler.addInstructor((Instructor) user);
                    break;
                case "admin":
                    fileHandler.addAdmin((Admin) user);
                    break;
            }
            response.sendRedirect("jsp/profile.jsp?success=true");
        } catch (Exception e) {
            LOGGER.severe("Profile update failed: " + e.getMessage());
            response.sendRedirect("jsp/profile.jsp?error=" + e.getMessage());
        }
    }

    private boolean isEmailUnique(String email) throws IOException {
        LOGGER.info("Checking email uniqueness for: " + email);
        List<Student> students = fileHandler.readStudents();
        List<Instructor> instructors = fileHandler.readInstructors();
        List<Admin> admins = fileHandler.readAdmins();

        // Log all emails for debugging
        if (students != null) {
            students.forEach(s -> LOGGER.info("Student email: " + s.getUserEmail()));
        }
        if (instructors != null) {
            instructors.forEach(i -> LOGGER.info("Instructor email: " + i.getUserEmail()));
        }
        if (admins != null) {
            admins.forEach(a -> LOGGER.info("Admin email: " + a.getUserEmail()));
        }

        // Check students
        if (students != null && students.stream().anyMatch(s -> s.getUserEmail() != null && s.getUserEmail().equalsIgnoreCase(email))) {
            LOGGER.warning("Email " + email + " found in students.txt");
            return false;
        }
        // Check instructors
        if (instructors != null && instructors.stream().anyMatch(i -> i.getUserEmail() != null && i.getUserEmail().equalsIgnoreCase(email))) {
            LOGGER.warning("Email " + email + " found in instructors.txt");
            return false;
        }
        // Check admins
        if (admins != null && admins.stream().anyMatch(a -> a.getUserEmail() != null && a.getUserEmail().equalsIgnoreCase(email))) {
            LOGGER.warning("Email " + email + " found in admins.txt");
            return false;
        }

        LOGGER.info("Email " + email + " is unique");
        return true;
    }

    private String generateUniqueUserId(String role) throws IOException {
        String prefix;
        switch (role) {
            case "student":
                prefix = "S";
                break;
            case "instructor":
                prefix = "I";
                break;
            case "admin":
                prefix = "A";
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }

        List<User> users;
        switch (role) {
            case "student":
                users = (List<User>)(List<?>) fileHandler.readStudents();
                break;
            case "instructor":
                users = (List<User>)(List<?>) fileHandler.readInstructors();
                break;
            case "admin":
                users = (List<User>)(List<?>) fileHandler.readAdmins();
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }

        int maxId = users.stream()
                .map(u -> Integer.parseInt(u.getUserId().substring(1)))
                .max(Integer::compare)
                .orElse(999);
        return prefix + (maxId + 1);
    }

    private User findUserByIdOrEmail(String userIdOrEmail) throws IOException {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(fileHandler.readStudents());
        allUsers.addAll(fileHandler.readInstructors());
        allUsers.addAll(fileHandler.readAdmins());

        return allUsers.stream()
                .filter(u -> u.getUserId().equals(userIdOrEmail) || u.getUserEmail().equalsIgnoreCase(userIdOrEmail))
                .findFirst()
                .orElse(null);
    }
}