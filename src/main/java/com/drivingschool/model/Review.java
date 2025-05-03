public class Review {
    private String username;
    private int rating;
    private String comment;

    public Review(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String toString() {
        return username + " (" + rating + "/5): " + comment;
    }

    public String toFileString() {
        return username + "|" + rating + "|" + comment;
    }

    public static Review fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;
        return new Review(parts[0], Integer.parseInt(parts[1]), parts[2]);
    }
}
import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ReviewApp {
    static Scanner sc = new Scanner(System.in);
    static List<Review> reviews = new ArrayList<>();
    static final String FILE_NAME = "reviews.txt";

    public static void main(String[] args) {
        loadReviewsFromFile();

        while (true) {
            System.out.println("\n=== User Reviews ===");
            System.out.println("1. Add Review");
            System.out.println("2. View All Reviews");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            if (choice == 1) {
                addReview();
            } else if (choice == 2) {
                viewReviews();
            } else if (choice == 3) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void addReview() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter rating (1-5): ");
        int rating = sc.nextInt(); sc.nextLine();
        System.out.print("Enter your comment: ");
        String comment = sc.nextLine();

        Review review = new Review(name, rating, comment);
        reviews.add(review);
        saveReviewToFile(review);
        System.out.println("Review added and saved!");
    }

    static void viewReviews() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews available.");
        } else {
            System.out.println("\n--- All Reviews ---");
            for (Review r : reviews) {
                System.out.println(r);
            }
        }
    }

    static void saveReviewToFile(Review review) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(review.toFileString());
        } catch (IOException e) {
            System.out.println("Error saving review: " + e.getMessage());
        }
    }

    static void loadReviewsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Review review = Review.fromFileString(line);
                if (review != null) {
                    reviews.add(review);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
        }
    }
}
