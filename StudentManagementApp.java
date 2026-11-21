import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class StudentManagementApp {

    public static void addStudent(Scanner sc) {

        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        String sql = "INSERT INTO JDBC_STUDENTS "
                + "(STUDENT_ID, STUDENT_NAME, EMAIL, MARKS) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DriverManager.getConnection(
                        url, username, password);

                PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int sid = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Student Name: ");
            String sname = sc.nextLine();

            System.out.print("Enter Email: ");
            String mail = sc.nextLine();

            System.out.print("Enter Marks: ");
            int marks = sc.nextInt();

            ps.setInt(1, sid);
            ps.setString(2, sname);
            ps.setString(3, mail);
            ps.setInt(4, marks);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student Added Successfully");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Insert Failed: " + e.getMessage());
        }
    }

    public void ViewStudents() {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String sql = "SELECT * FROM JDBC_STUDENTS";
        try (Connection con = DriverManager.getConnection(url, username, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("ID: " + rs.getInt("STUDENT_ID"));
                System.out.println("Name: " + rs.getString("STUDENT_NAME"));
                System.out.println("Email: " + rs.getString("EMAIL"));
                System.out.println("Marks: " + rs.getInt("MARKS"));
                System.out.println("--------------------------");
            }

            if (!found) {
                System.out.println("No Students Found");
            } else {
                System.out.println("All Student Details Displayed");
            }
        } catch (SQLException e) {
            System.out.println("Viewing Failed: " + e.getMessage());
        }
    }

    public void SearchStudent(Scanner sc) {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String sql = "SELECT * FROM JDBC_STUDENTS " + "WHERE STUDENT_ID=?";
        try (Connection con = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = con.prepareStatement(sql);) {
            System.out.println("Eneter Student Id: ");
            int sid = sc.nextInt();
            ps.setInt(1, sid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    System.out.println(
                            "Student ID: " + rs.getInt("STUDENT_ID"));

                    System.out.println(
                            "Name: " + rs.getString("STUDENT_NAME"));

                    System.out.println(
                            "Email: " + rs.getString("EMAIL"));

                    System.out.println(
                            "Marks: " + rs.getInt("MARKS"));

                } else {
                    System.out.println("Student Not Found");
                }
            }
        } catch (SQLException e) {
            System.out.println("Search Failed: " + e.getMessage());
        }
    }

    public void UpdateStudent(Scanner sc) {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String sql = "UPDATE JDBC_STUDENTS " + "SET MARKS=? " + "WHERE STUDENT_ID=?";
        try (Connection con = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Enter Student ID: ");
            int sid = sc.nextInt();
            ps.setInt(2, sid);
            System.out.print("Enter New Marks: ");
            int marks = sc.nextInt();
            ps.setInt(1, marks);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(rows + " Updated Successfully");
            } else {
                System.out.println("Student Not Found");
            }
        } catch (SQLException e) {
            System.out.println("Update Failed:" + e.getMessage());
        }
    }

    public void DeleteStudent(Scanner sc) {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String sql = "DELETE FROM JDBC_STUDENTS " + "WHERE STUDENT_ID=?";
        try (Connection con = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = con.prepareStatement(sql);) {
            int sid = sc.nextInt();
            ps.setInt(1, sid);
            int rows = ps.executeUpdate();
            if ((rows > 0)) {
                System.out.println("Student Deleted SuccessFully");
            } else {
                System.out.println("Student Not Found");
            }
        } catch (SQLException e) {
            System.out.println("Deletion Failed " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManagementApp s = new StudentManagementApp();

        int choice = 0;

        do {
            System.out.println("\n==== STUDENT MANAGEMENT SYSTEM ====");
            System.out.println("1. Add a Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    s.addStudent(sc);
                    break;

                case 2:
                    s.ViewStudents();
                    break;

                case 3:
                    s.SearchStudent(sc);
                    break;

                case 4:
                    s.UpdateStudent(sc);
                    break;

                case 5:
                    s.DeleteStudent(sc);
                    break;

                case 6:
                    System.out.println("Exiting Application...");
                    break;

                default:
                    System.out.println("Invalid Choice");
            }

        } while (choice != 6);

        sc.close();
    }
}
