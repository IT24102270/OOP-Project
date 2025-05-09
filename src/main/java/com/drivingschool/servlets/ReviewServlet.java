package main.java.com.drivingschool.servlets;

import main.java.com.drivingschool.model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "addReview":
                case "updateReview":
                    Review review = new Review(
                            req.getParameter("reviewID"),
                            req.getParameter("userID"),
                            req.getParameter("comment"),
                            Integer.parseInt(req.getParameter("rating"))
                    );
                    if (action.equals("addReview")) {
                        Review.addReview(review);
                    } else {
                        Review.updateReview(review);
                    }
                    break;

                case "deleteReview":
                    Review.deleteReview(req.getParameter("reviewID"));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.sendRedirect("reviewDashboard.jsp");
    }
}
