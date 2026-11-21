# JDBC Student Management System

A console-based Student Management System developed using **Java, JDBC, and Oracle Database**. This project demonstrates database connectivity and CRUD operations using JDBC.

## Features

- **Add Student** — Insert a new student record.
- **View Students** — Display all student records.
- **Search Student** — Find a student using their ID.
- **Update Student** — Modify a student's marks.
- **Delete Student** — Remove a student record.
- **Menu-Driven Interface** — Interact with the application through a console menu.

## Technologies Used

- Java
- JDBC (Java Database Connectivity)
- Oracle Database
- Oracle JDBC Driver
- Git and GitHub

## Database Structure

The application uses a table named `JDBC_STUDENTS`.

| Column | Data Type | Constraint |
|---|---|---|
| STUDENT_ID | NUMBER(10) | Primary Key |
| STUDENT_NAME | VARCHAR2(50) | NOT NULL |
| EMAIL | VARCHAR2(100) | UNIQUE, NOT NULL |
| MARKS | NUMBER(3) | NOT NULL, CHECK (0–100) |
## Database Setup

This project uses Oracle Database to store student information.

### 1. Create the Student Table

Connect to Oracle Database and execute:

```sql
CREATE TABLE JDBC_STUDENTS (
    STUDENT_ID NUMBER(10) PRIMARY KEY,
    STUDENT_NAME VARCHAR2(50) NOT NULL,
    EMAIL VARCHAR2(100) UNIQUE NOT NULL,
    MARKS NUMBER(3) NOT NULL
        CHECK (MARKS BETWEEN 0 AND 100)
);
```

### 2. Configure Database Connection

Set the database credentials as environment variables instead of hardcoding them in Java.

**PowerShell (Windows):**

```powershell
$env:DB_USERNAME = "YOUR_ORACLE_USERNAME"
$env:DB_PASSWORD = Read-Host "Enter Oracle password"
```

The application connects to Oracle using:

```text
jdbc:oracle:thin:@//localhost:1521/FREE
```

Ensure that Oracle Database is running and that the configured user has permission to access the `JDBC_STUDENTS` table.

## How to Run the Project

### Prerequisites

- Java JDK 17 or later
- Oracle Database
- Oracle JDBC Driver (`ojdbc17.jar`)
- PowerShell or another terminal

### Execution Steps

1. Clone the repository:

```bash
git clone https://github.com/jerripothulajagadish/JDBC-Student-Management-System.git
```

2. Navigate to the project directory:

```bash
cd JDBC-Student-Management-System
```

3. Create a `lib` folder and place the compatible Oracle JDBC driver inside it.

4. Configure the Oracle database credentials using the environment variables shown above.

5. Run the application in PowerShell:

```powershell
java --class-path ".;lib/ojdbc17.jar" StudentManagementApp.java
```

## Sample Console Menu

```text
==== STUDENT MANAGEMENT SYSTEM ====
1. Add a Student
2. View Students
3. Search Student
4. Update Student
5. Delete Student
6. Exit

Enter your choice:
```

## Project Structure

```text
JDBC-Student-Management-System/
│
├── StudentManagementApp.java
├── MyFirstJDBCConnection.java
├── OracleConnectionDemo.java
├── PreparedStatementMega.java
├── .gitignore
└── README.md
```

The Oracle JDBC driver is stored locally in the `lib` folder and is excluded from Git tracking.

## Learning Outcomes

Through this project, I gained practical experience with:

- Establishing JDBC connections to Oracle Database
- Using `PreparedStatement` for parameterized SQL queries
- Performing CRUD operations
- Processing query results using `ResultSet`
- Handling SQL exceptions
- Managing database resources using try-with-resources
- Using Git and GitHub for version control
