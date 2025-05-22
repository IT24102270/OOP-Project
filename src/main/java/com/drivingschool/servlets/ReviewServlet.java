package com.drivingschool.servlets;

import com.drivingschool.model.Review;
import com.drivingschool.model.Student;
import com.drivingschool.model.Payment;
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

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ReviewServlet.class.getName());
    private FileHandler reviewFileHandler;
    private FileHandler paymentFileHandler;

    @Override
    public void init() throws ServletException {
        String reviewFilePath = getServletContext().getRealPath("/WEB-INF/data/reviews.txt");
        reviewFileHandler = new FileHandler(reviewFilePath);
        String paymentFilePath = getServletContext().getRealPath("/WEB-INF/data/payments.txt");
        paymentFileHandler = new FileHandler(paymentFilePath);
        LOGGER.info("Initialized FileHandlers for reviews and payments");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /review");

        // Validate session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Student)) {
            LOGGER.warning("No valid student session found. Redirecting to student login.");
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Please log in as a student");
            return;
        }

        Student student = (Student) session.getAttribute("user");
        LOGGER.info("Processing review for student: " + student.getName() + " (ID: " + student.getId() + ")");

        // Optional: Check for paid bookings (comment out to disable)
        List<Payment> payments = paymentFileHandler.readAllPayments();
        boolean hasBooking = payments.stream().anyMatch(p -> p.getStudentId().equals(student.getId()));
        if (!hasBooking) {
            LOGGER.warning("Student has no paid bookings. Forwarding to studentReview.jsp with error.");
            request.setAttribute("errorMessage", "You must have a paid booking to submit a review");
            request.getRequestDispatcher("/jsp/studentReview.jsp").forward(request, response);
            return;
        }

        // Get form parameters
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // Server-side validation
        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid rating: " + ratingStr);
            request.setAttribute("errorMessage", "Invalid rating: " + e.getMessage());
            request.getRequestDispatcher("/jsp/studentReview.jsp").forward(request, response);
            return;
        }

        if (comment == null || comment.trim().isEmpty()) {
            LOGGER.warning("Comment is empty");
            request.setAttribute("errorMessage", "Comment is required");
            request.getRequestDispatcher("/jsp/studentReview.jsp").forward(request, response);
            return;
        }

        try {
            Review review = new Review(student.getId(), student.getName(), rating, comment);
            reviewFileHandler.saveReview(review, student.getId());
            LOGGER.info("Review saved successfully for student: " + student.getName());
            response.sendRedirect(request.getContextPath() + "/studentDashboard?successMessage=Review submitted successfully");
        } catch (IOException e) {
            LOGGER.severe("Error saving review: " + e.getMessage());
            request.setAttribute("errorMessage", "Error saving review: " + e.getMessage());
            request.getRequestDispatcher("/jsp/studentReview.jsp").forward(request, response);
        }
    }
}