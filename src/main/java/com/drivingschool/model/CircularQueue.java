package com.drivingschool.model;

import java.util.ArrayList;
import java.util.List;

public class CircularQueue {
    private String[] queue;
    private int front;
    private int rear;
    private int size;
    private final int capacity;

    public CircularQueue() {
        this.capacity = 100;
        this.queue = new String[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    // Add a booking ID to the queue
    public boolean enqueue(String bookingId) {
        if (isFull()) {
            return false;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = bookingId;
        size++;
        return true;
    }

    // Remove and return the front booking ID
    public String dequeue() {
        if (isEmpty()) {
            return null;
        }
        String bookingId = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity;
        size--;
        return bookingId;
    }

    // Peek at the front booking ID without removing it
    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check if the queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Get current size of the queue
    public int size() {
        return size;
    }

    // Get all booking IDs in the queue
    public List<String> getAllBookingIds() {
        List<String> bookingIds = new ArrayList<>();
        if (isEmpty()) {
            return bookingIds;
        }
        int current = front;
        for (int i = 0; i < size; i++) {
            if (queue[current] != null) {
                bookingIds.add(queue[current]);
            }
            current = (current + 1) % capacity;
        }
        return bookingIds;
    }
}