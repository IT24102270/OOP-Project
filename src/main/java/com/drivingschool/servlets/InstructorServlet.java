package com.drivingschool.servlets;

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

@WebServlet("/instructorDashboard")
public class InstructorServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(InstructorServlet.class.getName());
    private FileHandler instructorFileHandler;
    private FileHandler assignmentFileHandler;

    @Override
    public void init() throws ServletException {
        String instructorFilePath = getServletContext().getRealPath("/WEB-INF/data/instructors.txt");
        instructorFileHandler = new FileHandler(instructorFilePath);
        String assignmentFilePath = getServletContext().getRealPath("/WEB-INF/data/assignments.txt");
        assignmentFileHandler = new FileHandler(assignmentFilePath);
        LOGGER.info("Initialized FileHandlers for instructors and assignments");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received GET request to /instructorDashboard");

        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Instructor)) {
            LOGGER.warning("No valid instructor session found. Redirecting to instructor login.");
            response.sendRedirect(request.getContextPath() + "/jsp/instructorLogin.jsp?errorMessage=Please log in as an instructor");
            return;
        }

        Instructor instructor = (Instructor) session.getAttribute("user");

        // Check assignment status
        List<Assignment> assignments = assignmentFileHandler.readAllAssignments();
        boolean isAssigned = assignments.stream().anyMatch(a -> a.getInstructorId().equals(instructor.getId()));

        // Set attributes for JSP
        request.setAttribute("instructor", instructor);
        request.setAttribute("isAssigned", isAssigned);

        // Forward to JSP
        request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /instructorDashboard");

        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Instructor)) {
            LOGGER.warning("No valid instructor session found. Redirecting to instructor login.");
            response.sendRedirect(request.getContextPath() + "/jsp/instructorLogin.jsp?errorMessage=Please log in as an instructor");
            return;
        }

        Instructor instructor = (Instructor) session.getAttribute("user");
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            String name = request.getParameter("name");
            String number = request.getParameter("number");
            String address = request.getParameter("address");
            String yearsOfExperienceStr = request.getParameter("yearsOfExperience");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            // Validation
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Name is required.");
                request.setAttribute("instructor", instructor);
                request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
                return;
            }

            if (!number.matches("\\d{10}")) {
                request.setAttribute("errorMessage", "Phone number must be 10 digits.");
                request.setAttribute("instructor", instructor);
                request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
                return;
            }

            int yearsOfExperience;
            try {
                yearsOfExperience = Integer.parseInt(yearsOfExperienceStr);
                if (yearsOfExperience < 0) {
                    request.setAttribute("errorMessage", "Years of experience cannot be negative.");
                    request.setAttribute("instructor", instructor);
                    request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                    request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Years of experience must be a valid number.");
                request.setAttribute("instructor", instructor);
                request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
                return;
            }

            if (password != null && !password.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    request.setAttribute("errorMessage", "Passwords do not match.");
                    request.setAttribute("instructor", instructor);
                    request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                    request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
                    return;
                }
            } else {
                password = instructor.getPassword(); // Keep existing password
            }

            try {
                // Create updated instructor
                Instructor updatedInstructor = new Instructor(
                        instructor.getId(),
                        name,
                        instructor.getDob(),
                        number,
                        address,
                        instructor.getEmail(),
                        password,
                        yearsOfExperience
                );

                // Update instructors.txt
                instructorFileHandler.updateInstructor(updatedInstructor);

                // Update session
                session.setAttribute("user", updatedInstructor);

                LOGGER.info("Instructor details updated for: " + name);
                response.sendRedirect(request.getContextPath() + "/instructorDashboard?successMessage=Details updated successfully");
            } catch (IOException e) {
                LOGGER.severe("Error updating instructor: " + e.getMessage());
                request.setAttribute("errorMessage", "Error updating details: " + e.getMessage());
                request.setAttribute("instructor", instructor);
                request.setAttribute("isAssigned", checkAssignmentStatus(instructor));
                request.getRequestDispatcher("/jsp/instructorDashboard.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/instructorDashboard.jsp?errorMessage=Invalid action");
        }
    }

    private boolean checkAssignmentStatus(Instructor instructor) {
        List<Assignment> assignments = assignmentFileHandler.readAllAssignments();
        return assignments.stream().anyMatch(a -> a.getInstructorId().equals(instructor.getId()));
    }
}