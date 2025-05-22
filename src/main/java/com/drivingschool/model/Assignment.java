package com.drivingschool.model;

public class Assignment {
    private String assignmentId;    // Unique identifier for the assignment
    private String bookingId;       // ID of the associated booking
    private String studentId;       // ID of the student
    private String instructorId;    // ID of the assigned instructor
    private long timestamp;         // Assignment timestamp (epoch milliseconds)

    public Assignment(String assignmentId, String bookingId, String studentId, String instructorId, long timestamp) {
        this.assignmentId = assignmentId;
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

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

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert to text line for file storage
    public String toTextLine() {
        return String.join(",", assignmentId, bookingId, studentId, instructorId, String.valueOf(timestamp));
    }

    // Parse from text line
    public static Assignment fromTextLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid assignment data format: " + line);
        }
        String assignmentId = parts[0];
        String bookingId = parts[1];
        String studentId = parts[2];
        String instructorId = parts[3];
        long timestamp;
        try {
            timestamp = Long.parseLong(parts[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid timestamp format: " + parts[4]);
        }
        return new Assignment(assignmentId, bookingId, studentId, instructorId, timestamp);
    }
}