import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnectionDemo {
    public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@//localhost:1521/FREE";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        try (Connection con = DriverManager.getConnection(
                url, username, password)) {

            System.out.println(
                    "Oracle Database Connected Successfully!");
            System.out.println(
                    "Connected User: " + con.getMetaData().getUserName());

            System.out.println(
                    "Database: " + con.getMetaData().getDatabaseProductName());

        } catch (SQLException e) {
            System.out.println("Database Connection Failed");
            System.out.println(e.getMessage());
        }
    }
}
