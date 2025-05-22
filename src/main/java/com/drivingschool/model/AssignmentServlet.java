package com.drivingschool.servlets;

import com.drivingschool.model.Assignment;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.LessonBooking;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/assignInstructors")
public class AssignmentServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AssignmentServlet.class.getName());
    private FileHandler queueFileHandler;
    private FileHandler bookingFileHandler;
    private FileHandler instructorFileHandler;
    private FileHandler assignmentFileHandler;

    @Override
    public void init() throws ServletException {
        queueFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/queue.txt"));
        bookingFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/bookings.txt"));
        instructorFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/instructors.txt"));
        assignmentFileHandler = new FileHandler(getServletContext().getRealPath("/WEB-INF/data/assignments.txt"));
        LOGGER.info("Initialized FileHandlers for queue, bookings, instructors, and assignments");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /assignInstructors");

        // Check if user is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"Admin".equals(session.getAttribute("user").getClass().getSimpleName())) {
            LOGGER.warning("No valid admin session found. Redirecting to admin login.");
            response.sendRedirect(request.getContextPath() + "/jsp/adminLogin.jsp?errorMessage=Please login as admin to assign instructors");
            return;
        }

        // Read confirmed bookings from queue.txt
        List<String> confirmedBookingIds = queueFileHandler.readConfirmedBookings();
        if (confirmedBookingIds.isEmpty()) {
            LOGGER.info("No confirmed bookings to assign.");
            request.setAttribute("errorMessage", "No confirmed bookings available to assign.");
            request.getRequestDispatcher("/jsp/adminAssignInstructors.jsp").forward(request, response);
            return;
        }

        // Read all bookings to get student IDs
        List<LessonBooking> bookings = bookingFileHandler.readAllLessonBookings();
        List<LessonBooking> confirmedBookings = new ArrayList<>();
        for (String bookingId : confirmedBookingIds) {
            for (LessonBooking booking : bookings) {
                if (booking.getBookingId().equals(bookingId)) {
                    confirmedBookings.add(booking);
                    break;
                }
            }
        }

        if (confirmedBookings.isEmpty()) {
            LOGGER.warning("No matching bookings found for confirmed booking IDs.");
            request.setAttribute("errorMessage", "No valid bookings found for assignment.");
            request.getRequestDispatcher("/jsp/adminAssignInstructors.jsp").forward(request, response);
            return;
        }

        // Read all assignments to identify assigned instructors
        List<Assignment> existingAssignments = assignmentFileHandler.readAllAssignments();
        List<String> assignedInstructorIds = existingAssignments.stream()
                .map(Assignment::getInstructorId)
                .distinct()
                .collect(Collectors.toList());

        // Read instructors and filter out those already assigned
        List<Instructor> allInstructors = instructorFileHandler.readAllInstructors();
        List<Instructor> availableInstructors = allInstructors.stream()
                .filter(instructor -> !assignedInstructorIds.contains(instructor.getId()))
                .collect(Collectors.toList());

        if (availableInstructors.isEmpty()) {
            LOGGER.warning("No available instructors for assignment.");
            request.setAttribute("errorMessage", "No available instructors for assignment.");
            request.getRequestDispatcher("/jsp/adminAssignInstructors.jsp").forward(request, response);
            return;
        }

        // Sort available instructors by experience
        bubbleSortInstructors(availableInstructors);

        // Assign instructors (round-robin among available instructors)
        List<Assignment> assignments = new ArrayList<>();
        int instructorIndex = 0;
        for (LessonBooking booking : confirmedBookings) {
            Instructor instructor = availableInstructors.get(instructorIndex % availableInstructors.size());
            String assignmentId = UUID.randomUUID().toString();
            long timestamp = System.currentTimeMillis();
            Assignment assignment = new Assignment(assignmentId, booking.getBookingId(), booking.getStudentId(), instructor.getId(), timestamp);
            assignments.add(assignment);
            instructorIndex++;
        }

        // Save assignments
        try {
            for (Assignment assignment : assignments) {
                assignmentFileHandler.saveAssignment(assignment);
                LOGGER.info("Saved assignment: " + assignment.toTextLine());
            }

            // Clear queue.txt by overwriting with empty content
            synchronized (queueFileHandler.getFilePath()) {
                try (FileWriter writer = new FileWriter(queueFileHandler.getFilePath(), false)) {
                    writer.write(""); // Ensure file is empty
                }
            }

            LOGGER.info("Assignments completed successfully.");
            request.setAttribute("successMessage", "Instructors assigned successfully.");
            request.getRequestDispatcher("/jsp/adminAssignInstructors.jsp").forward(request, response);
        } catch (IOException e) {
            LOGGER.severe("Error saving assignments: " + e.getMessage());
            request.setAttribute("errorMessage", "Error saving assignments: " + e.getMessage());
            request.getRequestDispatcher("/jsp/adminAssignInstructors.jsp").forward(request, response);
        }
    }

    // Bubble sort instructors by years of experience (descending)
    private void bubbleSortInstructors(List<Instructor> instructors) {
        int n = instructors.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (instructors.get(j).getYearsOfExperience() < instructors.get(j + 1).getYearsOfExperience()) {
                    // Swap instructors
                    Instructor temp = instructors.get(j);
                    instructors.set(j, instructors.get(j + 1));
                    instructors.set(j + 1, temp);
                }
            }
        }
    }
}