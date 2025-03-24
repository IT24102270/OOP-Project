package main.java.com.drivingschool.model;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class User {
    private String userName;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String userEmail;
    private String password;
    private String role;

    public User(String userName, String userId, String userAddress, String phoneNumber, String userEmail, String password, String role) {
        this.userName = userName;
        this.userId = userId;
        this.userAddress = userAddress;
        this.phoneNumber = phoneNumber;
        this.userEmail = userEmail;
        this.password = hashPassword(password);
        this.role = role;
    }

    // getters
    public String getUsername() {
        return userName;
    }
    public String getUserid() {
        return userId;
    }
    public String getUseremail() {
        return userEmail;
    }
    public String getPhonenumber() {
        return phoneNumber;
    }
    public String getRole() {
        return role;
    }

    //setters
    public void setUsername(String userName) {
        this.userName = userName; // username allocation to be added , check with existing usernames
    }
    public void setUserid(String userId) {
        this.userId = userId;    // user id should be automatically generated
    }
    public void setPhonenumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setUseremail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setPassword(String password) {
        this.password = hashPassword(password); // password should be hashed and saved
    }
    public void setRole(String role) {
        this.role = role;
    }

    //Hashing method
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = md.digest(password.getBytes());

            //bytes >>> hexadecimal String
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

    // login logout
    public boolean login(String userName, String password) {
        return this.userName.equals(userName) && this.password.equals((hashPassword(password)));
    }
    public void logout() {
        System.out.println("User Logged out");
    }

    // other operations
    public void createUser() {
        System.out.println("Hi "+userName+" Your account has been successfully created.");  // welcome msg should be added
    }
    public void displayUser() {
        System.out.println("Username      : "+userName);
        System.out.println("User id       : "+userId);
        System.out.println("User address  : "+userAddress);
        System.out.println("Phone number  : "+phoneNumber);
        System.out.println("User email    : "+userEmail);

    }
    public void changePassword(String currentPassword, String newPassword) {
        if (password.equals(hashPassword(currentPassword))) {   // password hashes to be used
            this.password = hashPassword(newPassword);          // password hashes to be used
            System.out.println("Password changed successfully!");
        }
        else {
            System.out.println("Error: Incorrect current password!");
        }
    }
    public void changeUsername(String userName) {
        this.userName = userName;
    }
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void changeAddress(String address) {
        this.userAddress = address;
    }
    public void changeEmail(String email) {
        this.userEmail = email;
    }
    public void deleteUser() {
        System.out.println("User " +userName+ " has been removed!");
        this.userName = null;
        this.userId = null;
        this.userAddress = null;
        this.phoneNumber = null;
        this.userEmail = null;
        this.password = null;
        this.role = null;
    }

}
