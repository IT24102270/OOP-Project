package com.drivingschool.servlets;

import com.drivingschool.model.Lesson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/lesson")
public class LessonServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("view".equals(action)) {
            List<Lesson> lessons = Lesson.getAllLessons();
            req.setAttribute("lessons", lessons);
            req.getRequestDispatcher("/jsp/viewLessons.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            String id = req.getParameter("id");
            Lesson lesson = Lesson.getLessonById(id);
            req.setAttribute("lesson", lesson);
            req.getRequestDispatcher("/jsp/manageLesson.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/jsp/bookLesson.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("book".equals(action)) {
            Lesson lesson = new Lesson(
                    UUID.randomUUID().toString(),
                    req.getParameter("studentId"),
                    req.getParameter("instructorId"),
                    req.getParameter("vehicleType"),
                    req.getParameter("lessonType"),
                    req.getParameter("date"),
                    req.getParameter("time"),
                    "Scheduled"
            );
            lesson.bookLesson();
            resp.sendRedirect("lesson?action=view");
        } else if ("update".equals(action)) {
            Lesson lesson = new Lesson(
                    req.getParameter("id"),
                    req.getParameter("studentId"),
                    req.getParameter("instructorId"),
                    req.getParameter("vehicleType"),
                    req.getParameter("lessonType"),
                    req.getParameter("date"),
                    req.getParameter("time"),
                    req.getParameter("status")
            );
            Lesson.updateLesson(lesson);
            resp.sendRedirect("lesson?action=view");
        } else if ("delete".equals(action)) {
            String id = req.getParameter("id");
            Lesson.cancelLesson(id);
            resp.sendRedirect("lesson?action=view");
        }
    }
}
