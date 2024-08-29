package practical;
/* Practical: Write a program to demonstrate the use of PreparedStatement and ResultSet interface with all its types */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_Demo2 {
    private final String USER = "root";
    private final String URL = "jdbc:mysql://localhost:3306/practical?autoReconnect=true&SSL=false";
    private final String PASSWD = "";

    void registerDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void createTbl() {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "CREATE TABLE IF NOT EXISTS stdRegistration (stdID INTEGER NOT NULL, stdName VARCHAR(255))";
            Statement stmt = con.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertData(int stdID, String stdName) {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "INSERT INTO stdRegistration (stdID, stdName) VALUES (?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, stdID);
                pstmt.setString(2, stdName);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void readData() {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "SELECT * FROM stdRegistration";
            try (Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int stdID = rs.getInt("stdID");
                    String stdName = rs.getString("stdName");
                    System.out.println("ID: " + stdID + ", Name: " + stdName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void readDataScrollSensitive() {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "SELECT * FROM stdRegistration";
            try (Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = stmt.executeQuery(query);
                if (rs.last()) {
                    System.out.println("Last Record -> ID: " + rs.getInt("stdID") + ", Name: " + rs.getString("stdName"));
                }
                if (rs.first()) {
                    System.out.println("First Record -> ID: " + rs.getInt("stdID") + ", Name: " + rs.getString("stdName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateData(int stdID, String newStdName) {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "UPDATE stdRegistration SET stdName = ? WHERE stdID = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, newStdName);
                pstmt.setInt(2, stdID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteData(int stdID) {
        this.registerDriver();
        try (Connection con = DriverManager.getConnection(this.URL, this.USER, this.PASSWD)) {
            String query = "DELETE FROM stdRegistration WHERE stdID = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, stdID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JDBC_Demo2 obj = new JDBC_Demo2();
        obj.createTbl();
        obj.insertData(1, "Dipa");
        obj.insertData(2, "Janhvi");
        obj.readData();
        obj.readDataScrollSensitive();
        obj.updateData(1, "Krishna");
        obj.readData();
        obj.deleteData(2);
        obj.readData();
    }
}

