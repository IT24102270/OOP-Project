package com.drivingschool.model;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class User {
    protected String userName;
    protected String userId;
    protected String userDOB;
    protected String userAddress;
    protected String phoneNumber;
    protected String userEmail;
    private String password;
    protected String role;

    private User() {  // making default private because of security concerns
        this.userName = null;
        this.userDOB = null;
        this.userAddress = null;
        this.phoneNumber = null;
        this.userEmail = null;
        this.password = null;
        this.role = null;
    }

    public User(String userName,String userDOB, String userAddress, String phoneNumber, String userEmail, String password, String role) {
        setUserName(userName);
        setUserDOB(userDOB);
        setUserAddress(userAddress);
        setPhoneNumber(phoneNumber);
        setUserEmail(userEmail);
        this.password = hashPassword(password);
        this.role = role;
    }

    // getters
    public String getUserName() {
        return userName;
    }
    public String getUserId() {
        return userId;
    }
    public String getUserDOB() {return userDOB;}
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getRole() {
        return role;
    }
    public String getPassword() {
        return password;
    }

    //setters
    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is empty");
        }
        else {
            this.userName = userName;
        }
    }
    public void setUserDOB(String userDOB) {
        if (userDOB == null || userDOB.trim().isEmpty()) {
            throw new IllegalArgumentException("UserDOB is empty");
        }
        else {
            this.userDOB = userDOB;
        }
    }
    public void setUserAddress(String userAddress) {
        if (userAddress == null || userAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("UserAddress is empty");
        }
        else {
            this.userAddress = userAddress;
        }
    }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            this.phoneNumber = phoneNumber;
        }
        else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }
    public void setUserEmail(String userEmail) {
        if (userEmail == null || !userEmail.contains("@") || !userEmail.contains(".")) {
            throw new IllegalArgumentException("Invalid email!");
        }
        else {
            this.userEmail = userEmail.toLowerCase();
        }
    }
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is empty");
        }
        else {
            this.password = hashPassword(password);
        }
    }
    public void setRole(String role) {
        this.role = role;
    }

    //Hashing method
    private String hashPassword(String password) {
        try {
            password = password.trim();                                       // removing spaces if there are
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found",e);
        }
    }
    public boolean verifyPassword(String password) {
        return this.password.equals(hashPassword(password));
    }
    public void changePassword(String currentPassword, String newPassword) {
        if (verifyPassword(currentPassword)) {   // password hashes to be used
            this.password = hashPassword(newPassword);          // password hashes to be used
        }
        else {
            throw new IllegalArgumentException("Error: Incorrect current password!");
        }
    }

    public void changeUsername(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is empty");
        }
        else {
            setUserName(userName);
        }
    }
    public void changePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is empty");
        }
        else {
            setPhoneNumber(phoneNumber);
        }
    }
    public void changeAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is empty");
        } else {
            this.userAddress = address;
        }
    }
    public void changeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is empty");
        }
        else {
            setUserEmail(email);
        }
    }

    // other operations
    public String displayUser() {
        return userName +userDOB+userAddress+phoneNumber+userEmail;
    }

    public void deleteUser() {
        this.userName = null;
        this.userAddress = null;
        this.phoneNumber = null;
        this.userEmail = null;
        this.password = null;
        this.role = null;
        // File handler will remove the user data
    }
}

