package acad.db.courserank.dbis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rishabh
 */
public class Authenticator {
    HttpServletRequest request;

    public Authenticator(HttpServletRequest req) {
        request = req;
    }
    
    public String getUser() {
        String username = (String) request.getSession().getAttribute("username");
        return username;
    }

    public boolean Login() throws ClassNotFoundException, SQLException {
        HttpSession ses;
        ses = request.getSession(false);
        if(ses == null || ses.getAttribute("username") == null) {
            System.out.println("New Session Created");
            //user username and password to validate if no password then redirect to url
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if(username!=null && password!=null && !username.equals("") && !password.equals("")) {
                PreparedStatement pstmt;
                Connection conn = global.getConnection();
                pstmt = (PreparedStatement) conn.prepareStatement("select * from student where student_id=? and password=MD5(?)");
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rset = pstmt.executeQuery();
                int count = 0;
                String uname = "";
                while(rset.next()) {
                    uname = rset.getString("student_id");
                    count++;
                }
                conn.close();
                if(count!=1) {
                    return false;
                } else {
                    ses = request.getSession(true);
                    ses.setAttribute("username", uname);
                    return true;
                }
            }
            else {
                return false;
            }
        } else {
            //System.out.println("Assigning true based on earlier one");
            return true;
        }
    }

    public void RegisterLogin(String username) {
        HttpSession ses;
        ses = request.getSession(true);
        ses.setAttribute("username", username);
    }
    
    public void Logout() {
        HttpSession ses = request.getSession(false);
        if(ses.getAttribute("username") != null) {
            ses.removeAttribute("username");
        }
        if(ses!=null) {
            ses.invalidate();
        }
    }

}
