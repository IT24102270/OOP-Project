package main.java.com.drivingschool.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends User {
    private String instructorID;
    private int experienceYears;
    private String vehicleType;

    private static final String INSTRUCTOR_FILE = "instructors.txt";

    public Instructor(String userName, String userId, String userAddress, String phoneNumber, String userEmail, String password, String role, String instructorID, int experienceYears, String vehicleType) {
        super(userName, userId, userAddress, phoneNumber, userEmail, password, role);
        this.instructorID = instructorID;
        this.experienceYears = experienceYears;
        this.vehicleType = vehicleType;
    }

    // Getters and Setters
    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        if (instructorID != null && !instructorID.trim().isEmpty()) {
            this.instructorID = instructorID;
        } else {
            System.out.println("Instructor ID cannot be empty!");
        }
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears >= 0) {
            this.experienceYears = experienceYears;
        } else {
            System.out.println("Experience years cannot be negative!");
        }
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        if (vehicleType != null && !vehicleType.trim().isEmpty()) {
            this.vehicleType = vehicleType;
        } else {
            System.out.println("Vehicle type cannot be empty!");
        }
    }

    // CRUD Operations
    public static boolean addInstructor(Instructor instructor) {
        for (Instructor existing : getAllInstructors()) {
            if (existing.getInstructorID().equals(instructor.getInstructorID())) {
                System.out.println("Instructor with ID " + instructor.getInstructorID() + " already exists!");
                return false;
            }
        }
        String line = String.join("|", instructor.getInstructorID(), instructor.getUserid(), instructor.getUsername(), instructor.getUserAddress(), instructor.getPhoneNumber(), instructor.getUserEmail(), instructor.getPassword(), instructor.getRole(), String.valueOf(instructor.getExperienceYears()), instructor.getVehicleType());        return appendToFile(INSTRUCTOR_FILE, line);
    }

    public static boolean updateInstructor(Instructor updated) {
        String line = String.join("|", updated.getInstructorID(), updated.getUserid(), updated.getUsername(), updated.getUserAddress(), updated.getPhoneNumber(), updated.getUserEmail(), updated.getPassword(), updated.getRole(), String.valueOf(updated.getExperienceYears()), updated.getVehicleType());        return updateFileLine(INSTRUCTOR_FILE, updated.getInstructorID(), line);
    }

    public static boolean deleteInstructor(String instructorID) {
        return deleteLine(INSTRUCTOR_FILE, instructorID);
    }

    public static List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INSTRUCTOR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 10) {
                    int experienceYears;
                    try {
                        experienceYears = Integer.parseInt(parts[8]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid experience years for Instructor ID: " + parts[0]);
                        continue;
                    }
                    Instructor inst = new Instructor(parts[2], parts[1], parts[3], parts[4], parts[5], parts[6], parts[7], parts[0], experienceYears, parts[9]);
                    instructors.add(inst);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading instructors file: " + e.getMessage());
        }
        return instructors;
    }

    public static void displayAllInstructors() {
        List<Instructor> instructors = getAllInstructors();
        if (instructors.isEmpty()) {
            System.out.println("No instructors available!");
            return;
        }
        instructors = sortByExperience(instructors);
        System.out.println("--- All Instructors (Sorted by Experience) ---");
        for (Instructor instructor : instructors) {
            instructor.displayUser();
            System.out.println("------------------------");
        }
    }

    // Bubble Sort by Experience
    public static List<Instructor> sortByExperience(List<Instructor> instructors) {
        // Bubble Sort Algorithm
        int n = instructors.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (instructors.get(j).getExperienceYears() < instructors.get(j + 1).getExperienceYears()) {
                    Collections.swap(instructors, j, j + 1);
                }
            }
        }
        return instructors;
    }

    // File Utils
    private static boolean appendToFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }

    private static boolean updateFileLine(String filePath, String id, String newLine) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String current;
            while ((current = reader.readLine()) != null) {
                if (current.startsWith(id + ",")) {
                    lines.add(newLine);
                    found = true;
                } else {
                    lines.add(current);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file for update: " + e.getMessage());
            return false;
        }
        return found && overwriteFile(filePath, lines);
    }

    private static boolean deleteLine(String filePath, String id) {
        List<String> lines = new ArrayList<>();
        boolean removed = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(id + ",")) {
                    lines.add(line);
                } else {
                    removed = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file for deletion: " + e.getMessage());
            return false;
        }
        return removed && overwriteFile(filePath, lines);
    }

    private static boolean overwriteFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error overwriting file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void displayUser() {
        super.displayUser();
        System.out.println("Instructor ID  : " + instructorID);
        System.out.println("Experience     : " + experienceYears + "Years");
        System.out.println("Vehicle Type   : " + vehicleType);
    }

    public void performAction(String action, String studentName, String date, String time) {
        switch (action.toLowerCase()) {
            case "schedule":
                scheduleLesson(studentName, date, time);
                break;
            case "view-_schedule":
                viewSchedule();
                break;
            case "view_reviews":
                viewReviews();
                break;
            default:
                System.out.println("Invalid action provided.");
        }
    }

    public void scheduleLesson(String studentName, String date, String time) {
        System.out.println("Lesson scheduled with " + studentName + " on " + date + " at " + time);
    }

    public void viewSchedule() {
        System.out.println("Displaying schedule for instructor: " + getUsername());
    }

    public void viewReviews() {
        System.out.println("Displaying review for instructor: " + getUsername());
    }

    public void updateProfile(String address, String phone, String email) {
        changeAddress(address);
        setPhoneNumber(phone);
        setUserEmail(email);
        System.out.println("Instructor profile updated.");
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }
}



