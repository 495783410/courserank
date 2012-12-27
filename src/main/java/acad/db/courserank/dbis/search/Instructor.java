package acad.db.courserank.dbis.search;

/**
 *
 * @author rishabh
 */

import java.sql.*;
import org.json.simple.*;

public class Instructor {
    public static JSONArray Query (Connection Con, String str, int start_index, 
                                int max_fetch)
        throws Exception
    {
        JSONArray entry_list = new JSONArray();
        if(Con != null)
        {
            PreparedStatement pstmt = Con.prepareStatement("select * "
                    + "from instructor where match(instructor_id,name,department)"
                    + " against (? in natural language mode)"); 
            pstmt.setString(1, str);
            ResultSet rs= pstmt.executeQuery();
            int count=0;
            while(rs.next() && count < max_fetch+start_index)
            {
                if(count>=start_index)
                {
                    JSONArray value_list = new JSONArray();
                    value_list.add(rs.getString("name"));
                    String description = "";
                    description += "Department:";
                    description += rs.getString("department");
                    description += "&nbsp&nbsp&nbspRating:";
                    description += rs.getFloat("rating");
                    value_list.add(description);
                    String url = "instructorProfile.jsp?id="+rs.getString(1);
                    value_list.add(url);
                    value_list.add("Instructor");
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
    
    public static int getResultCount(Connection Con,String str)
            throws Exception
    {
        if(Con != null)
        {
            PreparedStatement pstmt= Con.prepareStatement("select "
                    + "count(*) from instructor where match(instructor_id,name,"
                    + "department) against (? in natural language mode)"); 
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
};