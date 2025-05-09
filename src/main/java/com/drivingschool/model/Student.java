package com.drivingschool.model;

import java.util.*;

public class Student extends User {
    private String vehicleClass;
    private String selectedVehicle;
    private int lessonsCompleted;
    private Queue<String> lessonRequests;


    private static final Map<String, List<String>> VEHICLE_CLASSES = new HashMap<>();
    static {
        VEHICLE_CLASSES.put("Light Vehicle", Arrays.asList("Car", "Bike", "Threewheeler"));
        VEHICLE_CLASSES.put("Heavy Vehicle", Arrays.asList("Van", "Lorry", "Bus", "Truck"));
    }

    public Student(String userName, String userId, String userAddress, String phoneNumber,
                   String userEmail, String password, String vehicleInput) {
        super(userName, userId, userAddress, phoneNumber, userEmail, password, "student");
        determineVehicleClass(vehicleInput); // Sets both vehicleClass & selectedVehicle
        this.lessonsCompleted = 0;
        this.lessonRequests = new LinkedList<>();
    }

    // Determine vehicle class from either vehicle or class name
    private void determineVehicleClass(String input) {
        // Check if it's a class name
        if (VEHICLE_CLASSES.containsKey(input)) {
            this.vehicleClass = input;
            this.selectedVehicle = VEHICLE_CLASSES.get(input).get(0); // Default to first vehicle
        } else {
            // Search for the vehicle and assign the matching class
            for (Map.Entry<String, List<String>> entry : VEHICLE_CLASSES.entrySet()) {
                if (entry.getValue().contains(input)) {
                    this.vehicleClass = entry.getKey();
                    this.selectedVehicle = input;
                    return;
                }
            }
            // If not found, assign as Unknown
            this.vehicleClass = "Unknown";
            this.selectedVehicle = input;
        }
    }

    // Getters
    public String getVehicleClass() {
        return vehicleClass;
    }
    public String getSelectedVehicle() {
        return selectedVehicle;
    }
    public int getLessonsCompleted() {
        return lessonsCompleted;
    }
    public Queue<String> getLessonRequests() {
        return lessonRequests;
    }

    // Student Actions
    public void registerLesson(String lessonId) {
        if (lessonsCompleted >= 5) {
            System.out.println("All lessons completed. You're ready for the practical exam!");
            return;
        }
        lessonRequests.add(lessonId);
        System.out.println("Lesson " + lessonId + " has been added.");
    }

    public void completeLesson() {
        if (!lessonRequests.isEmpty()) {
            String lesson = lessonRequests.poll();
            lessonsCompleted++;
            System.out.println("Completed: " + lesson + " | Total completed: " + lessonsCompleted);
            if (lessonsCompleted == 5) {
                System.out.println("All 5 lessons done! You're now ready for the practical exam.");
            }
        } else {
            System.out.println("No lessons to complete.");
        }
    }

    public void viewLessons() {
        if (lessonRequests.isEmpty()) {
            System.out.println("No upcoming lessons.");
        } else {
            System.out.println("Upcoming Lessons: " + lessonRequests);
        }
    }

    public void submitReview(String review) {
        System.out.println("Review submitted: " + review);
        // In a real app, save it to a database or file
    }

    @Override
    public void displayUser() {
        super.displayUser();
        System.out.println("Vehicle Class      : " + vehicleClass);
        System.out.println("Selected Vehicle   : " + selectedVehicle);
        System.out.println("Lessons Completed  : " + lessonsCompleted);
        viewLessons();
    }
}

