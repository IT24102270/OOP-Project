package com.drivingschool.servlets;

import com.drivingschool.model.Admin;
import com.drivingschool.model.Student;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.Assignment;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/adminDashboard")
public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminServlet.class.getName());
    private FileHandler studentFileHandler;
    private FileHandler instructorFileHandler;
    private FileHandler assignmentFileHandler;

    @Override
    public void init() throws ServletException {
        studentFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/students.txt"));
        instructorFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/instructors.txt"));
        assignmentFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/assignments.txt"));
        LOGGER.info("Initialized FileHandlers for students, instructors, and assignments");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received GET request to /adminDashboard");

        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Admin)) {
            LOGGER.warning("No valid admin session found. Redirecting to admin login.");
            response.sendRedirect(request.getContextPath() + "/jsp/adminLogin.jsp?errorMessage=Please log in as an admin");
            return;
        }

        // Load students and instructors
        List<Student> students = studentFileHandler.readAllStudents();
        List<Instructor> instructors = instructorFileHandler.readAllInstructors();

        // Set attributes
        request.setAttribute("admin", session.getAttribute("user"));
        request.setAttribute("students", students);
        request.setAttribute("instructors", instructors);

        // Forward to JSP
        request.getRequestDispatcher("/jsp/adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /adminDashboard");

        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Admin)) {
            LOGGER.warning("No valid admin session found. Redirecting to admin login.");
            response.sendRedirect(request.getContextPath() + "/jsp/adminLogin.jsp?errorMessage=Please log in as an admin");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String role = request.getParameter("role");
            String id = request.getParameter("id");

            if (id == null || id.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Invalid user ID.");
                reloadAndForward(request, response);
                return;
            }

            // Check assignments
            List<Assignment> assignments = assignmentFileHandler.readAllAssignments();
            try {
                if ("student".equals(role)) {
                    boolean hasAssignments = assignments.stream().anyMatch(a -> a.getStudentId().equals(id));
                    if (hasAssignments) {
                        request.setAttribute("errorMessage", "Cannot delete student with active assignments.");
                        reloadAndForward(request, response);
                        return;
                    }
                    studentFileHandler.deleteStudent(id);
                    LOGGER.info("Deleted student with ID: " + id);
                    response.sendRedirect(request.getContextPath() + "/adminDashboard?successMessage=Student deleted successfully");
                } else if ("instructor".equals(role)) {
                    boolean hasAssignments = assignments.stream().anyMatch(a -> a.getInstructorId().equals(id));
                    if (hasAssignments) {
                        request.setAttribute("errorMessage", "Cannot delete instructor with active assignments.");
                        reloadAndForward(request, response);
                        return;
                    }
                    instructorFileHandler.deleteInstructor(id);
                    LOGGER.info("Deleted instructor with ID: " + id);
                    response.sendRedirect(request.getContextPath() + "/adminDashboard?successMessage=Instructor deleted successfully");
                } else {
                    request.setAttribute("errorMessage", "Invalid role specified.");
                    reloadAndForward(request, response);
                }
            } catch (IOException e) {
                LOGGER.severe("Error deleting user: " + e.getMessage());
                request.setAttribute("errorMessage", "Error deleting user: " + e.getMessage());
                reloadAndForward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Invalid action.");
            reloadAndForward(request, response);
        }
    }

    private void reloadAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("students", studentFileHandler.readAllStudents());
        request.setAttribute("instructors", instructorFileHandler.readAllInstructors());
        request.setAttribute("admin", request.getSession(false).getAttribute("user"));
        request.getRequestDispatcher("/jsp/adminDashboard.jsp").forward(request, response);
    }
}