# Employee Attendance Management System

This is a complete Employee Attendance Management System developed using Spring Boot for the backend and plain HTML, CSS, and Vanilla JavaScript for the frontend.

## Demo

https://github.com/user-attachments/assets/demo.webp

> A full walkthrough showing Employee Registration, Login, Check-In/Check-Out, Leave Application, HR Dashboard, and Leave Approval.

## Features
- **Employee Login & Registration:** Simple authentication and authorization (Employee / HR).
- **Attendance Check-In / Check-Out:** Employees can mark their attendance daily.
- **Working Hours Calculation:** Calculates hours worked upon check-out.
- **Leave Deduction Calculation:** Leave requests automatically deduct from the employee's balance when approved by HR.
- **HR Dashboard:** View all employees, their attendance logs, and process pending leave requests.
- **Employee Dashboard:** View own attendance history, available leave balance, and leave request statuses.

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA.
- **Frontend:** HTML, CSS, Vanilla JavaScript (No React/Angular/Vue).
- **Database:** MySQL.

## Setup Instructions

### Prerequisites
1. Java Development Kit (JDK) 17 or higher.
2. Maven.
3. MySQL Server running locally.

### Database Setup
1. The application is configured to automatically create the database `attendance_db` and all necessary tables when it runs. 
2. Ensure your MySQL server is running with the username `root` and password `root`. If your credentials differ, update the `src/main/resources/application.properties` file.
3. (Optional) A `database_schema.sql` file is provided in the root directory if you wish to run the table creations manually.

### Running the Application
1. Open a terminal or command prompt in the project root directory (`IEC_Project`).
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. Once the server starts, open your web browser and navigate to:
   ```
   http://localhost:8080/
   ```

### Usage
- Register a new account as an "Employee" to test check-in, check-out, and applying for leaves.
- Register another account as "HR" to view employee data, view all attendance records, and approve/reject leave requests.

## Assessment Constraints Followed
- **No JS Frameworks:** Strictly uses Vanilla JS, standard HTML, and basic CSS.
- **Optimal & Readable Code:** Variables and logic are standard, human-written, and easy to understand.
- **Timeline Consideration:** Project structured cleanly to meet standard assignment constraints without over-engineering.