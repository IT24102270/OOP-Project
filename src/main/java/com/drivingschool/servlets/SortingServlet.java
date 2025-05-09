package com.drivingschool.servlets;

import com.drivingschool.model.Instructor;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet to handle instructor-related operations, including sorting
@WebServlet("/SortingServlet")
public class SortingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Handle GET requests to display sorted instructors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            // Retrieve and sort all instructors
            List<Instructor> instructors = Instructor.getAllInstructorsSorted();

            // Set the sorted list as a session attribute for the JSP page
            req.getSession().setAttribute("instructors", instructors);

            // Redirect to the instructor list JSP page (handled by group leader)
            res.sendRedirect("instructorList.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp"); // Redirect to error page on failure
        }
    }

    // Handle POST requests for Add, Update, and Remove operations
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "add":
                    // Add a new instructor
                    String instructorID = req.getParameter("instructorID");
                    String name = req.getParameter("name");
                    String email = req.getParameter("email");
                    int experienceYear = Integer.parseInt(req.getParameter("experienceYear"));
                    String vehicleClassesStr = req.getParameter("vehicleClasses");
                    String[] vehicleClasses = vehicleClassesStr.split(",");

                    Instructor instructor = new Instructor(name, instructorID, "", "", email, "", "instructor",
                            instructorID, vehicleClasses, experienceYear);
                    Instructor.addInstructor(instructor);
                    break;

                case "update":
                    // Update instructor experience
                    String updateInstructorID = req.getParameter("instructorID");
                    int newExperience = Integer.parseInt(req.getParameter("newExperience"));
                    Instructor.updateInstructorExperience(updateInstructorID, newExperience);
                    break;

                case "remove":
                    // Remove an instructor
                    String removeInstructorID = req.getParameter("instructorID");
                    Instructor.deleteInstructor(removeInstructorID);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("errorMessage", "Operation failed: " + e.getMessage());
            res.sendRedirect("error.jsp");
            return;
        }

        // Redirect back to the instructor list after any POST action
        res.sendRedirect("instructorList.jsp");
    }
}