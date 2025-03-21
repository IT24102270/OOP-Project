package main.java.com.drivingschool.model;

public class User {
    private String username;
    private String userid;
    private String useraddress;
    private String phonenumber;
    private String useremail;
    private String password;
    private String role;

    public User(String username, String userid, String useraddress, String phonenumber, String useremail, String password, String role) {
        this.username = username;
        this.userid = userid;
        this.useraddress = useraddress;
        this.phonenumber = phonenumber;
        this.useremail = useremail;
        this.password = password;
        this.role = role;
    }

    // getters
    public String getUsername() {
        return username;
    }
    public String getUserid() {
        return userid;
    }
    public String getUseremail() {
        return useremail;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public String getRole() {
        return role;
    }

    //setters
    public void setUsername(String username) {
        this.username = username; // username allocation to be added , check with existing usernames
    }
    public void setUserid(String userid) {
        this.userid = userid;    // user id should be automatically generated
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
    public void setPassword(String password) {
        this.password = password; // password should be hashed and saved
    }
    public void setRole(String role) {
        this.role = role;
    }

    // login logout
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    public void logout() {
        System.out.println("User Logged out");
    }

    // other operations
    public void createuser() {
        System.out.println("Hi "+username);  // welcome msg should be added
    }
    public void displayuser() {
        
    }
    public void changePassword(String currentpassword, String newpassword) {
        if (password.equals(currentpassword)) {   // password hashes to be used
            this.password = newpassword;          // password hashes to be used
            System.out.println("Password changed successfully!");
        }
        else {
            System.out.println("Passwords do not match");
        }
    }
    public void changeUsername(String username) {
        this.username = username;
    }
    public void changephonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public void changeaddress(String address) {
        this.useraddress = address;
    }
    public void changeemail(String email) {
        this.useremail = email;
    }
    public void deleteUser() {
        System.out.println("User has been removed!");
    }

}
