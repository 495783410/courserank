package acad.db.courserank.dbis;

/*
 * TODO:
 * Decide on the format of time - search for timestamp type.
 * Decide on the sorting parameter for review, offerings, updates.
 * In table post, why is author the primary key.
 */

/**
 *
 * @author rishabh
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoursePage {

    static Connection conn;
    
    public CoursePage(){
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
     
    /*
     * Class to store the information about a course.
     */
    public static class courseInfo{
        public String course_id;
        public String name;
        public String department;
        public String description;
        public double averageRating;
    }

    public static class instructorInfo{
        public String instructor_id;
        public String instructor_name;
    }
    
    /*
     * Class to store the information about a particular offering.
     */
    public static class offeringInfo{
        public long offering_id;
        public String course_id;
        public int year;
        public String semester;
        public double credits;
        public double rating;
        public double averageLoad;
        public String venue;
        public int timeslot_id;
        public int strength;
        // Store the stats about grades in numGrades.
        // AP(0), AA(1), AB(2), BB(3), BC(4), CC(5), CD(6), DD(7), FR(8), XX(9)
        public int[] numGrade = new int[10];
        public ArrayList<instructorInfo> instructorList = new ArrayList<instructorInfo>();
    }
    
    /*
     * Class to store offering info in the offering list of a course.
     */
    public static class offeringListElement{
        public long offeringId;
        public int year;
        public String semester;
    }
    
    /*
     * Class to store information about an update of a given offering.
     */
    public static class postInfo{
        public long postId;
        public long parentPostId;
        public String studentId;
        public String studentName;
        public String mytime;
        public String content;
    }
    
    /*
     * Class to store information about a review of a given course.
     */
    public static class reviewInfo{
        public String mytime;
        public String content;
        public String author;
    }
    
    /*
     * Function to retrieve information about a particular course.
     * Returns an object containing all the necessary information of the course on success, null otherwise.
     */
    public static courseInfo getCourseInfo(String courseId){
        courseInfo resCourse = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement("select * from course where course_id = ?");
            pstmt.setString(1, courseId);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()){
                resCourse = new courseInfo();
                resCourse.course_id = rset.getString("course_id");
                resCourse.name = rset.getString("name");
                resCourse.department = rset.getString("department");
                resCourse.description = rset.getString("description");
            
            
                pstmt = conn.prepareStatement("select avg(rating) as avgRating from offering where course_id = ?");
                pstmt.setString(1, courseId);
                rset = pstmt.executeQuery();
                if (rset.next()){
                    resCourse.averageRating = rset.getDouble("avgRating");
                }
            }
            return resCourse;
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }
    }
        
    /*
     * Function to retrieve all the offerings of a particular course.
     * Returns an ArrayList of all the offering-ids on success, null otherwise.
     */
    public static ArrayList<offeringListElement> getOfferings(String courseId){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select offering_id, year, semester "
                    + "from offering "
                    + "where course_id = ? "
                    + "order by year desc, semester desc");

            pstmt.setString(1, courseId);

            ResultSet rset = pstmt.executeQuery();

            ArrayList<offeringListElement> offeringList = new ArrayList<offeringListElement>();
            
            while(rset.next()){
            offeringListElement newElement = new offeringListElement();
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
    
    public static ArrayList<instructorInfo> getInstructorList(long offeringId){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select instructor_id, name "
                    + "from instructor join teaches using (instructor_id) "
                    + "where offering_id = ?");

            pstmt.setLong(1, offeringId);
            ResultSet rset = pstmt.executeQuery();
            ArrayList<instructorInfo> instructorList = new ArrayList<instructorInfo>();
            
            while(rset.next()){
            instructorInfo newElement = new instructorInfo();
                newElement.instructor_id = rset.getString("instructor_id");
                newElement.instructor_name = rset.getString("name");
                instructorList.add(newElement);
            }
           
            return instructorList;   
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }           
    }
    
    /*
     * Function to get the latest offering id for the given course-id.
     * Returns offering id of type Long.
     */
    public static long getLatestOfferingId(String courseId){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select offering_id from offering "
                    + "where course_id = ? "
                    + "order by year desc, semester desc");

            pstmt.setString(1, courseId);

            ResultSet rset = pstmt.executeQuery();

            long offeringId = -1;
            
            if(rset.next()){
                offeringId = rset.getLong("offering_id");
            }
            
            return offeringId;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return -1;
        }           
    }
    
    /*
     * Function to retrieve information about a particular offering of a course.
     * Returns an object containing all the necessary information of the offering on success, null otherwise.
     */
    public static offeringInfo getOfferingInfo(long offeringId, String courseId){
        offeringInfo resOffering = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement("select * "
                    + "from offering "
                    + "where offering_id = ? and course_id = ?");

            pstmt.setLong(1, offeringId);
            pstmt.setString(2, courseId);

            ResultSet rset = pstmt.executeQuery();

            if (rset.next()){
                resOffering = new offeringInfo();
                resOffering.offering_id = rset.getLong("offering_id");
                resOffering.course_id = rset.getString("course_id"); 
                resOffering.year = rset.getInt("year");
                resOffering.semester = rset.getString("semester");
                resOffering.credits = rset.getDouble("credits");
                resOffering.venue = rset.getString("venue");
                resOffering.timeslot_id = rset.getInt("timeslot_id");
                resOffering.strength = rset.getInt("strength");   
                resOffering.rating = rset.getDouble("rating");
                resOffering.averageLoad = rset.getDouble("aww");
                
                for (int i = 0; i < 10; i++){
                    resOffering.numGrade[i] = rset.getInt(i + 11);
                }
            

                pstmt = conn.prepareStatement("select name, instructor_id "
                        + "from teaches join instructor using (instructor_id) "
                        + "where offering_id = ?");
                pstmt.setLong(1, offeringId);

                rset = pstmt.executeQuery();

                while(rset.next()){
                    instructorInfo newEntry = new instructorInfo();
                    newEntry.instructor_id = rset.getString("instructor_id");
                    newEntry.instructor_name = rset.getString("name");
                    resOffering.instructorList.add(newEntry);
                }
            }
            
            return resOffering;

        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }        
    }
    
    /*
     * Function to add a review for a paritcular course to the database.
     * Returns: 1 for success, 0 otherwise.
     */
    public static int addReview(String course_id, String content, boolean showAuthor, String authorName){
        try{
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement("select * from course where course_id = ?");
            pstmt.setString(1, course_id);
            ResultSet rset = pstmt.executeQuery();
            if (!(rset.next())){
                return 0;
            }

            pstmt = conn.prepareStatement("insert into review(course_id, mytime, content, author) values(?, ?, ?, ?)");

            pstmt.setString(1, course_id);
            pstmt.setTimestamp(2, null);
            pstmt.setString(3, content);
            if (!showAuthor){
                pstmt.setString(4, "Anonymous");
            }
            else{
                PreparedStatement pstmt2 = conn.prepareStatement("select name from student where student_id = ?");
                //TODO : change to be made here.
                pstmt2.setString (1, authorName);
                
                ResultSet rset2 = pstmt2.executeQuery();
                
                if (rset2.next()){
                    pstmt.setString(4, rset2.getString("name"));    
                }
            }
            
            pstmt.executeUpdate();
            
            return 1;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return 0;
        }        
    }
    
    /*
     * Function to get the reviews for a given course.
     * Returns ArrayList of reviews on success, null otherwise.
     */
    public static ArrayList<reviewInfo> getReviews(String course_id){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select mytime, content, author "
                    + "from review "
                    + "where course_id = ? "
                    + "order by mytime asc");

            pstmt.setString(1, course_id);

            ResultSet rset = pstmt.executeQuery();
            
            ArrayList<reviewInfo> reviewList = new ArrayList<reviewInfo>();
            
            while(rset.next()){
                reviewInfo newReview = new reviewInfo();
                
                newReview.mytime = rset.getString("mytime");
                newReview.content = rset.getString("content");
                newReview.author = rset.getString("author");
                
                reviewList.add(newReview);
            }

            return reviewList;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }                
    }
    
    /*
     * Function to get the discussions for a particular offering.
     * Returns ArrayList of updates on success, null otherwise.
     */
    public static ArrayList<postInfo> getPosts(long offering_id, boolean privilegeFlag){
        try{
            PreparedStatement pstmt;
            if (!privilegeFlag){
                pstmt = conn.prepareStatement("select S.post_id, S.parent_post_id, S.student_id, student.name, S.mytime, "
                        + "(select max(mytime) from post as T where T.parent_post_id = S.post_id) as maxTime, S.content "
                        + "from post as S natural join student "
                        + "where offering_id = ? and post_id = parent_post_id "
                        + "order by maxTime desc");
            }
            else{
                pstmt = conn.prepareStatement("select S.post_id, S.parent_post_id, S.student_id, student.name, S.mytime, "
                        + "(select max(mytime) from post as T where T.parent_post_id = S.post_id) as maxTime, S.content "
                        + "from post as S natural join student "
                        + "where offering_id = ? and post_id = parent_post_id and privilege_flag = ? "
                        + "order by maxTime desc");
                pstmt.setBoolean(2, privilegeFlag);
            }
            pstmt.setLong(1, offering_id);

            ResultSet rset = pstmt.executeQuery();
            
            ArrayList<postInfo> primaryPostList = new ArrayList<postInfo>();
            
            while(rset.next()){
                postInfo newPost = new postInfo();
                newPost.postId = rset.getLong("post_id");
                newPost.parentPostId = rset.getLong("parent_post_id");
                newPost.studentName = rset.getString("student.name");
                newPost.studentId = rset.getString("student_id");
                newPost.mytime = rset.getString("myTime");
                newPost.content = rset.getString("content");
                
                primaryPostList.add(newPost);
            }
            
            ArrayList<postInfo> postList = new ArrayList<postInfo>();
            
            for (int i = 0; i < primaryPostList.size(); i++){
                postList.add(primaryPostList.get(i));
                
                pstmt = conn.prepareStatement("select post_id, parent_post_id, student_id, student.name, mytime, content "
                        + "from post natural join student "
                        + "where post_id != ? and parent_post_id = ? "
                        + "order by mytime asc");
                pstmt.setLong(1, primaryPostList.get(i).postId);
                pstmt.setLong(2, primaryPostList.get(i).postId);
                
                int numCommentsReqd = 3;
                
                rset = pstmt.executeQuery();
                rset.afterLast();
                ArrayList<postInfo> tempCommentList = new ArrayList<postInfo>();
                
                while(rset.previous() && numCommentsReqd > 0){
                    numCommentsReqd--;
                    postInfo newPost = new postInfo();
                    newPost.postId = rset.getLong("post_id");
                    newPost.parentPostId = rset.getLong("parent_post_id");
                    newPost.studentName = rset.getString("student.name");
                    newPost.studentId = rset.getString("student_id");
                    newPost.mytime = rset.getString("myTime");
                    newPost.content = rset.getString("content");
                    
                    tempCommentList.add(newPost);
                }
                
                for (int j = tempCommentList.size() - 1; j >= 0; j--){
                    postList.add(tempCommentList.get(j));
                }
            }

            return postList;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }                
    }

    public static ArrayList<postInfo> getSinglePost(long postId){
        try{
            PreparedStatement pstmt = conn.prepareStatement("select post_id, parent_post_id, student_id, student.name, mytime, content "
                    + "from post natural join student where parent_post_id = ? order by mytime asc");

            pstmt.setLong(1, postId);
            ResultSet rset = pstmt.executeQuery();

            ArrayList<postInfo> postList = new ArrayList<postInfo>();
            
            while(rset.next()){
                postInfo newPost = new postInfo();
                newPost.postId = rset.getLong("post_id");
                newPost.parentPostId = rset.getLong("parent_post_id");
                newPost.studentName = rset.getString("student.name");
                newPost.studentId = rset.getString("student_id");
                newPost.mytime = rset.getString("myTime");
                newPost.content = rset.getString("content");
                
                postList.add(newPost);
            }

            return postList;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return null;
        }                
    }
    
    /*
     * Function to add an update to appropriate table.
     * Returns 1 on success, 0 otherwise.
     */
    public static int addPost(String student_id, long offering_id, String content, boolean privilege_flag, long parentPostId){
        try{
            
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement("select * from offering where offering_id = ?");
            pstmt.setLong(1, offering_id);
            ResultSet rset = pstmt.executeQuery();
            if (!(rset.next())){
                return 0;
            }
            
            pstmt = conn.prepareStatement("insert into post values(?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, null);
            if (parentPostId == -1){
                pstmt.setString(2, null);
            }
            else{
                pstmt.setLong(2, parentPostId);
            }
            pstmt.setString(3, student_id);
            pstmt.setLong(4, offering_id);
            pstmt.setTimestamp(5, null);
            pstmt.setString(6, content);
            pstmt.setBoolean(7, privilege_flag);

            pstmt.executeUpdate();

            if (parentPostId == -1){
                pstmt = conn.prepareStatement("select max(post_id) from post");
                rset = pstmt.executeQuery();
                long recentEntry = -1;
                if (rset.next()){
                    recentEntry = rset.getLong(1);
                }
                
                if (recentEntry != -1){
                    pstmt = conn.prepareStatement("update post set parent_post_id = ? where post_id = ?");
                    pstmt.setLong(1, recentEntry);
                    pstmt.setLong(2, recentEntry);
                    
                    pstmt.executeUpdate();
                }                
            }
            return 1;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return 0;
        }        
    }

    public static int deletePost(long post_id, long parent_post_id){
        try{
            PreparedStatement pstmt; 
            if (post_id == parent_post_id){
                pstmt = conn.prepareStatement("delete from post where parent_post_id = ?");
            }
            else{
                pstmt = conn.prepareStatement("delete from post where post_id = ?");
            }

            pstmt.setLong(1, post_id);
            pstmt.executeUpdate();
            return 1;
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return 0;
        }        
    }
    
    public static boolean isPrivilegedUser(String studentId, long offeringId){
        try{
            PreparedStatement pstmt; 
            pstmt = conn.prepareStatement("select * from classrep where student_id = ?, offering_id = ?");
            pstmt.setString(1, studentId);
            pstmt.setLong(2, offeringId);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()){
                return true;
            }
            else{
                return false;
            }
            
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
            return false;
        }                
    }

    /*
     * Display class.
     * Assumption : course id that has been given is valid and offering id may -1 (for no offering selected).
     */
    public static String displayCourseInfo(String courseId) throws IOException, ClassNotFoundException, SQLException {
        assert(courseId != null) : "In class CoursePage, function display, courseId is null";
        String courseInfoString = "<p>";
 
        // Get course info for the given courseId
        courseInfo course_info = getCourseInfo(courseId);            
        if (course_info == null){
            courseInfoString += "Course-id : " + courseId + " not found";
        }
        else{      
            courseInfoString += course_info.course_id + " ";
            courseInfoString += course_info.name + " ";
            courseInfoString += course_info.department + " ";
            courseInfoString += course_info.description + " ";
            courseInfoString += course_info.averageRating;
            courseInfoString += "<input type='hidden' id='courseRating' value='" + course_info.averageRating + "' />";
        }
        courseInfoString += "</p>";
        return courseInfoString;        
    }
    
    public static String displayOfferingInfo(String courseId, long offeringId) throws IOException, ClassNotFoundException, SQLException {
        assert(courseId != null) : "In class CoursePage, function display, courseId is null";
        String offeringInfoString = "<p>";
 
        // Check if the offeringId provided is valid.
        if (offeringId == -1){
            offeringId = getLatestOfferingId(courseId);
        }

        // Check if the offering exists for the given course.
        if (offeringId == -1){
            offeringInfoString += "There are no offerings of this course";
        }
        else{
            // Get offering info.
            offeringInfo offering_info = getOfferingInfo(offeringId, courseId); 
            if (offering_info != null){
                offeringInfoString += offering_info.course_id + " ";
                offeringInfoString += offering_info.offering_id + " ";
                offeringInfoString += offering_info.semester + " ";
                offeringInfoString += offering_info.year + " ";
                offeringInfoString += offering_info.timeslot_id + " ";
                offeringInfoString += offering_info.venue + " ";
                offeringInfoString += offering_info.credits + " ";
                offeringInfoString += offering_info.rating + " ";
                offeringInfoString += offering_info.averageLoad + " ";                
                offeringInfoString += offering_info.numGrade[0] + " ";
                offeringInfoString += offering_info.numGrade[1] + " ";
                offeringInfoString += offering_info.numGrade[2] + " ";
                offeringInfoString += offering_info.numGrade[3] + " ";
                offeringInfoString += offering_info.numGrade[4] + " ";
                offeringInfoString += offering_info.numGrade[5] + " ";
                offeringInfoString += offering_info.numGrade[6] + " ";
                offeringInfoString += offering_info.numGrade[7] + " ";
                offeringInfoString += offering_info.numGrade[8] + " ";
                offeringInfoString += offering_info.numGrade[9] + " ";
                
                for (int i = 0; i < offering_info.instructorList.size(); i++){
                    offeringInfoString += "<br />" + offering_info.instructorList.get(i).instructor_id + " ";
                    offeringInfoString += offering_info.instructorList.get(i).instructor_name;
                }
                
                offeringInfoString += "<input type='hidden' id='offeringRating' value='" + offering_info.rating + "' />";
            }
            else{
                offeringInfoString += "Offering id : " + offeringId + " not found";
            }
        }

        offeringInfoString += "</p>";
        return offeringInfoString;        
    }
    
    public static void displaySideList(){
        
    }
}
