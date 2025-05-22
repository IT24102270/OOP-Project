package com.drivingschool.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LessonBooking {
    private String bookingId;       // Unique identifier for the booking
    private String studentId;       // ID of the student making the booking
    private String vehicleType;     // "light" or "heavy"
    private List<String> lessons;   // List of selected lessons (e.g., ["motorcycle", "car"])
    private boolean writtenExamPrep;// Whether written exam prep is included
    private double totalCost;       // Total cost of the booking

    public LessonBooking(String bookingId, String studentId, String vehicleType, List<String> lessons, boolean writtenExamPrep, double totalCost) {
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.vehicleType = vehicleType;
        this.lessons = lessons;
        this.writtenExamPrep = writtenExamPrep;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }

    public boolean isWrittenExamPrep() {
        return writtenExamPrep;
    }

    public void setWrittenExamPrep(boolean writtenExamPrep) {
        this.writtenExamPrep = writtenExamPrep;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // Convert to text line for file storage (comma-separated, escaping commas in lessons)
    public String toTextLine() {
        String escapedLessons = lessons.stream()
                .map(lesson -> lesson.replace(",", "\\,"))
                .collect(Collectors.joining(";")); // Use semicolon to separate lessons
        return String.join(",", bookingId, studentId, vehicleType, escapedLessons,
                String.valueOf(writtenExamPrep), String.valueOf(totalCost));
    }

    // Parse from text line
    public static LessonBooking fromTextLine(String line) {
        String[] parts = line.split(",(?=([^\\\\]|\\\\.)*$)", -1); // Split on commas not preceded by a backslash
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid booking data format: " + line);
        }
        String bookingId = parts[0];
        String studentId = parts[1];
        String vehicleType = parts[2];
        List<String> lessons = Arrays.stream(parts[3].split(";"))
                .map(lesson -> lesson.replace("\\,", ","))
                .collect(Collectors.toList());
        boolean writtenExamPrep = Boolean.parseBoolean(parts[4]);
        double totalCost;
        try {
            totalCost = Double.parseDouble(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid total cost format: " + parts[5]);
        }
        return new LessonBooking(bookingId, studentId, vehicleType, lessons, writtenExamPrep, totalCost);
    }
}