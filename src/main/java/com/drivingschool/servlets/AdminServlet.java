package main.java.com.drivingschool.servlets;

import main.java.com.drivingschool.model.Admin;
import main.java.com.drivingschool.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "addAdmin":
                case "updateAdmin":
                    Admin admin = new Admin(
                            req.getParameter("name"),
                            req.getParameter("userID"),
                            req.getParameter("address"),
                            req.getParameter("phone"),
                            req.getParameter("email"),
                            req.getParameter("password"),
                            "admin",
                            req.getParameter("adminID")
                    );
                    if (action.equals("addAdmin")) {
                        Admin.addAdmin(admin);
                    } else {
                        Admin.updateAdmin(admin);
                    }
                    break;

                case "deleteAdmin":
                    Admin.deleteAdmin(req.getParameter("adminID"));
                    break;

                case "addUser":
                case "updateUser":
                    User user = new User(
                            req.getParameter("name"),
                            req.getParameter("userID"),
                            req.getParameter("address"),
                            req.getParameter("phone"),
                            req.getParameter("email"),
                            req.getParameter("password"),
                            "user"
                    );
                    if (action.equals("addUser")) {
                        Admin.addUser(user);
                    } else {
                        Admin.updateUser(user);
                    }
                    break;

                case "deleteUser":
                    Admin.deleteUser(req.getParameter("userID"));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.sendRedirect("adminDashboard.jsp");
    }
}
