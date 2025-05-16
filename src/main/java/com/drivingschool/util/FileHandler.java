package com.drivingschool.util;

import com.drivingschool.model.Lesson;
import com.drivingschool.model.Student;
import com.drivingschool.model.Instructor;
import com.drivingschool.model.Admin;
import com.drivingschool.model.Review;
import com.drivingschool.model.Payment;
import com.drivingschool.model.Student.LessonRequest;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileHandler {
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());
    private static final String DATA_DIR = "WEB-INF/data/";
    private static final String STUDENTS_FILE = DATA_DIR + "students.txt";
    private static final String LESSONS_FILE = DATA_DIR + "lessons.txt";
    private static final String INSTRUCTORS_FILE = DATA_DIR + "instructors.txt";
    private static final String ADMINS_FILE = DATA_DIR + "admins.txt";
    private static final String REVIEWS_FILE = DATA_DIR + "reviews.txt";
    private static final String PAYMENTS_FILE = DATA_DIR + "payments.txt";
    private static final double FULL_COURSE_COST = 35000.00;
    private static final double FIRST_INSTALLMENT = 15000.00;
    private static final double SECOND_INSTALLMENT = 20000.00;

    private final ServletContext servletContext;

    // Constructor to inject ServletContext
    public FileHandler(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    // Resolve file path using ServletContext
    private String getFilePath(String relativePath) {
        String realPath = servletContext.getRealPath(relativePath);
        if (realPath == null) {
            throw new IllegalStateException("Cannot resolve path for: " + relativePath +
                    ". Ensure file exists in WEB-INF/data/ and Tomcat is configured correctly.");
        }
        LOGGER.info("Resolved file path: " + realPath);
        return realPath;
    }

    // Escape commas in fields to prevent parsing issues
    private String escapeField(String field) {
        if (field == null) return "";
        return field.replace(",", "\\,");
    }

    // Unescape commas when reading fields
    private String unescapeField(String field) {
        if (field == null) return "";
        return field.replace("\\,", ",");
    }

    // Read all students from students.txt
    public synchronized List<Student> readStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        String filePath = getFilePath(STUDENTS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Students file does not exist: " + filePath);
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Handle escaped commas by splitting carefully
                String[] parts = line.split("(?<!\\\\),");
                if (parts.length < 10) {
                    LOGGER.warning("Skipping invalid student line: " + line);
                    continue;
                }

                // Parse User fields
                String userId = parts[0].split("=")[1];
                String userName = unescapeField(parts[1].split("=")[1]);
                String userDOB = parts[2].split("=")[1];
                String userAddress = unescapeField(parts[3].split("=")[1]);
                String phoneNumber = parts[4].split("=")[1];
                String userEmail = parts[5].split("=")[1];
                String password = parts[6].split("=")[1];
                String role = parts[7].split("=")[1];

                // Parse Student-specific fields
                int lessonsCompleted;
                try {
                    lessonsCompleted = Integer.parseInt(parts[8].split("=")[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid lessonsCompleted in line: " + line);
                    continue;
                }
                String paymentStatus = parts[9].split("=")[1];

                // Create Student object
                Student student = new Student(userName, userDOB, userAddress, phoneNumber, userEmail, password);
                student.setRole(role);
                student.setUserId(userId);
                student.setLessonsCompleted(lessonsCompleted);
                student.setPaymentStatus(paymentStatus);

                // Parse lesson requests (if any)
                if (parts.length > 10 && parts[10].startsWith("lessonRequests=")) {
                    String requestsStr = parts[10].split("=", 2)[1];
                    if (!requestsStr.equals("none")) {
                        String[] requests = requestsStr.split("\\|");
                        for (String request : requests) {
                            String[] reqParts = request.split(":");
                            if (reqParts.length != 3) {
                                LOGGER.warning("Invalid lesson request format: " + request);
                                continue;
                            }
                            try {
                                int lessonNumber = Integer.parseInt(reqParts[0]);
                                String vehicleType = reqParts[1];
                                String vehicle = reqParts[2];
                                student.requestLesson(lessonNumber, vehicleType, vehicle);
                            } catch (NumberFormatException e) {
                                LOGGER.warning("Invalid lesson number in request: " + request);
                            }
                        }
                    }
                }

                students.add(student);
            }
        }
        LOGGER.info("Read " + students.size() + " students from: " + filePath);
        return students;
    }

    // Write all students to students.txt
    public synchronized void writeStudents(List<Student> students) throws IOException {
        String filePath = getFilePath(STUDENTS_FILE);
        LOGGER.info("Writing " + students.size() + " students to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
            LOGGER.info("Created data directory: " + dir.getAbsolutePath());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : students) {
                StringBuilder line = new StringBuilder();
                // User fields
                line.append("userId=").append(student.getUserId()).append(",");
                line.append("userName=").append(escapeField(student.getUserName())).append(",");
                line.append("userDOB=").append(student.getUserDOB()).append(",");
                line.append("userAddress=").append(escapeField(student.getUserAddress())).append(",");
                line.append("phoneNumber=").append(student.getPhoneNumber()).append(",");
                line.append("userEmail=").append(student.getUserEmail()).append(",");
                line.append("password=").append(student.getPassword()).append(",");
                line.append("role=").append(student.getRole()).append(",");
                // Student fields
                line.append("lessonsCompleted=").append(student.getLessonsCompleted()).append(",");
                line.append("paymentStatus=").append(student.getPaymentStatus()).append(",");
                // Lesson requests
                line.append("lessonRequests=");
                Student.CustomQueue requests = student.getLessonRequests();
                if (requests.isEmpty()) {
                    line.append("none");
                } else {
                    StringBuilder requestsStr = new StringBuilder();
                    Student.CustomQueue tempQueue = new Student.CustomQueue(requests.size() + 10);
                    while (!requests.isEmpty()) {
                        LessonRequest req = requests.dequeue();
                        tempQueue.enqueue(req);
                        requestsStr.append(req.getLessonNumber()).append(":")
                                .append(req.getVehicleType()).append(":")
                                .append(req.getVehicle());
                        if (!requests.isEmpty()) {
                            requestsStr.append("|");
                        }
                    }
                    while (!tempQueue.isEmpty()) {
                        requests.enqueue(tempQueue.dequeue());
                    }
                    line.append(requestsStr);
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote students to: " + filePath);
    }

    // Add a single student (Create/Update)
    public synchronized void addStudent(Student student) throws IOException {
        List<Student> students = readStudents();
        students.removeIf(s -> s.getUserId().equals(student.getUserId()));
        students.add(student);
        writeStudents(students);
    }

    // Delete a student
    public synchronized void deleteStudent(String userId) throws IOException {
        List<Student> students = readStudents();
        students.removeIf(s -> s.getUserId().equals(userId));
        writeStudents(students);
    }

    // Read all lessons from lessons.txt
    public synchronized List<Lesson> readLessons() throws IOException {
        List<Lesson> lessons = new ArrayList<>();
        String filePath = getFilePath(LESSONS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Lessons file does not exist: " + filePath);
            return lessons;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    LOGGER.warning("Skipping invalid lesson line: " + line);
                    continue;
                }

                String lessonId = parts[0].split("=")[1];
                String studentId = parts[1].split("=")[1];
                String instructorId = parts[2].split("=")[1].equals("null") ? null : parts[2].split("=")[1];
                int lessonNumber;
                try {
                    lessonNumber = Integer.parseInt(parts[3].split("=")[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid lessonNumber in line: " + line);
                    continue;
                }
                String vehicleType = parts[4].split("=")[1];
                String vehicle = parts[5].split("=")[1];
                String status = parts[6].split("=")[1];

                Lesson lesson = new Lesson(lessonId, studentId, lessonNumber, vehicleType, vehicle);
                lesson.setInstructorId(instructorId);
                lesson.setStatus(status);
                lessons.add(lesson);
            }
        }
        LOGGER.info("Read " + lessons.size() + " lessons from: " + filePath);
        return lessons;
    }

    // Write all lessons to lessons.txt
    public synchronized void writeLessons(List<Lesson> lessons) throws IOException {
        String filePath = getFilePath(LESSONS_FILE);
        LOGGER.info("Writing " + lessons.size() + " lessons to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Lesson lesson : lessons) {
                String line = String.format("lessonId=%s,studentId=%s,instructorId=%s,lessonNumber=%d,vehicleType=%s,vehicle=%s,status=%s",
                        lesson.getLessonId(),
                        lesson.getStudentId(),
                        lesson.getInstructorId() != null ? lesson.getInstructorId() : "null",
                        lesson.getLessonNumber(),
                        lesson.getVehicleType(),
                        lesson.getVehicle(),
                        lesson.getStatus());
                writer.write(line);
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote lessons to: " + filePath);
    }

    // Add a single lesson (Create/Update)
    public synchronized void addLesson(Lesson lesson) throws IOException {
        List<Lesson> lessons = readLessons();
        lessons.removeIf(l -> l.getLessonId().equals(lesson.getLessonId()));
        lessons.add(lesson);
        writeLessons(lessons);
    }

    // Delete a lesson
    public synchronized void deleteLesson(String lessonId) throws IOException {
        List<Lesson> lessons = readLessons();
        lessons.removeIf(l -> l.getLessonId().equals(lessonId));
        writeLessons(lessons);
    }

    // Read all payments from payments.txt
    public synchronized List<Payment> readPayments() throws IOException {
        List<Payment> payments = new ArrayList<>();
        String filePath = getFilePath(PAYMENTS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Payments file does not exist: " + filePath);
            return payments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    LOGGER.warning("Skipping invalid payment line: " + line);
                    continue;
                }

                String paymentId = parts[0].split("=")[1];
                String studentId = parts[1].split("=")[1];
                double amount;
                try {
                    amount = Double.parseDouble(parts[2].split("=")[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid amount in line: " + line);
                    continue;
                }
                Payment.PaymentType paymentType;
                try {
                    paymentType = Payment.PaymentType.valueOf(parts[3].split("=")[1]);
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("Invalid payment type in line: " + line);
                    continue;
                }
                String paymentDate = parts[4].split("=")[1];
                String status = parts[5].split("=")[1];

                try {
                    Payment payment = new Payment(paymentId, studentId, amount, paymentType, paymentDate, status);
                    payments.add(payment);
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("Failed to create payment from line: " + line + ", error: " + e.getMessage());
                    continue;
                }
            }
        }
        LOGGER.info("Read " + payments.size() + " payments from: " + filePath);
        return payments;
    }

    // Write all payments to payments.txt
    public synchronized void writePayments(List<Payment> payments) throws IOException {
        String filePath = getFilePath(PAYMENTS_FILE);
        LOGGER.info("Writing " + payments.size() + " payments to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Payment payment : payments) {
                String line = String.format("paymentId=%s,studentId=%s,amount=%.2f,paymentType=%s,paymentDate=%s,status=%s",
                        payment.getPaymentId(),
                        payment.getStudentId(),
                        payment.getAmount(),
                        payment.getPaymentType(),
                        payment.getPaymentDate(),
                        payment.getStatus());
                writer.write(line);
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote payments to: " + filePath);
    }

    // Add a single payment and update student payment status
    public synchronized void addPayment(Payment payment) throws IOException {
        List<Student> students = readStudents();
        Student student = students.stream()
                .filter(s -> s.getUserId().equals(payment.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + payment.getStudentId()));

        if (payment.getPaymentType() == Payment.PaymentType.INSTALLMENT && payment.getAmount() == SECOND_INSTALLMENT) {
            long completedLessons = readLessons().stream()
                    .filter(l -> l.getStudentId().equals(student.getUserId()) && l.getStatus().equals("completed"))
                    .count();
            if (completedLessons < 2) {
                throw new IllegalArgumentException("Second installment requires at least two completed lessons");
            }
        }

        List<Payment> payments = readPayments();
        payments.removeIf(p -> p.getPaymentId().equals(payment.getPaymentId()));
        payments.add(payment);
        writePayments(payments);

        updateStudentPaymentStatus(student, payments);
    }

    // Helper method to update student payment status
    private void updateStudentPaymentStatus(Student student, List<Payment> payments) throws IOException {
        double totalPaid = payments.stream()
                .filter(p -> p.getStudentId().equals(student.getUserId()) && p.getStatus().equals("COMPLETED"))
                .mapToDouble(Payment::getAmount)
                .sum();

        String newStatus;
        if (totalPaid >= FULL_COURSE_COST) {
            newStatus = "full";
        } else if (totalPaid > 0) {
            newStatus = "partial";
        } else {
            newStatus = "unpaid";
        }

        student.setPaymentStatus(newStatus);
        List<Student> students = readStudents();
        students.removeIf(s -> s.getUserId().equals(student.getUserId()));
        students.add(student);
        writeStudents(students);
    }

    // Read all instructors from instructors.txt
    public synchronized List<Instructor> readInstructors() throws IOException {
        List<Instructor> instructors = new ArrayList<>();
        String filePath = getFilePath(INSTRUCTORS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Instructors file does not exist: " + filePath);
            return instructors;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!\\\\),");
                if (parts.length < 10) {
                    LOGGER.warning("Skipping invalid instructor line: " + line);
                    continue;
                }

                String userId = parts[0].split("=")[1];
                String userName = unescapeField(parts[1].split("=")[1]);
                String userDOB = parts[2].split("=")[1];
                String userAddress = unescapeField(parts[3].split("=")[1]);
                String phoneNumber = parts[4].split("=")[1];
                String userEmail = parts[5].split("=")[1];
                String password = parts[6].split("=")[1];
                String role = parts[7].split("=")[1];
                int experienceYears;
                try {
                    experienceYears = Integer.parseInt(parts[8].split("=")[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid experienceYears in line: " + line);
                    continue;
                }
                boolean isAvailable = Boolean.parseBoolean(parts[9].split("=")[1]);

                Instructor instructor = new Instructor(userName, userDOB, userAddress, phoneNumber, userEmail, password, experienceYears);
                instructor.setRole(role);
                instructor.setUserId(userId);
                instructor.setAvailable(isAvailable);
                instructors.add(instructor);
            }
        }
        LOGGER.info("Read " + instructors.size() + " instructors from: " + filePath);
        return instructors;
    }

    // Write all instructors to instructors.txt
    public synchronized void writeInstructors(List<Instructor> instructors) throws IOException {
        String filePath = getFilePath(INSTRUCTORS_FILE);
        LOGGER.info("Writing " + instructors.size() + " instructors to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Instructor instructor : instructors) {
                String line = String.format("userId=%s,userName=%s,userDOB=%s,userAddress=%s,phoneNumber=%s,userEmail=%s,password=%s,role=%s,experienceYears=%d,isAvailable=%b",
                        instructor.getUserId(),
                        escapeField(instructor.getUserName()),
                        instructor.getUserDOB(),
                        escapeField(instructor.getUserAddress()),
                        instructor.getPhoneNumber(),
                        instructor.getUserEmail(),
                        instructor.getPassword(),
                        instructor.getRole(),
                        instructor.getExperienceYears(),
                        instructor.isAvailable());
                writer.write(line);
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote instructors to: " + filePath);
    }

    // Add a single instructor (Create/Update)
    public synchronized void addInstructor(Instructor instructor) throws IOException {
        List<Instructor> instructors = readInstructors();
        instructors.removeIf(i -> i.getUserId().equals(instructor.getUserId()));
        instructors.add(instructor);
        writeInstructors(instructors);
    }

    // Delete an instructor
    public synchronized void deleteInstructor(String userId) throws IOException {
        List<Instructor> instructors = readInstructors();
        instructors.removeIf(i -> i.getUserId().equals(userId));
        writeInstructors(instructors);
    }

    // Read all admins from admins.txt
    public synchronized List<Admin> readAdmins() throws IOException {
        List<Admin> admins = new ArrayList<>();
        String filePath = getFilePath(ADMINS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Admins file does not exist: " + filePath);
            return admins;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!\\\\),");
                if (parts.length < 8) {
                    LOGGER.warning("Skipping invalid admin line: " + line);
                    continue;
                }

                String userId = parts[0].split("=")[1];
                String userName = unescapeField(parts[1].split("=")[1]);
                String userDOB = parts[2].split("=")[1];
                String userAddress = unescapeField(parts[3].split("=")[1]);
                String phoneNumber = parts[4].split("=")[1];
                String userEmail = parts[5].split("=")[1];
                String password = parts[6].split("=")[1];
                String role = parts[7].split("=")[1];

                Admin admin = new Admin(userName, userDOB, userAddress, phoneNumber, userEmail, password);
                admin.setRole(role);
                admin.setUserId(userId);
                admins.add(admin);
            }
        }
        LOGGER.info("Read " + admins.size() + " admins from: " + filePath);
        return admins;
    }

    // Write all admins to admins.txt
    public synchronized void writeAdmins(List<Admin> admins) throws IOException {
        String filePath = getFilePath(ADMINS_FILE);
        LOGGER.info("Writing " + admins.size() + " admins to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Admin admin : admins) {
                String line = String.format("userId=%s,userName=%s,userDOB=%s,userAddress=%s,phoneNumber=%s,userEmail=%s,password=%s,role=%s",
                        admin.getUserId(),
                        escapeField(admin.getUserName()),
                        admin.getUserDOB(),
                        escapeField(admin.getUserAddress()),
                        admin.getPhoneNumber(),
                        admin.getUserEmail(),
                        admin.getPassword(),
                        admin.getRole());
                writer.write(line);
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote admins to: " + filePath);
    }

    // Add a single admin (Create/Update)
    public synchronized void addAdmin(Admin admin) throws IOException {
        List<Admin> admins = readAdmins();
        admins.removeIf(a -> a.getUserId().equals(admin.getUserId()));
        admins.add(admin);
        writeAdmins(admins);
    }

    // Delete an admin
    public synchronized void deleteAdmin(String userId) throws IOException {
        List<Admin> admins = readAdmins();
        admins.removeIf(a -> a.getUserId().equals(userId));
        writeAdmins(admins);
    }

    // Read all reviews from reviews.txt
    public synchronized List<Review> readReviews() throws IOException {
        List<Review> reviews = new ArrayList<>();
        String filePath = getFilePath(REVIEWS_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("Reviews file does not exist: " + filePath);
            return reviews;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!\\\\),");
                if (parts.length < 7) {
                    LOGGER.warning("Skipping invalid review line: " + line);
                    continue;
                }

                String reviewId = parts[0].split("=")[1];
                String studentId = parts[1].split("=")[1];
                String lessonId = parts[2].split("=")[1];
                String instructorId = parts[3].split("=")[1].equals("null") ? null : parts[3].split("=")[1];
                int rating;
                try {
                    rating = Integer.parseInt(parts[4].split("=")[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid rating in line: " + line);
                    continue;
                }
                String comment = unescapeField(parts[5].split("=")[1]);
                String reviewDate = parts[6].split("=")[1];

                try {
                    Review review = new Review(reviewId, studentId, lessonId, instructorId, rating, comment, reviewDate);
                    reviews.add(review);
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("Failed to create review from line: " + line + ", error: " + e.getMessage());
                    continue;
                }
            }
        }
        LOGGER.info("Read " + reviews.size() + " reviews from: " + filePath);
        return reviews;
    }

    // Write all reviews to reviews.txt
    public synchronized void writeReviews(List<Review> reviews) throws IOException {
        String filePath = getFilePath(REVIEWS_FILE);
        LOGGER.info("Writing " + reviews.size() + " reviews to: " + filePath);
        File dir = new File(getFilePath(DATA_DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Review review : reviews) {
                String line = String.format("reviewId=%s,studentId=%s,lessonId=%s,instructorId=%s,rating=%d,comment=%s,reviewDate=%s",
                        review.getReviewId(),
                        review.getStudentId(),
                        review.getLessonId(),
                        review.getInstructorId() != null ? review.getInstructorId() : "null",
                        review.getRating(),
                        escapeField(review.getComment()),
                        review.getReviewDate());
                writer.write(line);
                writer.newLine();
            }
        }
        LOGGER.info("Successfully wrote reviews to: " + filePath);
    }

    // Add a single review (Create/Update)
    public synchronized void addReview(Review review) throws IOException {
        List<Review> reviews = readReviews();
        reviews.removeIf(r -> r.getReviewId().equals(review.getReviewId()));
        reviews.add(review);
        writeReviews(reviews);
    }

    // Delete a review
    public synchronized void deleteReview(String reviewId) throws IOException {
        List<Review> reviews = readReviews();
        boolean removed = reviews.removeIf(r -> r.getReviewId().equals(reviewId));
        if (!removed) {
            throw new IllegalArgumentException("Review not found: " + reviewId);
        }
        writeReviews(reviews);
    }
}