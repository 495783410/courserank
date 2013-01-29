package acad.db.courserank.dbis.search;

/**
 *
 * @author rishabh
 */

import java.sql.*;
import org.json.simple.*;

public class Post {
    public static JSONArray Query (Connection Con, String str, int start_index, 
                                int max_fetch)
        throws Exception
    {
        JSONArray entry_list = new JSONArray();
        if(Con != null)
        {
            PreparedStatement pstmt = Con.prepareStatement("select * "
                    + "from post where match(content)"
                    + " against (? in natural language mode)"); 
            pstmt.setString(1, str);
            ResultSet rs= pstmt.executeQuery();
            int count=0;
            while(rs.next() && count < max_fetch+start_index)
            {
                if(count>=start_index)
                {
                    JSONArray value_list = new JSONArray();
                    String description = "";
                    description += rs.getString("description");
                    value_list.add(description.substring(0, 15));
                    value_list.add(description);
                    String url = "url"+rs.getString(1);
                    value_list.add("post.jsp?id="+rs.getInt("post_id"));
                    value_list.add("post");
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
                    + "count(*) from post where match(content)"
                    + " against (? in natural language mode)"); 
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
