package com.drivingschool.model;

public class Review {
    private String studentId;
    private String studentName;
    private int rating;
    private String comment;

    public Review(String studentId, String studentName, int rating, String comment) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String toTextLine() {
        // Escape commas in comment to prevent CSV issues
        String escapedComment = comment.replace(",", "\\,");
        return String.join(",", studentId, studentName, String.valueOf(rating), escapedComment);
    }

    public static Review fromTextLine(String line) {
        String[] parts = line.split(",(?=([^\\\\]|\\\\.)*$)", -1); // Split on commas not preceded by a backslash
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid review data format: " + line);
        }
        String studentId = parts[0];
        String studentName = parts[1];
        int rating = parseRating(parts[2]);
        String comment = parts[3].replace("\\,", ","); // Unescape commas
        return new Review(studentId, studentName, rating, comment);
    }

    private static int parseRating(String value) {
        try {
            int rating = Integer.parseInt(value);
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            return rating;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating: " + value);
        }
    }
}