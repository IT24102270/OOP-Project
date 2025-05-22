package com.drivingschool.model;

public class Admin extends User {

    public Admin(String id, String name, String dob, String number, String address, String email, String password) {
        super(name, id, dob, number, address, password, email, "admin");
    }

    @Override
    public String toTextLine() {
        // Escape commas in fields to prevent CSV issues
        String escapedName = name.replace(",", "\\,");
        String escapedAddress = address.replace(",", "\\,");
        return String.join(",", id, escapedName, dob, number, escapedAddress, email, password, role);
    }

    public static Admin fromTextLine(String line) {
        String[] parts = line.split(",(?=([^\\\\]|\\\\.)*$)", -1); // Split on commas not preceded by a backslash
        if (parts.length != 8) {
            throw new IllegalArgumentException("Invalid admin data format: " + line);
        }
        String id = parts[0];
        String name = parts[1].replace("\\,", ","); // Unescape commas
        String dob = parts[2];
        String number = parts[3];
        String address = parts[4].replace("\\,", ","); // Unescape commas
        String email = parts[5];
        String password = parts[6];
        String role = parts[7];
        if (!"admin".equals(role)) {
            throw new IllegalArgumentException("Invalid role for admin: " + role);
        }
        return new Admin(id, name, dob, number, address, email, password);
    }
}