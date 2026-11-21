
import java.sql.*;
import java.util.Arrays;

public class PreparedStatementMega {

    static void heading(String title) {
        System.out.println("\n========== " + title + " ==========");
    }

    public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            System.out.println("Database Connected Successfully");

            // SET 1: Create the practice table
            heading("SET 1: SETUP");

            String createSQL = "CREATE TABLE JDBC_PS_DEMO (" +
                    "STUDENT_ID NUMBER PRIMARY KEY, " +
                    "STUDENT_NAME VARCHAR2(50), " +
                    "COURSE VARCHAR2(50), " +
                    "MARKS NUMBER, " +
                    "BONUS NUMBER(10,2), " +
                    "JOIN_DATE DATE)";

            try (PreparedStatement ps = con.prepareStatement(createSQL)) {
                ps.execute();
                System.out.println("Practice Table Created");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) {
                    System.out.println("Practice Table Already Exists");
                } else {
                    throw e;
                }
            }

            // Clean previous demonstration rows
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM JDBC_PS_DEMO")) {
                ps.executeUpdate();
            }

            // All subsequent DML will be rolled back at the end.
            con.setAutoCommit(false);

            try {

                // SET 2: INSERT with placeholders
                heading("SET 2: INSERT");

                String insertSQL = "INSERT INTO JDBC_PS_DEMO " +
                        "(STUDENT_ID, STUDENT_NAME, COURSE, MARKS) " +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(insertSQL)) {

                    ps.setInt(1, 101);
                    ps.setString(2, "RAVI");
                    ps.setString(3, "JAVA");
                    ps.setInt(4, 95);

                    int rows = ps.executeUpdate();
                    System.out.println("Rows Inserted: " + rows);
                }

                // SET 3: SELECT with multiple placeholders
                heading("SET 3: SELECT");

                String selectSQL = "SELECT * FROM JDBC_PS_DEMO " +
                        "WHERE STUDENT_ID = ? AND STUDENT_NAME = ?";

                try (PreparedStatement ps = con.prepareStatement(selectSQL)) {

                    ps.setInt(1, 101);
                    ps.setString(2, "RAVI");

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            System.out.println(
                                    rs.getInt("STUDENT_ID") + " " +
                                            rs.getString("STUDENT_NAME") + " " +
                                            rs.getString("COURSE") + " " +
                                            rs.getInt("MARKS"));
                        }
                    }
                }

                // SET 4: UPDATE, DELETE, clearParameters
                heading("SET 4: UPDATE AND DELETE");

                String updateSQL = "UPDATE JDBC_PS_DEMO " +
                        "SET MARKS = ? WHERE STUDENT_ID = ?";

                try (PreparedStatement ps = con.prepareStatement(updateSQL)) {

                    ps.setInt(1, 98);
                    ps.setInt(2, 101);

                    System.out.println(
                            "Rows Updated: " + ps.executeUpdate());

                    ps.clearParameters();

                    ps.setInt(1, 99);
                    ps.setInt(2, 101);

                    System.out.println(
                            "Rows Updated Again: " + ps.executeUpdate());
                }

                // Insert temporary student for DELETE
                try (PreparedStatement ps = con.prepareStatement(insertSQL)) {

                    ps.setInt(1, 102);
                    ps.setString(2, "KIRAN");
                    ps.setString(3, "SQL");
                    ps.setInt(4, 80);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM JDBC_PS_DEMO WHERE STUDENT_ID = ?")) {

                    ps.setInt(1, 102);
                    System.out.println(
                            "Rows Deleted: " + ps.executeUpdate());
                }

                // SET 5: Batch processing
                heading("SET 5: BATCH PROCESSING");

                try (PreparedStatement ps = con.prepareStatement(insertSQL)) {

                    ps.setInt(1, 103);
                    ps.setString(2, "ARUN");
                    ps.setString(3, "JAVA");
                    ps.setInt(4, 90);
                    ps.addBatch();

                    ps.setInt(1, 104);
                    ps.setString(2, "SURESH");
                    ps.setString(3, "SQL");
                    ps.setInt(4, 85);
                    ps.addBatch();

                    int[] results = ps.executeBatch();
                    System.out.println(
                            "Batch Results: " + Arrays.toString(results));

                    // Queue a third row, then cancel it.
                    ps.setInt(1, 105);
                    ps.setString(2, "CANCELLED");
                    ps.setString(3, "JAVA");
                    ps.setInt(4, 70);
                    ps.addBatch();

                    ps.clearBatch();
                    System.out.println("Pending Batch Cleared");
                }

                // Verify that 105 was not inserted
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM JDBC_PS_DEMO WHERE STUDENT_ID = ?")) {

                    ps.setInt(1, 105);

                    try (ResultSet rs = ps.executeQuery()) {
                        System.out.println(
                                "Student 105 Exists: " + rs.next());
                    }
                }

                // SET 6: Parameter and ResultSet metadata
                heading("SET 6: METADATA");

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT STUDENT_ID, STUDENT_NAME " +
                                "FROM JDBC_PS_DEMO WHERE STUDENT_ID = ?")) {

                    ParameterMetaData pmd = ps.getParameterMetaData();

                    System.out.println(
                            "Parameter Count: " + pmd.getParameterCount());

                    ResultSetMetaData rsmd = ps.getMetaData();

                    if (rsmd != null) {
                        System.out.println(
                                "Column Count: " + rsmd.getColumnCount());

                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            System.out.println(
                                    "Column: " + rsmd.getColumnName(i));
                        }
                    }
                }

                // SET 7: Different parameter data types
                heading("SET 7: DATA TYPES");

                String typesSQL = "UPDATE JDBC_PS_DEMO " +
                        "SET BONUS = ?, JOIN_DATE = ?, COURSE = ? " +
                        "WHERE STUDENT_ID = ?";

                try (PreparedStatement ps = con.prepareStatement(typesSQL)) {

                    ps.setDouble(1, 1500.75);

                    ps.setDate(2,
                            Date.valueOf("2026-11-18"));

                    ps.setNull(3, Types.VARCHAR);

                    ps.setObject(4, 101);

                    System.out.println(
                            "Data Type Update: " + ps.executeUpdate());
                }

                // SET 8: execute() and executeLargeUpdate()
                heading("SET 8: EXECUTION METHODS");

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM JDBC_PS_DEMO WHERE STUDENT_ID = ?")) {

                    ps.setInt(1, 101);

                    boolean hasResultSet = ps.execute();

                    System.out.println(
                            "execute() Returned: " + hasResultSet);

                    if (hasResultSet) {
                        try (ResultSet rs = ps.getResultSet()) {
                            if (rs.next()) {
                                System.out.println(
                                        "Student Found: " +
                                                rs.getString("STUDENT_NAME"));
                            }
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE JDBC_PS_DEMO SET MARKS = ? " +
                                "WHERE STUDENT_ID = ?")) {

                    ps.setInt(1, 100);
                    ps.setInt(2, 101);

                    long rows = ps.executeLargeUpdate();

                    System.out.println(
                            "Large Update Count: " + rows);
                }

                heading("ALL SETS COMPLETED");

            } finally {
                // Preserve the practice table but undo demo data changes.
                con.rollback();
                con.setAutoCommit(true);
                System.out.println(
                        "Demonstration Data Changes Rolled Back");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database Error: " + e.getMessage());
        }
    }
}
