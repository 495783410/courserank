package acad.db.courserank.dbis;

/**
 *
 * @author rishabh
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseComparePage {
    static Connection conn;
    
    public CourseComparePage(){
        try{
            conn = global.getConnection();
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found exception : " + ex);
        }        
    }
    
    public static void closeConnection(){
        try{
            conn.close();           
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
        }    
    }
    
    public static class courseInfo{
        public String courseId;
        public String courseName;
    }
    
    public static class offeringInfo{
        public long offeringId;
        public int year;
        public String semester;
    }
    
    public static class instructorInfo{
        public String instructorId;
        public String instructorName;
        public double instructorRating;
    }
    
    public static class comparisionInfo{
        public String courseId;
        public String courseName;
        public String courseDescription;
        public String courseDepartment;
        public int year;
        public String semester;
        public double credits;
        public double offeringRating;
        public double offeringWorkLoad;
        public String venue;
        public int strength;
        public int[] grades = new int[10];
        public ArrayList<instructorInfo> instructorList = new ArrayList<instructorInfo>();
    }
    
    public static ArrayList<String> getDeptList(){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select distinct department from course");

            ResultSet rset = pstmt.executeQuery();

            ArrayList<String> deptList = new ArrayList<String>();
            while (rset.next()){
                deptList.add(rset.getString("department"));
            }

            return deptList;
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }

    }
 
    public static ArrayList<courseInfo> getCourseList(String dept){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select course_id, name "
                    + "from course "
                    + "where department = ? "
                    + "order by course_id");

            pstmt.setString(1, dept);
            ResultSet rset = pstmt.executeQuery();

            ArrayList<courseInfo> courseList = new ArrayList<courseInfo>();
            while (rset.next()){
                courseInfo newCourse = new courseInfo();
                newCourse.courseId = rset.getString("course_id");
                newCourse.courseName = rset.getString("name");
                courseList.add(newCourse);
            }

            return courseList;
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }
    }

    public static ArrayList<offeringInfo> getOfferingList(String courseId){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select offering_id, year, semester "
                    + "from offering "
                    + "where course_id = ? "
                    + "order by year desc, semester desc");

            pstmt.setString(1, courseId);

            ResultSet rset = pstmt.executeQuery();

            ArrayList<offeringInfo> offeringList = new ArrayList<offeringInfo>();
            
            while(rset.next()){
                offeringInfo newElement = new offeringInfo();
                newElement.offeringId = rset.getLong("offering_id");
                newElement.year = rset.getInt("year");
                newElement.semester = rset.getString("semester");
                offeringList.add(newElement);
            }
           
            return offeringList;   
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }   
    }
    
    public static comparisionInfo getComparisionData(String courseId, long offeringId){
        try{
            comparisionInfo data = new comparisionInfo();

            PreparedStatement pstmt = conn.prepareStatement("select * from course where course_id = ? ");
            pstmt.setString(1, courseId);
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                data.courseId = rset.getString("course_id");
                data.courseName = rset.getString("name");
                data.courseDepartment = rset.getString("department");
                data.courseDescription = rset.getString("description");
            }
            
            pstmt = conn.prepareStatement("select * from offering where offering_id = ?");
            pstmt.setLong(1, offeringId);
            rset = pstmt.executeQuery();
            if(rset.next()){
                data.year = rset.getInt("year");
                data.semester = rset.getString("semester");
                data.credits = rset.getDouble("credits");
                data.offeringRating = rset.getDouble("rating");
                data.offeringWorkLoad = rset.getDouble("aww");
                data.venue = rset.getString("venue");
                data.strength = rset.getInt("strength");
                
                data.grades[0] = rset.getInt("num_ap");
                data.grades[1] = rset.getInt("num_aa");
                data.grades[2] = rset.getInt("num_ab");
                data.grades[3] = rset.getInt("num_bb");
                data.grades[4] = rset.getInt("num_bc");
                data.grades[5] = rset.getInt("num_cc");
                data.grades[6] = rset.getInt("num_cd");
                data.grades[7] = rset.getInt("num_dd");
                data.grades[8] = rset.getInt("num_fr");
                data.grades[9] = rset.getInt("num_xx");
            }
            
            pstmt = conn.prepareStatement("select instructor_id, name, instructor.rating "
                    + "from teaches join instructor using (instructor_id) "
                    + "where offering_id = ?");
            pstmt.setLong(1, offeringId);
            rset = pstmt.executeQuery();
            while(rset.next()){
                instructorInfo newInstr = new instructorInfo();
                newInstr.instructorId = rset.getString("instructor_id");
                newInstr.instructorName = rset.getString("name");
                newInstr.instructorRating = rset.getDouble("instructor.rating");
                data.instructorList.add(newInstr);
            }
            
            return data;   
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }           
    }

}