package acad.db.courserank.dbis;

import com.mysql.jdbc.Connection;
import java.sql.*;

/**
 *
 * @author rishabh
 */

public class global {
    public static String driver = "com.mysql.jdbc.Driver";
//    public static String url = "jdbc:mysql://127.5.92.1:3306/dbis";
//    public static String username = "admin";
//    public static String password ="fAVDSiFILmYF";
    public static String url = "jdbc:mysql://localhost:3306/dbis";
    public static String username = "root";
    public static String password ="rinku";

    public static Connection getConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(driver);
        Connection conn = (Connection) DriverManager.getConnection(url, username, password);
        return conn;
    }
     
    public static String getHost() {
        String url;
        //url = "http://dbis-alphanso.rhcloud.com/";
        url = "http://localhost:8080/courserank";
        return url;
    }
}    

