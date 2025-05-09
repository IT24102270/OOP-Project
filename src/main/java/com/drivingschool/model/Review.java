package com.drivingschool.model;

import java.util.ArrayList;
import java.util.List;

public class Review {
    private String reviewID;
    private String userID;
    private String comment;
    private int rating;

    private static final List<Review> reviews = new ArrayList<>();

    public Review(String reviewID, String userID, String comment, int rating) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.comment = comment;
        this.rating = rating;
    }

    public static void addReview(Review review) {
        reviews.add(review);
    }

    public static void updateReview(Review updatedReview) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getReviewID().equals(updatedReview.getReviewID())) {
                reviews.set(i, updatedReview);
                return;
            }
        }
    }

    public static void deleteReview(String reviewID) {
        reviews.removeIf(r -> r.getReviewID().equals(reviewID));
    }

    public static List<Review> getAllReviews() {
        return reviews;
    }

    public String getReviewID() {
        return reviewID;
    }

    public String getUserID() {
        return userID;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
