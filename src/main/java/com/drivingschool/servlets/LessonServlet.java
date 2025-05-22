package com.drivingschool.servlets;

import com.drivingschool.model.LessonBooking;
import com.drivingschool.model.Student;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet("/lessonBooking")
public class LessonServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LessonServlet.class.getName());
    private FileHandler bookingFileHandler;

    @Override
    public void init() throws ServletException {
        String bookingFilePath = getServletContext().getRealPath("/WEB-INF/data/bookings.txt");
        LOGGER.info("Initializing FileHandler with path: " + bookingFilePath);
        bookingFileHandler = new FileHandler(bookingFilePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /lessonBooking");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Student)) {
            LOGGER.warning("No valid student session found. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Please login as a student to book lessons");
            return;
        }

        Student student = (Student) session.getAttribute("user");
        String studentId = student.getId();
        LOGGER.info("Processing booking for student ID: " + studentId);

        String vehicleType = request.getParameter("vehicle-type");
        String[] selectedLessons = request.getParameterValues("lesson");
        String examPrep = request.getParameter("exam-prep");
        LOGGER.info("Received parameters: vehicleType=" + vehicleType + ", lessons=" + (selectedLessons != null ? Arrays.toString(selectedLessons) : "null") + ", examPrep=" + examPrep);

        if (vehicleType == null || (!vehicleType.equals("light") && !vehicleType.equals("heavy"))) {
            LOGGER.warning("Invalid vehicle type: " + vehicleType);
            session.setAttribute("bookingVehicleType", vehicleType);
            session.setAttribute("bookingLessons", selectedLessons);
            session.setAttribute("bookingExamPrep", examPrep);
            request.setAttribute("errorMessage", "Please select a valid vehicle type (light or heavy).");
            request.getRequestDispatcher("/jsp/lessonBooking.jsp").forward(request, response);
            return;
        }

        if (selectedLessons == null || selectedLessons.length == 0) {
            LOGGER.warning("No lessons selected.");
            session.setAttribute("bookingVehicleType", vehicleType);
            session.setAttribute("bookingLessons", selectedLessons);
            session.setAttribute("bookingExamPrep", examPrep);
            request.setAttribute("errorMessage", "Please select at least one lesson.");
            request.getRequestDispatcher("/jsp/lessonBooking.jsp").forward(request, response);
            return;
        }

        List<String> validLessons = vehicleType.equals("light")
                ? Arrays.asList("motorcycle", "car", "three-wheeler")
                : Arrays.asList("truck", "bus");
        for (String lesson : selectedLessons) {
            if (!validLessons.contains(lesson)) {
                LOGGER.warning("Invalid lesson for vehicle type: " + lesson);
                session.setAttribute("bookingVehicleType", vehicleType);
                session.setAttribute("bookingLessons", selectedLessons);
                session.setAttribute("bookingExamPrep", examPrep);
                request.setAttribute("errorMessage", "Selected lessons are not valid for the chosen vehicle type.");
                request.getRequestDispatcher("/jsp/lessonBooking.jsp").forward(request, response);
                return;
            }
        }

        double lessonTotalAmount = 0;
        final double MOTORCYCLE_COST = 50;
        final double CAR_COST = 60;
        final double THREE_WHEELER_COST = 55;
        final double TRUCK_COST = 100;
        final double BUS_COST = 90;
        final double EXAM_PREP_COST = 30;

        for (String lesson : selectedLessons) {
            switch (lesson) {
                case "motorcycle":
                    lessonTotalAmount += MOTORCYCLE_COST;
                    break;
                case "car":
                    lessonTotalAmount += CAR_COST;
                    break;
                case "three-wheeler":
                    lessonTotalAmount += THREE_WHEELER_COST;
                    break;
                case "truck":
                    lessonTotalAmount += TRUCK_COST;
                    break;
                case "bus":
                    lessonTotalAmount += BUS_COST;
                    break;
            }
        }

        boolean writtenExamPrep = "written-exam".equals(examPrep);
        if (writtenExamPrep) {
            lessonTotalAmount += EXAM_PREP_COST;
        }
        LOGGER.info("Calculated total amount: $" + lessonTotalAmount);

        String bookingId = UUID.randomUUID().toString();
        LessonBooking booking = new LessonBooking(bookingId, studentId, vehicleType,
                Arrays.asList(selectedLessons), writtenExamPrep, lessonTotalAmount);

        try {
            LOGGER.info("Attempting to save booking: " + booking.toTextLine());
            bookingFileHandler.saveLessonBooking(booking);
            LOGGER.info("Booking saved successfully.");

            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", lessonTotalAmount);
            session.setAttribute("bookingVehicleType", vehicleType);
            session.setAttribute("bookingLessons", selectedLessons);
            session.setAttribute("bookingExamPrep", examPrep);

            response.sendRedirect(request.getContextPath() + "/jsp/makePayment.jsp");
        } catch (IOException e) {
            LOGGER.severe("Error saving booking: " + e.getMessage());
            session.setAttribute("bookingVehicleType", vehicleType);
            session.setAttribute("bookingLessons", selectedLessons);
            session.setAttribute("bookingExamPrep", examPrep);
            session.setAttribute("bookingLessonTotalAmount", lessonTotalAmount);
            request.setAttribute("errorMessage", "Error saving booking. Please try again.");
            request.getRequestDispatcher("/jsp/lessonBooking.jsp").forward(request, response);
        }
    }
}