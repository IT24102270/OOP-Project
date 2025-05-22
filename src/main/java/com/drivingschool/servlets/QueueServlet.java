
package com.drivingschool.servlets;

import com.drivingschool.model.CircularQueue;
import com.drivingschool.model.LessonBooking;
import com.drivingschool.util.FileHandler;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/queue")
public class QueueServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(QueueServlet.class.getName());
    private FileHandler queueFileHandler;

    @Override
    public void init() throws ServletException {
        String queueFilePath = getServletContext().getRealPath("/WEB-INF/data/queue.txt");
        LOGGER.info("Initializing FileHandler for queue with path: " + queueFilePath);
        queueFileHandler = new FileHandler(queueFilePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received POST request to /queue");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"admin".equals(session.getAttribute("user").getClass().getSimpleName().toLowerCase())) {
            LOGGER.warning("No valid admin session found. Redirecting to admin login.");
            response.sendRedirect(request.getContextPath() + "/jsp/adminLogin.jsp?errorMessage=Please login as admin to manage queue");
            return;
        }

        String action = request.getParameter("action");
        ServletContext context = getServletContext();
        CircularQueue bookingQueue = (CircularQueue) context.getAttribute("bookingQueue");
        if (bookingQueue == null) {
            bookingQueue = new CircularQueue();
            context.setAttribute("bookingQueue", bookingQueue);
        }

        if ("confirm".equals(action)) {
            String bookingId = bookingQueue.dequeue();
            if (bookingId != null) {
                try {
                    // Save the confirmed booking ID to queue.txt
                    queueFileHandler.saveConfirmedBooking(bookingId);
                    LOGGER.info("Confirmed and dequeued booking ID: " + bookingId);
                    response.sendRedirect(request.getContextPath() + "/jsp/adminQueue.jsp?successMessage=Booking confirmed and removed from queue");
                } catch (IOException e) {
                    LOGGER.severe("Error saving confirmed booking: " + e.getMessage());
                    request.setAttribute("errorMessage", "Error saving confirmed booking: " + e.getMessage());
                    request.getRequestDispatcher("/jsp/adminQueue.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "No bookings in queue to confirm.");
                request.getRequestDispatcher("/jsp/adminQueue.jsp").forward(request, response);
            }
        } else {
            LOGGER.warning("Invalid action: " + action);
            request.setAttribute("errorMessage", "Invalid action.");
            request.getRequestDispatcher("/jsp/adminQueue.jsp").forward(request, response);
        }
    }
}