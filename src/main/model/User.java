package com.drivingschool.model;

/**
 * Abstract class representing a user in the Driving School System.
 * Serves as the base class for Student, Instructor, and Admin, providing common attributes and methods.
 */
public abstract class User {
    protected String id;        // Unique identifier for the user (e.g., UUID)
    protected String name;      // Full name of the user
    protected String dob;       // date of birth
    protected String number;    // phone number
    protected String address;   // address
    protected String email;     // Email address of the user
    protected String password;  // Password (plain text for simplicity, should be hashed in production)
    protected String role;      // Role of the user: "student", "instructor", or "admin"

    public User(String name, String id, String dob, String number, String address, String password, String email, String role) {
        this.name = name;
        this.id = id;
        this.dob = dob;
        this.number = number;
        this.address = address;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDob() {
        return dob;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Converts the user object to a string representation for file storage.
     * Subclasses must implement this method to define their specific format.
     *
     * @return a comma-separated string of user attributes
     */
    public abstract String toTextLine();
}