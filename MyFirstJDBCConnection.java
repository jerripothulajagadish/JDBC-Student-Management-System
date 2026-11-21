import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyFirstJDBCConnection {
    public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database Connected.");
            System.out.println("username: " + username);
            try (Statement stmt = con.createStatement()) {

                /*
                 * int rows = stmt.executeUpdate(
                 * "INSERT INTO JDBC_STUDENTS " +
                 * "(STUDENT_ID, STUDENT_NAME, COURSE, MARKS) " +
                 * "VALUES (101, 'RAVI', 'JAVA', 90)");
                 * 
                 * System.out.println("Rows Inserted: " + rows);
                 * int rowsUpdated = stmt.executeUpdate("UPDATE JDBC_STUDENTS " +
                 * "SET MARKS = 90 " +
                 * "WHERE STUDENT_ID=101");
                 * System.out.println("Rows Updated: " + rowsUpdated);
                 * // System.out.println("Statement Created successfully");
                 * 
                 * 
                 * int rowsDeleted = stmt.executeUpdate("DELETE FROM JDBC_STUDENTS " +
                 * "WHERE STUDENT_ID=101");
                 * System.out.println("Row Deleted Successfully: " + rowsDeleted);
                 * 
                 * 
                 * boolean result = stmt.execute("SELECT * FROM JDBC_STUDENTs");
                 * System.out.println("Result: " + result);
                 * 
                 */
                /*
                 * boolean result = stmt.execute(
                 * "SELECT * FROM JDBC_STUDENTS");
                 * 
                 * System.out.println("execute() Result: " + result);
                 * 
                 * if (result) {
                 * try (ResultSet rs = stmt.getResultSet()) {
                 * while (rs.next()) {
                 * System.out.println(
                 * "Student Name: " + rs.getString("STUDENT_NAME"));
                 * }
                 * }
                 * }
                 * 
                 * boolean result = stmt.execute(
                 * "UPDATE JDBC_STUDENTS " +
                 * "SET MARKS = 95 " +
                 * "WHERE STUDENT_ID = 101");
                 * 
                 * int count = stmt.getUpdateCount();
                 * 
                 * System.out.println("execute() Result: " + result);
                 * System.out.println("Rows Updated: " + count);
                 */
                try (ResultSet rs = stmt.executeQuery("SELECT * FROM JDBC_STUDENTS")) {
                    if (rs.next()) {
                        System.out.println(
                                "Student ID: " + rs.getInt("STUDENT_ID") + "\tStudent Name: "
                                        + rs.getString("STUDENT_NAME")
                                        + "\tCourse: " + rs.getString("COURSE") + "\tMarks: " + rs.getInt("MARKS"));
                        // System.out.println("Student Name: " + rs.getString("STUDENT_NAME"));
                        // System.out.println("Course: " + rs.getString("COURSE"));
                        // System.out.println("Marks: " + rs.getInt("MARKS"));
                    } else
                        System.out.println("STudent Not Found");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
