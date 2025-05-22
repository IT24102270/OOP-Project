package com.drivingschool.servlets;

import com.drivingschool.model.Admin;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.Student;
import com.drivingschool.model.User;
import com.drivingschool.util.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private FileHandler studentFileHandler;
    private FileHandler instructorFileHandler;
    private FileHandler adminFileHandler;

    @Override
    public void init() throws ServletException {
        String studentFilePath = getServletContext().getRealPath("/WEB-INF/data/students.txt");
        studentFileHandler = new FileHandler(studentFilePath);
        String instructorFilePath = getServletContext().getRealPath("/WEB-INF/data/instructors.txt");
        instructorFileHandler = new FileHandler(instructorFilePath);
        String adminFilePath = getServletContext().getRealPath("/WEB-INF/data/admins.txt");
        adminFileHandler = new FileHandler(adminFilePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            handleRegistration(request, response);
        } else if ("login".equals(action)) {
            handleLogin(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/studentLogin.jsp?errorMessage=Invalid action");
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String dob = request.getParameter("dob");
        String number = request.getParameter("number");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");
        String yearsOfExperience = request.getParameter("yearsOfExperience");

        if (!"student".equals(role) && !"instructor".equals(role) && !"admin".equals(role)) {
            request.setAttribute("errorMessage", "Invalid role specified.");
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
            return;
        }

        if (!number.matches("\\d{10}")) {
            request.setAttribute("errorMessage", "Phone number must be 10 digits.");
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
            return;
        }

        int experience = 0;
        if ("instructor".equals(role)) {
            if (yearsOfExperience == null || yearsOfExperience.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Years of experience is required for instructors.");
                request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
                return;
            }
            try {
                experience = Integer.parseInt(yearsOfExperience);
                if (experience < 0) {
                    request.setAttribute("errorMessage", "Years of experience cannot be negative.");
                    request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Years of experience must be a valid number.");
                request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
            }
        }

        if (studentFileHandler.emailExists(email) || instructorFileHandler.emailExists(email) || adminFileHandler.emailExists(email)) {
            request.setAttribute("errorMessage", "Email already registered.");
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
            return;
        }

        String id = UUID.randomUUID().toString();

        try {
            if ("student".equals(role)) {
                Student student = new Student(id, name, dob, number, address, email, password);
                studentFileHandler.saveStudent(student);
            } else if ("instructor".equals(role)) {
                Instructor instructor = new Instructor(id, name, dob, number, address, email, password, experience);
                instructorFileHandler.saveInstructor(instructor);
            } else if ("admin".equals(role)) {
                Admin admin = new Admin(id, name, dob, number, address, email, password);
                adminFileHandler.saveAdmin(admin);
            }

            response.sendRedirect(request.getContextPath() + "/jsp/" + getLoginJsp(role) + "?registered=true");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid data: " + e.getMessage());
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error saving data: " + e.getMessage());
            request.getRequestDispatcher("/jsp/" + getRegisterJsp(role)).forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (!"student".equals(role) && !"instructor".equals(role) && !"admin".equals(role)) {
            request.setAttribute("errorMessage", "Invalid role specified.");
            request.getRequestDispatcher("/jsp/" + getLoginJsp(role)).forward(request, response);
            return;
        }

        User user = null;
        if ("student".equals(role)) {
            user = studentFileHandler.authenticate(email, password);
        } else if ("instructor".equals(role)) {
            user = instructorFileHandler.authenticate(email, password);
        } else if ("admin".equals(role)) {
            user = adminFileHandler.authenticate(email, password);
        }

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String redirectUrl;
            switch (role) {
                case "student":
                    redirectUrl = request.getContextPath() + "/studentDashboard";
                    break;
                case "instructor":
                    redirectUrl = request.getContextPath() + "/instructorDashboard";
                    break;
                case "admin":
                    redirectUrl = request.getContextPath() + "/adminDashboard";
                    break;
                default:
                    redirectUrl = request.getContextPath() + "/jsp/studentLogin.jsp";
            }
            response.sendRedirect(redirectUrl);
        } else {
            request.setAttribute("errorMessage", "Invalid email or password.");
            request.getRequestDispatcher("/jsp/" + getLoginJsp(role)).forward(request, response);
        }
    }

    private String getRegisterJsp(String role) {
        switch (role) {
            case "student":
                return "studentRegister.jsp";
            case "instructor":
                return "instructorRegister.jsp";
            case "admin":
                return "adminRegister.jsp";
            default:
                return "studentRegister.jsp";
        }
    }

    private String getLoginJsp(String role) {
        switch (role) {
            case "student":
                return "studentLogin.jsp";
            case "instructor":
                return "instructorLogin.jsp";
            case "admin":
                return "adminLogin.jsp";
            default:
                return "studentLogin.jsp";
        }
    }
}