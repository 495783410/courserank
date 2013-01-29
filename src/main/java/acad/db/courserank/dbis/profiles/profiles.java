package acad.db.courserank.dbis.profiles;

import acad.db.courserank.dbis.global;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;

/**
 *
 * @author rishabh
 */

public class profiles {
        
    // Student Profile
    public static String loadImage(String id, byte[] photo) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(photo));
            File dir = new File("images/student");
            dir.mkdirs();
            File f = new File("images/student/" + id + ".jpg");
            f.createNewFile();
            ImageIO.write(bufferedImage, "jpg", f);
            System.out.println(f.getAbsolutePath());
            return f.getAbsolutePath();
        }
        catch (IOException ioe) {
            System.out.println("IOException: " + ioe.toString());
            return "";
        }
    }

    public static class SPStudentInfo {
        public String student_id;
        public String name;
        public String department;
        public byte photo[];
        public int cpi2;
    }
    
    public static class SPCourseInfo {
        public String course_id;
        public int offering_id;
        public String name;
        public String department;
        public int credits;
        public int year;
        public String semester;
        public String grade;
    }
    
    public static class SPProjectInfo {
        public String name;
        public String description;
        public int offering_id;
        public String course_id;
        public String instructor_id;
        public String instructor_name;
        public float score;
        public int project_id;
    }
    
    public static class SPWorkInfo {
        public String name;
        public String description;
        public String period;
        public int length;
        public int stipend;
        public String location;
        public String category;
    }
    
    public static SPStudentInfo getSPStudentInfo(String s_id) throws Exception {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select name, department, photo, cpi from student where student_id = ?");
            pStmt.setString(1, s_id);
            
            ResultSet rs = pStmt.executeQuery();
            SPStudentInfo result = new SPStudentInfo();
            
            if (!rs.next()) {
                result.student_id = "";
                return result;
            }
            
            result.student_id = s_id;
            result.name = rs.getString(1);
            result.department = rs.getString(2);
            result.cpi2 = (int) Math.round(rs.getDouble(4)*100);
            java.sql.Blob bl = rs.getBlob(3);
            result.photo = (bl == null ? null : bl.getBytes(1, (int)bl.length()));
            
            pStmt.close();
            conn.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe.toString());
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle.toString());
        }
        return null;
    }
    
    public static ArrayList<SPCourseInfo> getSPCourseInfo(String s_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select course_id, name, department, credits, year, semester, offering_id from offering natural join course where offering_id in (select offering_id from takes where student_id = ?)");
            pStmt.setString(1, s_id);
            
            PreparedStatement pStmt2 = conn.prepareStatement("select grade from takes where student_id = ? and offering_id = ?");
            
            ArrayList<SPCourseInfo> result = new ArrayList<SPCourseInfo>();
            SPCourseInfo tuple = new SPCourseInfo();
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()) {
                tuple.course_id = rs.getString(1);
                tuple.name = rs.getString(2);
                tuple.department = rs.getString(3);
                tuple.credits = rs.getInt(4);
                tuple.year = rs.getInt(5);
                tuple.semester = rs.getString(6);
                tuple.offering_id = rs.getInt(7);
                
                pStmt2.setString(1, s_id);
                pStmt2.setInt(2, rs.getInt(7));
                ResultSet rs2 = pStmt2.executeQuery();
                
                rs2.next();
                tuple.grade = rs2.getString(1);
                if (rs2.wasNull()) {
                    tuple.grade = "";
                }
                
                result.add(tuple);
                tuple = new SPCourseInfo();
            }
            
            pStmt.close();
            pStmt2.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static ArrayList<SPProjectInfo> getSPProjectInfo(String s_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select project_id, B.name, description, course_id, instructor.name, offering_id, instructor_id "
                    + "from (select * from (select * from project where project_id in (select project_id from takes_project where student_id = ?)) A "
                    + "join offering using(offering_id)) B join instructor using(instructor_id)");
            pStmt.setString(1, s_id);
            
            PreparedStatement pStmt2 = conn.prepareStatement("select score from takes_project where student_id = ? and project_id = ?");
            
            ArrayList<SPProjectInfo> result = new ArrayList<SPProjectInfo>();
            SPProjectInfo tuple = new SPProjectInfo();
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()) {
                tuple.project_id = rs.getInt(1);
                tuple.name = rs.getString(2);
                tuple.description = rs.getString(3);
                tuple.course_id = rs.getString(4);
                tuple.instructor_name = rs.getString(5);
                tuple.offering_id = rs.getInt(6);
                tuple.instructor_id = rs.getString(7);
                
                pStmt2.setString(1, s_id);
                pStmt2.setInt(2, rs.getInt(1));
                ResultSet rs2 = pStmt2.executeQuery();
                rs2.next();
                tuple.score = rs2.getFloat(1);
                
                result.add(tuple);
                tuple = new SPProjectInfo();
            }
            
            pStmt.close();
            pStmt2.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    public static ArrayList<SPWorkInfo> getSPWorkInfo(String s_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select * from work_profile where work_id in (select work_id from takes_work where student_id = ?)");
            pStmt.setString(1, s_id);
            
            ArrayList<SPWorkInfo> result = new ArrayList<SPWorkInfo>();
            SPWorkInfo tuple = new SPWorkInfo();
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()) {
                tuple.name = rs.getString(2);
                tuple.description = rs.getString(3);
                tuple.period = rs.getString(4);
                tuple.length = rs.getInt(5);
                tuple.stipend = rs.getInt(6);
                tuple.location = rs.getString(7);
                tuple.category = rs.getString(8);
                result.add(tuple);
                tuple = new SPWorkInfo();
            }
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    // Instructor Profile Stuff
    
    public static class IPInstructorInfo {
        public String instructor_id;
        public String name;
        public String department;
        public int rating2;
    }
    
    public static class IPProjectInfo {
        public String name;
        public String description;
        public int offering_id;
        public String course_id;
        public ArrayList<String> student_ids;
        public ArrayList<String> student_names;
        public ArrayList<Integer> student_scores2;
    }
    
    public static class IPCourseInfo {
        public String course_id;
        public int offering_id;
        public String name;
        public String department;
        public int credits;
        public int year;
        public String semester;
        public int rating2;
    }
    
    public static IPInstructorInfo getIPInstructorInfo(String i_id) {
        try {
            
            Connection conn = global.getConnection();            
            PreparedStatement pStmt = conn.prepareStatement("select name, department, rating from instructor where instructor_id = ?");
            pStmt.setString(1, i_id);
            
            ResultSet rs = pStmt.executeQuery();
            IPInstructorInfo result = new IPInstructorInfo();
            
            if (!rs.next()) {
                result.instructor_id = "";
                pStmt.close();
                conn.close();
                return result;
            }
            
            result.instructor_id = i_id;
            result.name = rs.getString(1);
            result.department = rs.getString(2);
            result.rating2 = (int) Math.round(100*rs.getDouble(3));
            
            pStmt.close();
            conn.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("IPInstructorInfo CNFE: " + cnfe.toString());
        }
        catch (SQLException sqle) {
            System.err.println("IPInstructorInfo SQLE: " + sqle.toString());
        }
        return null;
    }
    
    public static ArrayList<IPCourseInfo> getIPCourseInfo(String i_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select course_id, name, department, credits, year, semester, offering_id  from offering natural join course where offering_id in (select offering_id from teaches where instructor_id = ?)");
            pStmt.setString(1, i_id);
            
            PreparedStatement pStmt2 = conn.prepareStatement("select rating from teaches where offering_id = ?");
            
            ArrayList<IPCourseInfo> result = new ArrayList<IPCourseInfo>();
            IPCourseInfo tuple = new IPCourseInfo();
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()) {
                tuple.course_id = rs.getString(1);
                tuple.name = rs.getString(2);
                tuple.department = rs.getString(3);
                tuple.credits = rs.getInt(4);
                tuple.year = rs.getInt(5);
                tuple.semester = rs.getString(6);
                tuple.offering_id = rs.getInt(7);
                
                pStmt2.setInt(1, tuple.offering_id);
                ResultSet rs2 = pStmt2.executeQuery();
                rs2.next();
                tuple.rating2 = (int) Math.round(100*rs2.getDouble(1));
                
                result.add(tuple);
                tuple = new IPCourseInfo();
            }
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("IPCourseInfo CNFE: " + cnfe.toString());
            return null;
        }
        catch (SQLException sqle) {
            System.err.println("IPCourseInfo SQLE: " + sqle.toString());
            return null;
        }
    }
    
    public static ArrayList<IPProjectInfo> getIPProjectInfo(String i_id) {
        
        try {
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select project_id, name, description, course_id, offering_id from "
                    + "project join offering using(offering_id) where project.instructor_id = ?");
            pStmt.setString(1, i_id);
            
            ArrayList<IPProjectInfo> result = new ArrayList<IPProjectInfo>();
            IPProjectInfo tuple = new IPProjectInfo();
            tuple.student_ids = new ArrayList<String>();
            tuple.student_names = new ArrayList<String>();
            tuple.student_scores2 = new ArrayList<Integer>();
            ResultSet rs = pStmt.executeQuery();
            
            PreparedStatement pStmt2 = conn.prepareStatement("select student_id, name, score from student natural join takes_project where student_id in "
                    + "(select student_id from takes_project where project_id = ?)");
            
            while(rs.next()) {
                tuple.name = rs.getString(2);
                tuple.description = rs.getString(3);
                tuple.course_id = rs.getString(4);
                tuple.offering_id = rs.getInt(5);
                
                pStmt2.setInt(1, rs.getInt(1));
                ResultSet rs2 = pStmt2.executeQuery();
                while (rs2.next()) {
                    tuple.student_ids.add(rs2.getString(1));
                    tuple.student_names.add(rs2.getString(2));
                    tuple.student_scores2.add((int)Math.round(100*rs2.getDouble(3)));
                }
                
                result.add(tuple);
                tuple = new IPProjectInfo();
            }
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            System.err.println("IPProjectInfo SQLE: " + sqle.toString());
            return null;
        }
    }

    /**
     * Feedback Submit. This function submits the feedback given by a student
     * for an offering and corresponding instructor.
     * It firsts enters a tuple into the feedback table, and then updates
     * the instr_rating values in student, instructor and offering relations.
     * @param s_id
     * @param i_id
     * @param o_id
     * @param cr
     * @param ir
     * @param dr
     * @param aww 
     */
    public static void submitFeedback(String s_id, String i_id, int o_id, int cr, int ir, int dr, float aww) {
        try {
            Connection conn = global.getConnection();
            
            // Insert feedback tuple in the feedback table
            PreparedStatement pStmt = conn.prepareStatement("insert into feedback values(?, ?, ?, ?, ?, ?, ?, ?)");
            pStmt.setString(1, s_id);
            pStmt.setInt(2, o_id);
            pStmt.setString(3, i_id);
            Timestamp t = new Timestamp(Calendar.getInstance().getTime().getTime());
            pStmt.setTimestamp(4, t);
            pStmt.setInt(5, cr);
            pStmt.setInt(6, ir);
            pStmt.setInt(7, dr);
            pStmt.setFloat(8, aww);
            
            pStmt.executeUpdate();
            
            pStmt.close();
            
            // Update mean and var fields of student
            
            pStmt = conn.prepareStatement("select avg(course_rating), avg(instructor_rating), avg(difficulty_rating), stddev(course_rating), stddev(instructor_rating), stddev(difficulty_rating) from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            ResultSet rs = pStmt.executeQuery();
            rs.next();
            float cr_mean = rs.getFloat(1);
            float ir_mean = rs.getFloat(2);
            float dr_mean = rs.getFloat(3);
            float cr_var = rs.getFloat(4);
            float ir_var = rs.getFloat(5);
            float dr_var = rs.getFloat(6);
            pStmt.close();
            
            pStmt = conn.prepareStatement("update student set cr_mean = ? and cr_var = ? and ir_mean = ? and ir_var = ? and dr_mean = ? and dr_var = ? where student_id = ?");
            pStmt.setFloat(1, cr_mean);
            pStmt.setFloat(2, cr_var);
            pStmt.setFloat(3, ir_mean);
            pStmt.setFloat(4, ir_var);
            pStmt.setFloat(5, dr_mean);
            pStmt.setFloat(6, dr_var);
            pStmt.setString(7, s_id);
            pStmt.executeUpdate();
            pStmt.close();
            
            
            // Now updating instr_rating for all offering-instructor pairs in teaches relation
            
            pStmt = conn.prepareStatement("select distinct offering_id, instructor_id from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            rs = pStmt.executeQuery();
            ArrayList<Integer> offs = new ArrayList<Integer>();
            ArrayList<String> instrs = new ArrayList<String>();
            
            while(rs.next()) {
                offs.add(rs.getInt(1));
                instrs.add(rs.getString(2));
            }
            
            // (offs, instrs) has all (o_id,i_id)s whose instr_rating is to be updated;
            
            pStmt.close();
            pStmt = conn.prepareStatement("select student_id, avg(instructor_rating), count(instructor_rating), avg(avg_workload), avg(course_rating), avg(difficulty_rating) from feedback where offering_id = ? and instructor_id = ? group by student_id");
            
            PreparedStatement pStmt2 = conn.prepareStatement("select ir_mean, ir_var, cr_mean, cr_var, dr_mean, dr_var from student where student_id = ?");
            PreparedStatement pStmt3 = conn.prepareStatement("update teaches set instructor_rating = ?, aww = ?, course_rating = ?, difficulty_rating = ? where offering_id = ? and instructor_id = ?");
            
            for (int i=0; i<offs.size(); i++) {
                pStmt.setInt(1, offs.get(i));
                pStmt.setString(2, instrs.get(i));
                rs = pStmt.executeQuery();
                // instr_rating is the instr_rating for this offering-instructor pair
                // It gets incremented for each student
                // Similarly for avgworkload
                // count adds the weight for each student (i.e. number of feedback tuples for that student)
                // count is used to take weighted average (weight = number of feedback tuples) over all students
                float instr_rating = 0.0f;
                float course_rating = 0.0f;
                float difficulty_rating = 0.0f;
                float avgworkload = 0.0f;
                int count = 0;
                // For each student
                while(rs.next()) {
                    pStmt2.setString(1, rs.getString(1));
                    ResultSet rs2 = pStmt2.executeQuery();
                    rs2.next();
                    // instr_rating += count(ir) * (avg(ir) - ir_mean)/ir_var;
                    instr_rating += rs.getInt(3) * ((rs.getFloat(2) - rs2.getFloat(1))/(rs2.getFloat(2) == 0 ? 1 : rs2.getFloat(2)));
                    course_rating += rs.getInt(3) * ((rs.getFloat(5) - rs2.getFloat(3))/(rs2.getFloat(4) == 0 ? 1 : rs2.getFloat(4)));
                    difficulty_rating += rs.getInt(3) * ((rs.getFloat(6) - rs2.getFloat(5))/(rs2.getFloat(6) == 0 ? 1 : rs2.getFloat(6)));
                    avgworkload += rs.getInt(3) * rs.getFloat(4);
                    count += rs.getInt(3);
                }
                
                // Final instr_rating and update of offering relation
                instr_rating /= (count == 0 ? 1 : count);
                course_rating /= (count == 0 ? 1 : count);
                difficulty_rating /= (count == 0 ? 1 : count);
                avgworkload /= count;
                pStmt3.setFloat(1, instr_rating);
                pStmt3.setFloat(2, avgworkload);
                pStmt3.setFloat(3, course_rating);
                pStmt3.setFloat(4, difficulty_rating);
                pStmt3.setInt(5, offs.get(i));
                pStmt3.setString(6, instrs.get(i));
                pStmt3.executeUpdate();
            }
            
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            conn.close();
        }
        catch(SQLException sqle) {
            System.err.println("SQLE: " + sqle.toString());
        }
        catch(ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe.toString());
        }
        
        System.out.println("Feedback: Student = " + s_id + " Instructor = " + i_id + " Offering = " + o_id);
    }
    /*
    public static void submitFeedback(String s_id, String i_id, int o_id, int cr, int ir, int dr, float aww) {
        try {
            Class.forName (driver);
            Connection conn = DriverManager.getConnection (url, username, password);
            
            // Insert feedback tuple in the feedback table
            PreparedStatement pStmt = conn.prepareStatement("insert into feedback values(?, ?, ?, ?, ?, ?, ?, ?)");
            pStmt.setString(1, s_id);
            pStmt.setInt(2, o_id);
            pStmt.setString(3, i_id);
            Timestamp t = new Timestamp(Calendar.getInstance().getTime().getTime());
            pStmt.setTimestamp(4, t);
            pStmt.setInt(5, cr);
            pStmt.setInt(6, ir);
            pStmt.setInt(7, dr);
            pStmt.setFloat(8, aww);
            
            pStmt.executeUpdate();
            
            pStmt.close();
            
            // Update mean and var fields of student
            
            pStmt = conn.prepareStatement("select avg(course_rating), avg(instructor_rating), avg(difficulty_rating), stddev(course_rating), stddev(instructor_rating), stddev(difficulty_rating) from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            ResultSet rs = pStmt.executeQuery();
            rs.next();
            float cr_mean = rs.getFloat(1);
            float ir_mean = rs.getFloat(2);
            float dr_mean = rs.getFloat(3);
            float cr_var = rs.getFloat(4);
            float ir_var = rs.getFloat(5);
            float dr_var = rs.getFloat(6);
            pStmt.close();
            
            pStmt = conn.prepareStatement("update student set cr_mean = ? and cr_var = ? and ir_mean = ? and ir_var = ? and dr_mean = ? and dr_var = ? where student_id = ?");
            pStmt.setFloat(1, cr_mean);
            pStmt.setFloat(2, cr_var);
            pStmt.setFloat(3, ir_mean);
            pStmt.setFloat(4, ir_var);
            pStmt.setFloat(5, dr_mean);
            pStmt.setFloat(6, dr_var);
            pStmt.setString(7, s_id);
            pStmt.executeUpdate();
            pStmt.close();
            
            
            // Updating Instructor Ratings for all instructors
            
            pStmt = conn.prepareStatement("select distinct instructor_id from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            rs = pStmt.executeQuery();
            ArrayList<String> instructors = new ArrayList<String>();
            
            while(rs.next()) {
                instructors.add(rs.getString(1));
            }
            
            // instructors has all i_ids whose instr_rating is to be updated;
            
            pStmt.close();
            pStmt = conn.prepareStatement("select student_id, avg(instructor_rating), count(instructor_rating) from feedback where instructor_id = ? group by student_id");
            
            PreparedStatement pStmt2 = conn.prepareStatement("select ir_mean, ir_var from student where student_id = ?");
            PreparedStatement pStmt3 = conn.prepareStatement("update instructor set instr_rating = ? where instructor_id = ?");
            
            for (String instr: instructors) {
                pStmt.setString(1, instr);
                rs = pStmt.executeQuery();
                // instr_rating is the instr_rating for this instructor
                // It gets incremented for each student
                // count adds the weight for each student (i.e. number of feedback tuples for that student)
                // count is used to take weighted average (weight = number of feedback tuples) over all students
                float instr_rating = 0.0f;
                int count = 0;
                // For each student
                while(rs.next()) {
                    pStmt2.setString(1, rs.getString(1));
                    ResultSet rs2 = pStmt2.executeQuery();
                    rs2.next();
                    // instr_rating += count(ir) * (avg(ir) - ir_mean)/ir_var;
                    instr_rating += rs.getInt(3) * ((rs.getFloat(2) - rs2.getFloat(1))/(rs2.getFloat(2) == 0 ? 1 : rs2.getFloat(2)));
                    count += rs.getInt(3);
                }
                
                // Final instr_rating and update of instructor relation
                instr_rating /= (count == 0 ? 1 : count);
                pStmt3.setFloat(1, instr_rating);
                pStmt3.setString(2, instr);
                pStmt3.executeUpdate();
            }
            
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            
            // Now updating instr_rating and aww for all offerings
            
            pStmt = conn.prepareStatement("select distinct offering_id from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            rs = pStmt.executeQuery();
            ArrayList<Integer> offerings = new ArrayList<Integer>();
            
            while(rs.next()) {
                offerings.add(rs.getInt(1));
            }
            
            // offerings has all o_ids whose instr_rating is to be updated;
            
            pStmt.close();
            pStmt = conn.prepareStatement("select student_id, avg(instructor_rating), count(instructor_rating), avg(avg_workload) from feedback where offering_id = ? group by student_id");
            
            pStmt2 = conn.prepareStatement("select ir_mean, ir_var from student where student_id = ?");
            pStmt3 = conn.prepareStatement("update offering set instr_rating = ?, aww = ? where offering_id = ?");
            
            for (int offering: offerings) {
                pStmt.setInt(1, offering);
                rs = pStmt.executeQuery();
                // instr_rating is the instr_rating for this offering
                // It gets incremented for each student
                // Similarly for avgworkload
                // count adds the weight for each student (i.e. number of feedback tuples for that student)
                // count is used to take weighted average (weight = number of feedback tuples) over all students
                float instr_rating = 0.0f;
                float avgworkload = 0.0f;
                int count = 0;
                // For each student
                while(rs.next()) {
                    pStmt2.setString(1, rs.getString(1));
                    ResultSet rs2 = pStmt2.executeQuery();
                    rs2.next();
                    // instr_rating += count(ir) * (avg(ir) - ir_mean)/ir_var;
                    instr_rating += rs.getInt(3) * ((rs.getFloat(2) - rs2.getFloat(1))/(rs2.getFloat(2) == 0 ? 1 : rs2.getFloat(2)));
                    avgworkload += rs.getInt(3) * rs.getFloat(4);
                    count += rs.getInt(3);
                }
                
                // Final instr_rating and update of offering relation
                instr_rating /= (count == 0 ? 1 : count);
                avgworkload /= (count == 0 ? 1 : count);
                pStmt3.setFloat(1, instr_rating);
                pStmt3.setFloat(2, avgworkload);
                pStmt3.setInt(3, offering);
                pStmt3.executeUpdate();
            }
            
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            
            // Now updating instr_rating for all offering-instructor pairs in teaches relation
            
            pStmt = conn.prepareStatement("select distinct offering_id, instructor_id from feedback where student_id = ?");
            pStmt.setString(1, s_id);
            
            rs = pStmt.executeQuery();
            ArrayList<Integer> offs = new ArrayList<Integer>();
            ArrayList<String> instrs = new ArrayList<String>();
            
            while(rs.next()) {
                offs.add(rs.getInt(1));
                instrs.add(rs.getString(2));
            }
            
            // (offs, instrs) has all (o_id,i_id)s whose instr_rating is to be updated;
            
            pStmt.close();
            pStmt = conn.prepareStatement("select student_id, avg(instructor_rating), count(instructor_rating), avg(avg_workload) from feedback where offering_id = ? and instructor_id = ? group by student_id");
            
            pStmt2 = conn.prepareStatement("select ir_mean, ir_var from student where student_id = ?");
            pStmt3 = conn.prepareStatement("update teaches set instr_rating = ?, aww = ? where offering_id = ? and instructor_id = ?");
            
            for (int i=0; i<offs.size(); i++) {
                pStmt.setInt(1, offs.get(i));
                pStmt.setString(2, instrs.get(i));
                rs = pStmt.executeQuery();
                // instr_rating is the instr_rating for this offering-instructor pair
                // It gets incremented for each student
                // Similarly for avgworkload
                // count adds the weight for each student (i.e. number of feedback tuples for that student)
                // count is used to take weighted average (weight = number of feedback tuples) over all students
                float instr_rating = 0.0f;
                float avgworkload = 0.0f;
                int count = 0;
                // For each student
                while(rs.next()) {
                    pStmt2.setString(1, rs.getString(1));
                    ResultSet rs2 = pStmt2.executeQuery();
                    rs2.next();
                    // instr_rating += count(ir) * (avg(ir) - ir_mean)/ir_var;
                    instr_rating += rs.getInt(3) * ((rs.getFloat(2) - rs2.getFloat(1))/(rs2.getFloat(2) == 0 ? 1 : rs2.getFloat(2)));
                    avgworkload += rs.getInt(3) * rs.getFloat(4);
                    count += rs.getInt(3);
                }
                
                // Final instr_rating and update of offering relation
                instr_rating /= (count == 0 ? 1 : count);
                avgworkload /= count;
                pStmt3.setFloat(1, instr_rating);
                pStmt3.setFloat(2, avgworkload);
                pStmt3.setInt(3, offs.get(i));
                pStmt3.setString(4, instrs.get(i));
                pStmt3.executeUpdate();
            }
            
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            conn.close();
        }
        catch(SQLException sqle) {
            System.err.println("SQLE: " + sqle.toString());
        }
        catch(ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe.toString());
        }
        
        System.out.println("Feedback: Student = " + s_id + " Instructor = " + i_id + " Offering = " + o_id);
    }
    */
    /**
     * Checks if the given (s_id, i_id, o_id) triplet is valid, or 
     * if the given student has already submitted feedback for the
     * given instructor and offering.
     * Returns true if the Feedback is Disallowed
     * @param s_id
     * @param i_id
     * @param o_id
     * @return 
     */
    
    public static boolean checkFeedback(String s_id, String i_id, int o_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select student_id from takes where offering_id = ? and student_id = ?");
            pStmt.setInt(1, o_id);
            pStmt.setString(2, s_id);
            
            ResultSet rs = pStmt.executeQuery();
            if (!rs.next()) {
                pStmt.close();
                return true;
            }
            pStmt.close();
            
            pStmt = conn.prepareStatement("select instructor_id from teaches where offering_id = ? and instructor_id = ?");
            pStmt.setInt(1, o_id);
            pStmt.setString(2, i_id);
            rs = pStmt.executeQuery();
            
            if (!rs.next()) {
                pStmt.close();
                return true;
            }
            pStmt.close();
            
            pStmt = conn.prepareStatement("select count(*) from feedback where student_id = ? and instructor_id = ? and offering_id = ?");
            pStmt.setString(1, s_id);
            pStmt.setString(2, i_id);
            pStmt.setInt(3, o_id);
            
            rs = pStmt.executeQuery();
            
            if(rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    pStmt.close();
                    return false;
                }
                pStmt.close();
                return true;
            }
            else {
                pStmt.close();
                return false;
            }
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe.toString());
            return true;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle.toString());
            return true;
        }
    }
    
    public static class FBCourseInfo {
        public String course_id;
        public int offering_id;
        public String name;
        public String department;
        public int credits;
        public int year;
        public String semester;
    }
    
    public static class FBInstructorInfo {
        public String instructor_id;
        public String name;
        public String department;
    }
    
    public static FBCourseInfo getFBCourseInfo(int o_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select course_id, name, department, credits, year, semester from course natural join offering where offering_id = ?");
            pStmt.setInt(1, o_id);
            
            ResultSet rs = pStmt.executeQuery();
            FBCourseInfo result = new FBCourseInfo();
            
            if (!rs.next()) {
                result.offering_id = -1;
                pStmt.close();
                conn.close();
                return result;
            }
            
            result.course_id = rs.getString(1);
            result.offering_id = o_id;
            result.name = rs.getString(2);
            result.department = rs.getString(3);
            result.credits = rs.getInt(4);
            result.year = rs.getInt(5);
            result.semester = rs.getString(6);
            
            pStmt.close();
            conn.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    public static FBInstructorInfo getFBInstructorInfo(String i_id) {
        try {
            
            Connection conn = global.getConnection();
            
            PreparedStatement pStmt = conn.prepareStatement("select name, department from instructor where instructor_id = ?");
            pStmt.setString(1, i_id);
            
            ResultSet rs = pStmt.executeQuery();
            FBInstructorInfo result = new FBInstructorInfo();
            
            if (!rs.next()) {
                result.instructor_id = "";
                pStmt.close();
                conn.close();
                return result;
            }
            
            result.instructor_id = i_id;
            result.name = rs.getString(1);
            result.department = rs.getString(2);
            
            pStmt.close();
            conn.close();
            
            return result;
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    public static boolean submitBasicToDB(String s_id, String name, float cpi) {
        try {
            
            Connection conn = global.getConnection();
            
            //TODO: Check if s_id == current logged-in user
            
            PreparedStatement pStmt = conn.prepareStatement("update student set name = ?, cpi = ? where student_id = ?");
            pStmt.setString(1, name);
            pStmt.setFloat(2, cpi);
            pStmt.setString(3, s_id);
            pStmt.executeUpdate();
            
            pStmt.close();
            conn.close();
            
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return false;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return false;
        }
    }
    
    public static boolean submitPasswordToDB(String s_id, String curPass, String newPass) {
        try {
            
            Connection conn = global.getConnection();
            
            //TODO: Check if s_id == current logged-in user
            
            PreparedStatement pStmt = conn.prepareStatement("select student_id from student where student_id = ? and password = MD5(?)");
            pStmt.setString(1, s_id);
            pStmt.setString(2, curPass);
            ResultSet rs = pStmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("Password Mismatch");
                pStmt.close();
                conn.close();
                return false;
            }
                        
            pStmt.close();
            pStmt = conn.prepareStatement("update student set password = MD5(?) where student_id = ?");
            pStmt.setString(1, newPass);
            pStmt.setString(2, s_id);
            pStmt.executeUpdate();
            
            pStmt.close();
            conn.close();
            
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return false;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return false;
        }
    }
    
    public static boolean submitGradesToDB(String s_id, ArrayList<Integer> offs, ArrayList<String> grades) {
        try {
            
            Connection conn = global.getConnection();
            
            //TODO: Check if s_id == current logged-in user
            
            PreparedStatement pStmt = conn.prepareStatement("update takes set grade = ? where student_id = ? and offering_id = ?");
            pStmt.setString(2, s_id);
            for (int i=0; i<offs.size(); i++) {
                pStmt.setInt(3, offs.get(i));
                if ("".equals(grades.get(i))) {
                    pStmt.setNull(1, java.sql.Types.VARCHAR);
                }
                else {
                    pStmt.setString(1, grades.get(i));
                }
                pStmt.executeUpdate();
            }
            
            pStmt.close();
            conn.close();
            
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return false;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return false;
        }
    }
    
    public static boolean submitProjectsToDB(String s_id, ArrayList<Integer> projects, ArrayList<Float> scores) {
        try {
            Connection conn = global.getConnection();
            
            //TODO: Check if s_id == current logged-in user
            
            PreparedStatement pStmt = conn.prepareStatement("update takes_project set score = ? where student_id = ? and project_id = ?");
            pStmt.setString(2, s_id);
            for (int i=0; i<projects.size(); i++) {
                pStmt.setInt(3, projects.get(i));
                pStmt.setFloat(1, scores.get(i));
                pStmt.executeUpdate();
            }
            
            pStmt.close();
            conn.close();
            
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return false;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return false;
        }
    }
    
    public static boolean addToTakes(String s_id, int o_id, String grade) {
        try {
            Connection conn = global.getConnection();
            
            //TODO: Check if s_id == current logged-in user
            
            PreparedStatement pStmt = conn.prepareStatement("select * from takes where student_id = ? and offering_id = ?");
            pStmt.setString(1, s_id);
            pStmt.setInt(2, o_id);
            ResultSet rs = pStmt.executeQuery();
            
            if (rs.next()) {
                pStmt.close();
                pStmt = conn.prepareStatement("delete from takes where student_id = ? and offering_id = ?");
                pStmt.setString(1, s_id);
                pStmt.setInt(2, o_id);
                pStmt.executeUpdate();
                pStmt.close();
                conn.close();
                return true;
            }
            
            pStmt.close();
            pStmt = conn.prepareStatement("insert into takes values (?, ?, ?)");
            pStmt.setString(1, s_id);
            pStmt.setInt(2, o_id);
            if ("".equals(grade)) {
                pStmt.setNull(3, Types.VARCHAR);
            }
            else {
                pStmt.setString(3, grade);
            }
            
            pStmt.executeUpdate();
            
            pStmt.close();
            conn.close();
            
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return false;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return false;
        }
    }
    
    public static boolean checkTakes(String s_id, int o_id) {
        try {
            Connection conn = global.getConnection();
            
            
            PreparedStatement pStmt = conn.prepareStatement("select * from takes where student_id = ? and offering_id = ?");
            pStmt.setString(1, s_id);
            pStmt.setInt(2, o_id);
            ResultSet rs = pStmt.executeQuery();
            
            return rs.next();
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("CNFE: " + cnfe);
            return true;
        }
        catch (SQLException sqle) {
            System.err.println("SQLE: " + sqle);
            return true;
        }
    }
}