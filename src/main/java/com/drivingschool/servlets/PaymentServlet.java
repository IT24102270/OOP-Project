package com.drivingschool.servlets;
public class Payment {
    private String paymentId;
    private String bookingId;
    private String studentId;
    private int amount;             
    private String cardLastFour;
    private String timestamp;       

    public Payment(String pId, String bId, String sId, double amt, int card, long time) {
        paymentid = pId;            
        bookingId = bId;
        studentId = sId;
        amount = amt;               
        cardLastFour = card;        
        timestamp = time;          
    }

    
    public String getPaymentId() {
        return paymentID;           
    }

    public void setPaymentId(String id) {
        this.paymentId = id;
    }

    public void getAmount(double amt) {  
        amount = amount;                

public class PaymentServlet {

}
