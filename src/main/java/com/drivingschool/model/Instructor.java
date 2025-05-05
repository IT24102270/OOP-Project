package main.java.com.drivingschool.model;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

// Instructor class extends User to inherit common attributes and methods
public class Instructor extends User {
    // File path for storing instructor data
    private static final String INSTRUCTOR_FILE = "data/instructors.txt";

    // Instructor-specific attributes with encapsulation
    private String instructorID;
    private String[] vehicleClasses; // Array of vehicle types the instructor can teach (e.g., Car, Motorcycle)
    private int experienceYear; // Years of experience for sorting

    // Constructor to initialize an Instructor, calling the User superclass constructor
    public Instructor(String userName, String userId, String userAddress, String phoneNumber,
                      String userEmail, String password, String role, String instructorID,
                      String[] vehicleClasses, int experienceYear) {
        super(userName, userId, userAddress, phoneNumber, userEmail, password, role);
        this.instructorID = instructorID;
        this.vehicleClasses = vehicleClasses;
        this.experienceYear = experienceYear;
    }

    // Getters and setters for encapsulation
    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public String[] getVehicleClasses() {
        return vehicleClasses;
    }

    public void setVehicleClasses(String[] vehicleClasses) {
        this.vehicleClasses = vehicleClasses;
    }

    public int getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(int experienceYear) {
        this.experienceYear = experienceYear;
    }

    // ---------------- CRUD Operations ----------------

    // Add a new instructor to instructors.txt
    public static boolean addInstructor(Instructor instructor) {
        // Format the instructor data as a CSV line
        String vehicleClassesStr = String.join("-", instructor.getVehicleClasses());
        String line = String.join(",", instructor.getInstructorID(), instructor.getUsername(),
                instructor.getUserEmail(), String.valueOf(instructor.getExperienceYear()),
                vehicleClassesStr);
        return appendToFile(INSTRUCTOR_FILE, line);
    }

    // Retrieve and sort all instructors by experience using bubble sort
    public static List<Instructor> getAllInstructorsSorted() {
        List<Instructor> instructors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INSTRUCTOR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 5) {
                    String[] vehicleClasses = parts[4].split("-");
                    Instructor instructor = new Instructor(parts[1], parts[0], "", "", parts[2], "",
                            "instructor", parts[0], vehicleClasses,
                            Integer.parseInt(parts[3]));
                    instructors.add(instructor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort instructors by experience using bubble sort
        bubbleSortByExperience(instructors);
        return instructors;
    }

    // Update the years of experience for an instructor
    public static boolean updateInstructorExperience(String instructorID, int newExperience) {
        List<Instructor> instructors = getAllInstructorsSorted();
        boolean found = false;
        for (Instructor instructor : instructors) {
            if (instructor.getInstructorID().equals(instructorID)) {
                instructor.setExperienceYear(newExperience);
                found = true;
                break;
            }
        }
        if (!found) return false;

        // Rewrite the file with updated data
        List<String> lines = new ArrayList<>();
        for (Instructor instructor : instructors) {
            String vehicleClassesStr = String.join("-", instructor.getVehicleClasses());
            String line = String.join(",", instructor.getInstructorID(), instructor.getUsername(),
                    instructor.getUserEmail(), String.valueOf(instructor.getExperienceYear()),
                    vehicleClassesStr);
            lines.add(line);
        }
        return overwriteFile(INSTRUCTOR_FILE, lines);
    }

    // Remove an instructor by ID
    public static boolean deleteInstructor(String instructorID) {
        return deleteLine(INSTRUCTOR_FILE, instructorID);
    }

    // ---------------- Sorting Logic ----------------

    // Bubble sort to sort instructors by experience (descending order)
    private static void bubbleSortByExperience(List<Instructor> instructors) {
        int n = instructors.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (instructors.get(j).getExperienceYear() < instructors.get(j + 1).getExperienceYear()) {
                    // Swap instructors
                    Instructor temp = instructors.get(j);
                    instructors.set(j, instructors.get(j + 1));
                    instructors.set(j + 1, temp);
                }
            }
        }
    }

    // ---------------- Queue Integration ----------------

    // Schedule a lesson by dequeuing from QueueManager (implementation assumed in QueueManager)
    public void scheduleLesson() {
        // Placeholder: Assumes QueueManager has a method to dequeue a lesson
        System.out.println("Instructor " + getUsername() + " scheduled a lesson.");
        // Actual integration would involve calling QueueManager.dequeueLesson() and assigning the lesson
    }

    // ---------------- Other Responsibilities ----------------

    // View reviews (placeholder; assumes Review class interaction)
    public void viewReviews() {
        System.out.println("Viewing reviews for instructor: " + getUsername());
        // Implementation would involve reading reviews.txt and filtering by instructorID
    }

    // Update profile (extends User functionality)
    public void updateProfile(String email, String[] vehicleClasses) {
        setUserEmail(email);
        setVehicleClasses(vehicleClasses);
        updateInstructorExperience(this.instructorID, this.experienceYear); // Update file
    }

    // View schedule (placeholder; assumes Lesson class interaction)
    public void viewSchedule() {
        System.out.println("Viewing schedule for instructor: " + getUsername());
        // Implementation would involve reading lessons.txt and filtering by instructorID
    }

    // ---------------- File Utilities (Adapted from Admin) ----------------

    // Append a new line to the file
    private static boolean appendToFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Update a line in the file (not used directly but included for completeness)
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
            return false;
        }
        return found && overwriteFile(filePath, lines);
    }

    // Delete a line from the file by ID
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
            return false;
        }
        return removed && overwriteFile(filePath, lines);
    }

    // Overwrite the file with updated lines
    private static boolean overwriteFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) writer.write(line + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}