package acad.db.courserank.dbis;

/**
 *
 * @author rinku
 */
import java.sql.*;
import java.util.*;

public class Course_reg {
    public ArrayList<String> course_name = new ArrayList<String>();
    public ArrayList<String> offering_id = new ArrayList<String>();
    public ArrayList<String> course_id = new ArrayList<String> ();
    
    
    public Course_reg(String Student_id) throws Exception
    {
        Connection conn = global.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select C.name "
                + "name, T.offering_id, C.course_id from takes T, offering O, course C "
                + "where T.student_id = ? "
                + "and T.offering_id = O.offering_id "
                + "and O.course_id = C.course_id "
                + "order by O.year");
        pstmt.setString(1,Student_id);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
        {
            offering_id.add(rs.getString("offering_id"));
            course_id.add(rs.getString("course_id"));
            course_name.add(rs.getString("name"));
        }
        conn.close();
    }
    
    public ArrayList<String> result(){
        ArrayList<String> ret = new ArrayList<String>();
        for(int i=0;i<course_name.size();i++)
        {
            if(i==0) {
                ret.add("<a href=\"index.jsp?id="+offering_id.get(i)+"&course_id="+course_id.get(i)+"\"><li class=\"sb_selected\">"+course_name.get(i)+"</li></a>");
            }
            else {
                ret.add("<a href=\"index.jsp?id="+offering_id.get(i)+"&course_id="+course_id.get(i)+"\"><li>"+course_name.get(i)+"</li></a>");
            }
        }
        return ret;
    }
}
