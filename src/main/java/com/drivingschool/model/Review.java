public class Review {
    private String username;
    private int rating;
    private String comment;

    // Constructor to create a new review
    public Review(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    // For displaying the review nicely in the UI
    public String toString() {
        return username + " (" + rating + "/5): " + comment;
    }

    // Converts the review to a format suitable for saving in a file
    public String toFileString() {
        return username + "|" + rating + "|" + comment;
    }

    // Converts a line from the file back into a Review object
    public static Review fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;
        try {
            int rating = Integer.parseInt(parts[1]);
            return new Review(parts[0], rating, parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Optional: Getters if needed in future
    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
