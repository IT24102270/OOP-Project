package com.drivingschool.model;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    private String adminID;

    private static final String ADMIN_FILE = "data/admins.txt";
    private static final String USER_FILE = "data/users.txt";

    public Admin(String userName, String userId, String userAddress, String phoneNumber, String userEmail, String password, String role, String adminID) {
        super(userName, userId, userAddress, phoneNumber, userEmail, password, role);
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    // ---------------- Admin CRUD ----------------
    public static boolean addAdmin(Admin admin) {
        String line = String.join(",", admin.getAdminID(), admin.getUserid(), admin.getUsername(), admin.getUserAddress(), admin.getPhoneNumber(), admin.getUserEmail(), admin.getPassword());
        return appendToFile(ADMIN_FILE, line);
    }

    public static boolean updateAdmin(Admin updatedAdmin) {
        String line = String.join(",", updatedAdmin.getAdminID(), updatedAdmin.getUserid(), updatedAdmin.getUsername(), updatedAdmin.getUserAddress(), updatedAdmin.getPhoneNumber(), updatedAdmin.getUserEmail(), updatedAdmin.getPassword());
        return updateFileLine(ADMIN_FILE, updatedAdmin.getAdminID(), line);
    }

    public static boolean deleteAdmin(String adminID) {
        return deleteLine(ADMIN_FILE, adminID);
    }

    public static List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 7) {
                    admins.add(new Admin(parts[2], parts[1], parts[3], parts[4], parts[5], parts[6], "admin", parts[0]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admins;
    }

    // ---------------- User CRUD (managed by Admin) ----------------
    public static boolean addUser(User user) {
        String line = String.join(",", user.getUserid(), user.getUsername(), user.getUserAddress(), user.getPhoneNumber(), user.getUserEmail(), user.getPassword());
        return appendToFile(USER_FILE, line);
    }

    public static boolean updateUser(User user) {
        String line = String.join(",", user.getUserid(), user.getUsername(), user.getUserAddress(), user.getPhoneNumber(), user.getUserEmail(), user.getPassword());
        return updateFileLine(USER_FILE, user.getUserid(), line);
    }

    public static boolean deleteUser(String userID) {
        return deleteLine(USER_FILE, userID);
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 6) {
                    users.add(new User(parts[1], parts[0], parts[2], parts[3], parts[4], parts[5], "user"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // ---------------- File Utils ----------------
    private static boolean appendToFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line + "\n");
            return true;
        } catch (IOException e) {
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
            return false;
        }
        return removed && overwriteFile(filePath, lines);
    }

    private static boolean overwriteFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) writer.write(line + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getPassword() {
        return super.getPassword(); // simply return the hashed password already stored
    }
}
