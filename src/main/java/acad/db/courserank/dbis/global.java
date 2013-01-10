package acad.db.courserank.dbis;

import com.mysql.jdbc.Connection;
import java.sql.*;

/**
 *
 * @author rishabh
 */

public class global {
    public static String driver = "com.mysql.jdbc.Driver";
    //public static String url = "jdbc:mysql://127.9.63.129:3306/courserank";
    //public static String username = "admin";
    //public static String password ="vcUsYACtQmUE";
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
        //url = "https://courserank-alphanso.rhcloud.com/";
        url = "http://localhost:8084/courserank";
        return url;
    }
}    

