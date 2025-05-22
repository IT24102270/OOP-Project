package com.drivingschool.servlets;

import com.drivingschool.model.CircularQueue;
import com.drivingschool.model.Student;
import com.drivingschool.model.LessonBooking;
import com.drivingschool.model.Assignment;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.Payment;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/studentDashboard")
public class StudentDashboardServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StudentDashboardServlet.class.getName());
    private FileHandler bookingFileHandler;
    private FileHandler queueFileHandler;
    private FileHandler assignmentFileHandler;
    private FileHandler instructorFileHandler;
    private FileHandler paymentFileHandler;

    @Override
    public void init() throws ServletException {
        bookingFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/bookings.txt"));
        queueFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/queue.txt"));
        assignmentFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/assignments.txt"));
        instructorFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/instructors.txt"));
        paymentFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/payments.txt"));
        LOGGER.info("Initialized FileHandlers for bookings, queue, assignments, instructors, and payments");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received GET request to /studentDashboard");

        // Check if user is a student
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"Student".equals(session.getAttribute("user").getClass().getSimpleName())) {
            LOGGER.warning("No valid student session found. Redirecting to student login.");
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Please login as a student to access the dashboard");
            return;
        }

        Student student = (Student) session.getAttribute("user");

        // Get paid booking IDs
        List<Payment> payments = paymentFileHandler.readAllPayments();
        Set<String> paidBookingIds = payments.stream()
                .filter(payment -> payment.getStudentId().equals(student.getId()))
                .map(Payment::getBookingId)
                .collect(Collectors.toSet());

        // Get student's paid bookings
        List<LessonBooking> allBookings = bookingFileHandler.readAllLessonBookings();
        List<LessonBooking> studentBookings = allBookings.stream()
                .filter(booking -> booking.getStudentId().equals(student.getId()) && paidBookingIds.contains(booking.getBookingId()))
                .collect(Collectors.toList());

        // Get confirmed booking IDs from queue.txt
        List<String> confirmedBookingIds = queueFileHandler.readConfirmedBookings();

        // Get pending booking IDs from CircularQueue
        CircularQueue bookingQueue = (CircularQueue) getServletContext().getAttribute("bookingQueue");
        List<String> pendingBookingIds = (bookingQueue != null) ? bookingQueue.getAllBookingIds() : Collections.emptyList();

        // Get assignments
        List<Assignment> assignments = assignmentFileHandler.readAllAssignments();

        // Get instructors
        List<Instructor> instructors = instructorFileHandler.readAllInstructors();

        // Set attributes for JSP
        request.setAttribute("student", student);
        request.setAttribute("studentBookings", studentBookings);
        request.setAttribute("confirmedBookingIds", confirmedBookingIds);
        request.setAttribute("pendingBookingIds", pendingBookingIds);
        request.setAttribute("assignments", assignments);
        request.setAttribute("instructors", instructors);

        // Forward to JSP
        request.getRequestDispatcher("/jsp/studentDashboard.jsp").forward(request, response);
    }
}