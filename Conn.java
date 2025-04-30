package airlinemanagementsystem;

import java.sql.*;

public class Conn {
    private Connection c;  // Private connection variable
    private Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "root");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Public getter method to access connection
    public Connection getConnection() {
        return c;
    }

    // ✅ Public getter method to access statement (if needed)
    public Statement getStatement() {
        return s;
    }
}
