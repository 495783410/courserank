package acad.db.courserank.dbis.search;

/**
 *
 * @author rishabh
 */

import java.sql.*;
import org.json.simple.*;

public class Course {
    public static JSONArray Query (Connection Con, String str, int start_index, int max_fetch) throws Exception
    {
        JSONArray entry_list = new JSONArray();
        if(Con != null)
        {
            PreparedStatement pstmt = Con.prepareStatement("select * "
                    + "from course where match(course_id,name,department,description)"
                    + " against (? in natural language mode)"); 
            pstmt.setString(1, str);
            ResultSet rs= pstmt.executeQuery();
            int count=0;
            while(rs.next() && count < max_fetch+start_index)
            {
                if(count>=start_index)
                {
                    JSONArray value_list = new JSONArray();
                    value_list.add(rs.getString("course_id")+" : "+rs.getString("name"));
                    String description = "";
                    float rating = getRating(Con,rs.getString("course_id"));
                    description += "Department:";
                    description += rs.getString("department");
                    description += "&nbsp&nbsp&nbspRating:";
                    description += rating;
                    description += "<br/>";
                    description += rs.getString("description");
                    value_list.add(description);
                    String url = "coursePage.jsp?courseId="+rs.getString(1);
                    value_list.add(url);
                    value_list.add("course");
                    entry_list.add(value_list);
                }
                count++;
            }
            return entry_list;
        }
        else {
            System.err.print("Connection to db failed");
            return entry_list;
        }
    }
    
    private static float getRating(Connection Con, String Course_id) throws Exception
    {
        PreparedStatement pstmt = Con.prepareStatement("select avg(rating) from offering where course_id = ?");
        pstmt.setString(1,Course_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            return rs.getFloat(1);
        }
        return 0;
    }
    
    
    public static int getResultCount(Connection Con,String str) throws Exception
    {
        if(Con != null)
        {
            PreparedStatement pstmt= Con.prepareStatement("select "
                    + "count(*) from course where match(course_id,name,"
                    + "department,description) against (? in natural language mode)"); 
            pstmt.setString(1, str);
            ResultSet rs = pstmt.executeQuery();
            int result = 0;
            if(rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        }
        else {
            System.err.print("Connection to db failed");
            return 0;
        }
    }
}