package main.java.com.drivingschool.model;

import java.util.ArrayList;

public class Review {
    private String reviewID;
    private String userID;
    private String comment;
    private int rating;

    public Review(String reviewID, String userID, String comment, int rating) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters
    public String getReviewID() { return reviewID; }
    public String getUserID() { return userID; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }

    // Add logic methods (you need to implement actual DB logic or storage)
    public static void addReview(Review review) { /* DB logic */ }
    public static void updateReview(Review review) { /* DB logic */ }
    public static void deleteReview(String reviewID) { /* DB logic */ }
    public static ArrayList<Review> getAllReviews() { return new ArrayList<>(); /* fetch from DB */ }
}
