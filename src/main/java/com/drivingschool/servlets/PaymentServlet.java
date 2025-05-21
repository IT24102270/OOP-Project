

package com.drivingschool.servlets;

import com.drivingschool.model.Payment;
import com.drivingschool.model.Student;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PaymentServlet.class.getName());
    private FileHandler paymentFileHandler;

    @Override
    public void init() throws ServletException {
        String paymentFilePath = getServletContext().getRealPath("/WEB-INF/data/payments.txt");
        LOGGER.info("Initializing FileHandler with path: " + paymentFilePath);
        paymentFileHandler = new FileHandler(paymentFilePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /payment");

        // Check if user is logged in and is a student
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Student)) {
            LOGGER.warning("No valid student session found. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Please login to make a payment");
            return;
        }

        // Get student and booking details
        Student student = (Student) session.getAttribute("user");
        String studentId = student.getId();
        String bookingId = request.getParameter("bookingId");
        String amountStr = request.getParameter("amount");
        String cardNumber = request.getParameter("cardNumber");
        String expiry = request.getParameter("expiry");
        String cvv = request.getParameter("cvv");

        LOGGER.info("Received parameters: bookingId=" + bookingId + ", studentId=" + studentId + ", amount=" + amountStr);

        // Validate inputs
        if (bookingId == null || amountStr == null || cardNumber == null || expiry == null || cvv == null) {
            LOGGER.warning("Missing required parameters.");
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amountStr != null ? Double.parseDouble(amountStr) : null);
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid amount format: " + amountStr);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", null);
            request.setAttribute("errorMessage", "Invalid amount format.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        // Clean card number (remove spaces and dashes)
        cardNumber = cardNumber.replaceAll("[\\s-]", "");

        // Basic card validation
        if (!cardNumber.matches("\\d{16}")) {
            LOGGER.warning("Invalid card number format: " + cardNumber);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Card number must be 16 digits.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        // Validate expiry (MM/YY) and ensure it's in the future
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            LOGGER.warning("Invalid expiry format: " + expiry);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Expiry must be in MM/YY format.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        // Check if expiry is in the future
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
            sdf.setLenient(false);
            Date expiryDate = sdf.parse(expiry);
            Date now = new Date();
            if (expiryDate.before(now)) {
                LOGGER.warning("Card expired: " + expiry);
                session.setAttribute("bookingId", bookingId);
                session.setAttribute("bookingLessonTotalAmount", amount);
                request.setAttribute("errorMessage", "Card has expired.");
                request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
                return;
            }
        } catch (ParseException e) {
            LOGGER.warning("Invalid expiry date: " + expiry);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Invalid expiry date.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        if (!cvv.matches("\\d{3}")) {
            LOGGER.warning("Invalid CVV format: " + cvv);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "CVV must be 3 digits.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        // Store only last four digits of card
        String cardLastFour = cardNumber.substring(cardNumber.length() - 4);

        // Create payment object
        String paymentId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        Payment payment = new Payment(paymentId, bookingId, studentId, amount, cardLastFour, timestamp);

        // Save payment to payments.txt
        try {
            LOGGER.info("Attempting to save payment: " + payment.toTextLine());
            paymentFileHandler.savePayment(payment);
            LOGGER.info("Payment saved successfully.");

            // Clear session attributes
            session.removeAttribute("bookingId");
            session.removeAttribute("bookingLessonTotalAmount");

            // Redirect to student dashboard
            response.sendRedirect(request.getContextPath() + "/jsp/studentDashboard.jsp?successMessage=Payment successfully completed!");
        } catch (IOException e) {
            LOGGER.severe("Error saving payment: " + e.getMessage());
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Error saving payment: " + e.getMessage());
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
        }
    }
}
