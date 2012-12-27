package acad.db.courserank.dbis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author rishabh
 */
public class Authenticate extends HttpServlet {
  String appSecrete = "be220ac34b1609dc150b3a9a8aca7554";
    HttpServletResponse globalResponse;
    Connection conn = null;
    Authenticator authenticator;
    
    void registerUser(String name, String username, String email, String password, String department) throws IOException {
        try {
            PreparedStatement pstmt;
            conn = global.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement("insert into student(student_id, name, password, department) values(?, ?, MD5(?), ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, department);
            int retval = pstmt.executeUpdate();
            if(retval != 0) {
                authenticator.RegisterLogin(username);
                globalResponse.sendRedirect("index.jsp");
            } else {
                globalResponse.sendRedirect("login.jsp");
            }
            conn.close();
        } catch (Exception e) {
            globalResponse.sendRedirect("register.jsp?error=error");
        } finally {
            if(conn != null) {
                try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }
    
    void ParseAndRegister(String signed_request, PrintWriter out) throws ParseException, IOException {
        StringTokenizer st = new StringTokenizer(signed_request, ".");
        String encodedSig = st.nextToken();
        String payload = st.nextToken();
        while(st.hasMoreTokens()) {
            payload += "." + st.nextToken();
        }

        String encodedData;
        encodedData = Base64Coder.decodeString(payload);

        JSONParser parser = new JSONParser();
        JSONObject arrayObj = (JSONObject) parser.parse(encodedData);
        JSONObject registration = (JSONObject) arrayObj.get("registration");

        String username = (String) registration.get("username");
        String email = (String) registration.get("email");
        String name = (String) registration.get("name");
        String department = (String) registration.get("department");
        String password = (String) registration.get("password");
        out.println(username + " " + email + " " + name + " " + department + " " + password);
        registerUser(name, username, email, password, department);
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        authenticator = new Authenticator(request);
        globalResponse = response;
        try {
            if(request.getParameter("src")!=null && request.getParameter("src").equals("login")) {
                boolean flag;
                flag = authenticator.Login();
                if(flag) {
                    //redirect to home page/index.jsp
                    response.sendRedirect("index.jsp");
                } else {
                    //redirect to login.jsp with error
                    response.sendRedirect("login.jsp?error=error");
                }
            } else if(request.getParameter("src")!=null && request.getParameter("src").equals("logout")) {
                authenticator.Logout();
                response.sendRedirect("login.jsp");
            } else if(request.getParameter("signed_request") != null) {
                String signedRequest = request.getParameter("signed_request");
                ParseAndRegister(signedRequest, out);
            } else {
                response.sendRedirect("login.jsp");
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
