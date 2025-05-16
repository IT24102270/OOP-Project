package com.drivingschool.model;

public class Student extends User {
    private int lessonsCompleted;
    private String paymentStatus;
    private CustomQueue lessonRequests;
    private static int studentId = 1000;

    // Constructor (Create)
    public Student(String userName, String userDOB, String userAddress, String phoneNumber,
                   String userEmail, String password) {
        super(userName, userDOB, userAddress, phoneNumber, userEmail, password, "student");
        this.userId = generateStudentId();
        this.lessonsCompleted = 0;
        this.paymentStatus = "unpaid";
        this.lessonRequests = new CustomQueue(10); // Max 10 requests
    }

    // Getters (Read)
    public int getLessonsCompleted() {
        return lessonsCompleted;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public CustomQueue getLessonRequests() {
        return lessonRequests;
    }

    // Setters (Update)
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setRole(String role) {
        this.role = role;
    }
    private synchronized String generateStudentId() { // synchronized - prevent race conditions
        return "S"+studentId++;
    }
    public void setLessonsCompleted(int lessonsCompleted) {
        if (lessonsCompleted < 0) {
            throw new IllegalArgumentException("Lessons completed cannot be negative");
        }
        this.lessonsCompleted = lessonsCompleted;
    }

    public void setPaymentStatus(String paymentStatus) {
        if (!paymentStatus.equals("unpaid") && !paymentStatus.equals("partial") && !paymentStatus.equals("full")) {
            throw new IllegalArgumentException("Invalid payment status");
        }
        this.paymentStatus = paymentStatus;
    }

    // Student-specific methods
    public void completeLesson() {
        if (lessonsCompleted >= 5) {
            throw new IllegalStateException("Student has already completed all required lessons");
        }
        lessonsCompleted++;
    }

    public void requestLesson(int lessonNumber, String vehicleType, String vehicle) {
        if (lessonNumber < 1 || lessonNumber > 5) {
            throw new IllegalArgumentException("Lesson number must be between 1 and 5");
        }
        if (!vehicleType.equals("light") && !vehicleType.equals("heavy")) {
            throw new IllegalArgumentException("Vehicle type must be 'light' or 'heavy'");
        }
        if (!isValidVehicle(vehicle, vehicleType)) {
            throw new IllegalArgumentException("Invalid vehicle for the selected type");
        }
        if (lessonNumber <= lessonsCompleted) {
            throw new IllegalStateException("Cannot request a lesson already completed");
        }
        if (lessonNumber > lessonsCompleted + 1) {
            throw new IllegalStateException("Must complete lesson " + (lessonsCompleted + 1) + " first");
        }
        if (lessonNumber >= 3 && !paymentStatus.equals("full")) {
            throw new IllegalStateException("Full payment required before requesting lesson 3");
        }

        LessonRequest request = new LessonRequest(lessonNumber, vehicleType, vehicle);
        lessonRequests.enqueue(request);
    }

    public boolean isEligibleForExam() {
        return lessonsCompleted >= 5;
    }

    // Delete (mark for removal, actual deletion handled by FileHandler)
    public void deleteStudent() {
        setLessonsCompleted(0);
        setPaymentStatus("unpaid");
        lessonRequests = new CustomQueue(10); // Reset queue
        // FileHandler will remove the student from storage
    }

    // Helper method to validate vehicle
    private boolean isValidVehicle(String vehicle, String vehicleType) {
        if (vehicleType.equals("light")) {
            return vehicle.equals("car") || vehicle.equals("motorcycle") || vehicle.equals("threewheeler");
        } else {
            return vehicle.equals("lorry") || vehicle.equals("bus");
        }
    }

    // Inner class for lesson requests
    public static class LessonRequest {
        private int lessonNumber;
        private String vehicleType;
        private String vehicle;

        public LessonRequest(int lessonNumber, String vehicleType, String vehicle) {
            this.lessonNumber = lessonNumber;
            this.vehicleType = vehicleType;
            this.vehicle = vehicle;
        }

        public int getLessonNumber() {
            return lessonNumber;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public String getVehicle() {
            return vehicle;
        }
    }

    // Custom Queue implementation
    public static class CustomQueue {
        private LessonRequest[] queue;
        private int front;
        private int rear;
        private int size;   // to get the count
        private int capacity;  // the size of the array

        public CustomQueue(int capacity) {
            this.capacity = capacity;
            this.queue = new LessonRequest[capacity];
            this.front = 0;
            this.rear = -1;
            this.size = 0;
        }

        public void enqueue(LessonRequest request) {
            if (isFull()) {
                throw new IllegalStateException("Queue is full");
            }
            rear = (rear + 1) % capacity; // Circular queue
            queue[rear] = request;
            size++;
        }

        public LessonRequest dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            LessonRequest request = queue[front];
            queue[front] = null;
            front = (front + 1) % capacity; // Circular queue
            size--;
            return request;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }

        public int size() {
            return size;
        }

        public LessonRequest peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            return queue[front];
        }
    }
}