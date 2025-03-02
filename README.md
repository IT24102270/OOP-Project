# OOP-Project
OOP Project Group 22


## File Tree consept 
```
/DrivingInstructorSystem
│
├── web
│   ├── index.html
│   ├── Userlogin.html
│   ├── Userregister.html
│   ├── instructorlist.html
│   ├── review.html
│   ├── booking.html
│   └── style.css
│
└── src
    ├── InstructorManagementSystem.java
    ├── User.java
    ├── instructors.java
    ├── Review.java
    └── ForumPost.java

```
# Driving Instructor System

This project is the initial blueprint for a Driving Instructor System designed to manage various aspects of a driving school using file-based storage for CRUD operations. Rather than using a database, all data (users, instructors, bookings, and reviews) will be stored and managed through text files.

## Project Overview

The Driving Instructor System is aimed at providing a lightweight solution for managing driving lessons. The key functionalities include:
- **User Management:** Enabling users to register and log in, with user data stored in text files.
- **Instructor Management:** Displaying a list of driving instructors along with their experience, managed through file-based CRUD operations.
- **Lesson Booking:** Allowing users to book driving lessons.
- **Review Submission:** Letting users submit and view reviews for driving lessons and instructors.

## Project Structure

The project is organized into two main sections:

- **Web Folder:** Contains all frontend components, including HTML pages (home, login, registration, instructor list, booking, and review pages) and a shared CSS file for styling.
- **Src Folder:** Contains Java Servlet classes for handling server-side logic (such as user authentication, instructor listing, booking, and review submission) and Java Bean classes for data models (User, Instructor, Review, and ForumPost). All CRUD operations will be performed on files rather than a database.

## Future Plans

This is just the starting point. Future enhancements include:
- Expanding the CRUD operations to handle more complex interactions.
- Improving the user interface and overall experience.
- Adding new features like a forum for community discussions and advanced instructor management.

This blueprint sets the foundation for our team project and will be refined and expanded as development progresses.
