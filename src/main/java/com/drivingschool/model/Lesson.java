package com.driving.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lesson extends DataModel {
    private String studentId;
    private String instructorId;
    private String vehicleType;
    private String lessonType;
    private String date;
    private String time;
    private String status;

    // Encapsulation: private fields with public getters/setters
    public Lesson(String id, String studentId, String instructorId, String vehicleType, String lessonType, String date, String time, String status) {
        super(id);
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.vehicleType = vehicleType;
        this.lessonType = lessonType;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Polymorphism: Override toFileString from DataModel
    @Override
    public String toFileString() {
        return getId() + "," + studentId + "," + instructorId + "," + vehicleType + "," + lessonType + "," + date + "," + time + "," + status;
    }

    // CRUD Operations using File Handling
    private static final String FILE_PATH = "lessons.txt";

    // Create: Book a new lesson
    public void bookLesson() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(this.toFileString());
            writer.newLine();
        }
    }

    // Read: Retrieve all lessons
    public static List<Lesson> getAllLessons() throws IOException {
        List<Lesson> lessons = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return lessons;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    Lesson lesson = new Lesson(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    // Read: Retrieve a lesson by ID
    public static Lesson getLessonById(String id) throws IOException {
        for (Lesson lesson : getAllLessons()) {
            if (lesson.getId().equals(id)) {
                return lesson;
            }
        }
        return null;
    }

    // Update: Update lesson details
    public static void updateLesson(Lesson updatedLesson) throws IOException {
        List<Lesson> lessons = getAllLessons();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Lesson lesson : lessons) {
                if (lesson.getId().equals(updatedLesson.getId())) {
                    writer.write(updatedLesson.toFileString());
                } else {
                    writer.write(lesson.toFileString());
                }
                writer.newLine();
            }
        }
    }

    // Delete: Cancel a lesson
    public static void cancelLesson(String id) throws IOException {
        List<Lesson> lessons = getAllLessons();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Lesson lesson : lessons) {
                if (!lesson.getId().equals(id)) {
                    writer.write(lesson.toFileString());
                    writer.newLine();
                }
            }
        }
    }
}
