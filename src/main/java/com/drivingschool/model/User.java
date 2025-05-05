package main.java.com.drivingschool.model;

public class User {
<<<<<<< HEAD
=======
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
        if (userName != null) {
            this.userName = userName;
        }
        else {
            System.out.println("Username is empty");
        }
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPhoneNumber(String phoneNumber) {
        if ((phoneNumber != null) && (phoneNumber.length() == 10)) {
            this.phoneNumber = phoneNumber;
        }
        else {
            System.out.println("Invalid phone number");
        }
    }
    public void setUserEmail(String userEmail) {
        if (userEmail == null || (!userEmail.contains("@") && !userEmail.contains("."))) {
            System.out.println("Invalid email!");
        }
        else {
            this.userEmail = userEmail.toLowerCase();
        }
    }
    public void setPassword(String password) {
        this.password = hashPassword(password);
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

    // login logout
    public boolean login(String userIDorEmail, String password) {
        return (this.userId.equals(userIDorEmail) && this.password.equals((hashPassword(password)))) || (this.userEmail.equalsIgnoreCase(userIDorEmail) && this.password.equals((hashPassword(password))));   //used ignore case if user enters their email
    }
    public void logout() {
        System.out.println("User "+userName+" has logged out!");
    }

    // other operations
    public void createUser() {
        System.out.println("Hi "+userName+" Your account has been successfully created.");
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
        this.role = null;    // user will be removed from User.txt by filehandler.java
    }
>>>>>>> 012559b15706b333382c38271e004296512d7ca4
}
