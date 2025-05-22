package com.drivingschool.util;

import com.drivingschool.model.Admin;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.Student;
import com.drivingschool.model.User;
import com.drivingschool.model.Review;
import com.drivingschool.model.LessonBooking;
import com.drivingschool.model.Payment;
import com.drivingschool.model.Assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final String filePath;
    private final Object fileLock = new Object();

    public FileHandler(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file: " + filePath, e);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void saveStudent(Student student) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(student.toTextLine());
                writer.newLine();
            }
        }
    }

    public void saveInstructor(Instructor instructor) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(instructor.toTextLine());
                writer.newLine();
            }
        }
    }

    public void saveAdmin(Admin admin) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(admin.toTextLine());
                writer.newLine();
            }
        }
    }

    public void saveReview(Review review, String studentId) throws IOException {
        synchronized (fileLock) {
            List<Review> reviews = readAllReviews();
            reviews.removeIf(r -> r.getStudentId().equals(studentId));
            reviews.add(review);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Review r : reviews) {
                    writer.write(r.toTextLine());
                    writer.newLine();
                }
            }
        }
    }

    public void saveLessonBooking(LessonBooking booking) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(booking.toTextLine());
                writer.newLine();
            }
        }
    }

    public void savePayment(Payment payment) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(payment.toTextLine());
                writer.newLine();
            }
        }
    }

    public void saveConfirmedBooking(String bookingId) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(bookingId);
                writer.newLine();
            }
        }
    }

    public void saveAssignment(Assignment assignment) throws IOException {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(assignment.toTextLine());
                writer.newLine();
            }
        }
    }

    public void updateInstructor(Instructor updatedInstructor) throws IOException {
        synchronized (fileLock) {
            List<Instructor> instructors = readAllInstructors();
            boolean updated = false;
            for (int i = 0; i < instructors.size(); i++) {
                if (instructors.get(i).getId().equals(updatedInstructor.getId())) {
                    instructors.set(i, updatedInstructor);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                throw new IOException("Instructor not found: " + updatedInstructor.getId());
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Instructor instructor : instructors) {
                    writer.write(instructor.toTextLine());
                    writer.newLine();
                }
            }
        }
    }

    public void deleteStudent(String studentId) throws IOException {
        synchronized (fileLock) {
            List<Student> students = readAllStudents();
            boolean deleted = students.removeIf(student -> student.getId().equals(studentId));
            if (!deleted) {
                throw new IOException("Student not found: " + studentId);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Student student : students) {
                    writer.write(student.toTextLine());
                    writer.newLine();
                }
            }
        }
    }

    public void deleteInstructor(String instructorId) throws IOException {
        synchronized (fileLock) {
            List<Instructor> instructors = readAllInstructors();
            boolean deleted = instructors.removeIf(instructor -> instructor.getId().equals(instructorId));
            if (!deleted) {
                throw new IOException("Instructor not found: " + instructorId);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Instructor instructor : instructors) {
                    writer.write(instructor.toTextLine());
                    writer.newLine();
                }
            }
        }
    }

    public List<String> readConfirmedBookings() {
        List<String> bookingIds = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        bookingIds.add(line.trim());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading queue.txt: " + e.getMessage());
            }
        }
        return bookingIds;
    }

    public List<Assignment> readAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Assignment assignment = Assignment.fromTextLine(line);
                        assignments.add(assignment);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in assignments.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading assignments.txt: " + e.getMessage());
            }
        }
        return assignments;
    }

    public List<Payment> readAllPayments() {
        List<Payment> payments = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Payment payment = Payment.fromTextLine(line);
                        payments.add(payment);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in payments.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading payments.txt: " + e.getMessage());
            }
        }
        return payments;
    }

    public boolean emailExists(String email) {
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        if (filePath.endsWith("students.txt")) {
                            Student student = Student.fromTextLine(line);
                            if (student.getEmail().equalsIgnoreCase(email)) {
                                return true;
                            }
                        } else if (filePath.endsWith("instructors.txt")) {
                            Instructor instructor = Instructor.fromTextLine(line);
                            if (instructor.getEmail().equalsIgnoreCase(email)) {
                                return true;
                            }
                        } else if (filePath.endsWith("admins.txt")) {
                            Admin admin = Admin.fromTextLine(line);
                            if (admin.getEmail().equalsIgnoreCase(email)) {
                                return true;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading " + filePath + ": " + e.getMessage());
            }
            return false;
        }
    }

    public User authenticate(String email, String password) {
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        User user = null;
                        if (filePath.endsWith("students.txt")) {
                            user = Student.fromTextLine(line);
                        } else if (filePath.endsWith("instructors.txt")) {
                            user = Instructor.fromTextLine(line);
                        } else if (filePath.endsWith("admins.txt")) {
                            user = Admin.fromTextLine(line);
                        }
                        if (user != null && user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                            return user;
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading " + filePath + ": " + e.getMessage());
            }
            return null;
        }
    }

    public List<Student> readAllStudents() {
        List<Student> students = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Student student = Student.fromTextLine(line);
                        students.add(student);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in students.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading students.txt: " + e.getMessage());
            }
        }
        return students;
    }

    public List<Instructor> readAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Instructor instructor = Instructor.fromTextLine(line);
                        instructors.add(instructor);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in instructors.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading instructors.txt: " + e.getMessage());
            }
        }
        return instructors;
    }

    public List<Admin> readAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Admin admin = Admin.fromTextLine(line);
                        admins.add(admin);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in admins.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading admins.txt: " + e.getMessage());
            }
        }
        return admins;
    }

    public List<Review> readAllReviews() {
        List<Review> reviews = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Review review = Review.fromTextLine(line);
                        reviews.add(review);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in reviews.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading reviews.txt: " + e.getMessage());
            }
        }
        return reviews;
    }

    public List<LessonBooking> readAllLessonBookings() {
        List<LessonBooking> bookings = new ArrayList<>();
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        LessonBooking booking = LessonBooking.fromTextLine(line);
                        bookings.add(booking);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line in bookings.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading bookings.txt: " + e.getMessage());
            }
        }
        return bookings;
    }
}
