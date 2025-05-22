package com.drivingschool.servlets;

import com.drivingschool.model.Payment;
import com.drivingschool.model.Student;
import com.drivingschool.model.CircularQueue;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Student)) {
            LOGGER.warning("No valid student session found. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Please login to make a payment");
            return;
        }

        Student student = (Student) session.getAttribute("user");
        String studentId = student.getId();
        String bookingId = request.getParameter("bookingId");
        String amountStr = request.getParameter("amount");
        String cardNumber = request.getParameter("cardNumber");
        String expiry = request.getParameter("expiry");
        String cvv = request.getParameter("cvv");

        LOGGER.info("Received parameters: bookingId=" + bookingId + ", studentId=" + studentId + ", amount=" + amountStr);

        if (bookingId == null || amountStr == null || cardNumber == null || expiry == null || cvv == null) {
            LOGGER.warning("Missing required parameters.");
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amountStr != null ? Double.parseDouble(amountStr) : null);
            request.setAttribute("errorMessage", "All payment fields are required.");
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

        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        if (!cardNumber.matches("\\d{16}")) {
            LOGGER.warning("Invalid card number format: " + cardNumber);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Card number must be 16 digits.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            LOGGER.warning("Invalid expiry format: " + expiry);
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Expiry must be in MM/YY format.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
            return;
        }

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

        String cardLastFour = cardNumber.substring(cardNumber.length() - 4);
        String paymentId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        Payment payment = new Payment(paymentId, bookingId, studentId, amount, cardLastFour, timestamp);

        try {
            LOGGER.info("Attempting to save payment: " + payment.toTextLine());
            paymentFileHandler.savePayment(payment);
            LOGGER.info("Payment saved successfully.");

            ServletContext context = getServletContext();
            CircularQueue bookingQueue = (CircularQueue) context.getAttribute("bookingQueue");
            if (bookingQueue == null) {
                bookingQueue = new CircularQueue();
                context.setAttribute("bookingQueue", bookingQueue);
            }
            if (!bookingQueue.enqueue(bookingId)) {
                LOGGER.warning("Queue is full, cannot enqueue booking ID: " + bookingId);
                session.setAttribute("bookingId", bookingId);
                session.setAttribute("bookingLessonTotalAmount", amount);
                request.setAttribute("errorMessage", "Booking queue is full. Please try again later.");
                request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
                return;
            }
            LOGGER.info("Enqueued booking ID: " + bookingId);

            session.removeAttribute("bookingId");
            session.removeAttribute("bookingLessonTotalAmount");
            session.removeAttribute("bookingVehicleType");
            session.removeAttribute("bookingLessons");
            session.removeAttribute("bookingExamPrep");

            String successMessage = URLEncoder.encode("Payment completed successfully! Your booking is now pending confirmation.", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/studentDashboard?successMessage=" + successMessage);
        } catch (IOException e) {
            LOGGER.severe("Error saving payment: " + e.getMessage());
            session.setAttribute("bookingId", bookingId);
            session.setAttribute("bookingLessonTotalAmount", amount);
            request.setAttribute("errorMessage", "Error processing payment. Please try again.");
            request.getRequestDispatcher("/jsp/makePayment.jsp").forward(request, response);
        }
    }
}