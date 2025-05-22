package com.drivingschool.model;

public class Instructor extends User {
    private int yearsOfExperience;

    public Instructor(String id, String name, String dob, String number, String address, String email, String password, int yearsOfExperience) {
        super(name, id, dob, number, address, password, email, "instructor");
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toTextLine() {
        // Escape commas in fields to prevent CSV issues
        String escapedName = name.replace(",", "\\,");
        String escapedAddress = address.replace(",", "\\,");
        return String.join(",", id, escapedName, dob, number, escapedAddress, email, password, role, String.valueOf(yearsOfExperience));
    }

    public static Instructor fromTextLine(String line) {
        String[] parts = line.split(",(?=([^\\\\]|\\\\.)*$)", -1); // Split on commas not preceded by a backslash
        if (parts.length < 8 || parts.length > 9) {
            throw new IllegalArgumentException("Invalid instructor data format: " + line);
        }
        String id = parts[0];
        String name = parts[1].replace("\\,", ","); // Unescape commas
        String dob = parts[2];
        String number = parts[3];
        String address = parts[4].replace("\\,", ","); // Unescape commas
        String email = parts[5];
        String password = parts[6];
        String role = parts[7];
        if (!"instructor".equals(role)) {
            throw new IllegalArgumentException("Invalid role for instructor: " + role);
        }
        // Handle backward compatibility: default to 0 if yearsOfExperience is missing
        int yearsOfExperience = parts.length == 9 ? parseYearsOfExperience(parts[8]) : 0;
        return new Instructor(id, name, dob, number, address, email, password, yearsOfExperience);
    }

    private static int parseYearsOfExperience(String value) {
        try {
            int years = Integer.parseInt(value);
            if (years < 0) {
                throw new IllegalArgumentException("Years of experience cannot be negative");
            }
            return years;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid years of experience: " + value);
        }
    }
}