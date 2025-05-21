//package com.drivingschool.model;
//public class Payment {
   package com.drivingschool.model;

public class Payment {
    private String paymentId;       // Unique identifier for the payment
    private String bookingId;       // ID of the associated booking
    private String studentId;       // ID of the student making the payment
    private double amount;          // Payment amount
    private String cardLastFour;    // Last four digits of the card
    private long timestamp;         // Payment timestamp (epoch milliseconds)

    public Payment(String paymentId, String bookingId, String studentId, double amount, String cardLastFour, long timestamp) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.amount = amount;
        this.cardLastFour = cardLastFour;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert to text line for file storage
    public String toTextLine() {
        return String.join(",", paymentId, bookingId, studentId, String.valueOf(amount), cardLastFour, String.valueOf(timestamp));
    }

    // Parse from text line
    public static Payment fromTextLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid payment data format: " + line);
        }
        String paymentId = parts[0];
        String bookingId = parts[1];
        String studentId = parts[2];
        double amount;
        try {
            amount = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + parts[3]);
        }
        String cardLastFour = parts[4];
        long timestamp;
        try {
            timestamp = Long.parseLong(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid timestamp format: " + parts[5]);
        }
        return new Payment(paymentId, bookingId, studentId, amount, cardLastFour, timestamp);
    }
}
//public class Payment {
//}
