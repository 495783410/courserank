<%-- 
    Document   : review
    Created on : 28 Dec, 2012, 12:44:18 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CoursePage.courseInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.instructorInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.reviewInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.offeringListElement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    header.addScript("scripts/jquery.js");
    header.addScript("scripts/reviewPageJs.js");
    header.addScript("scripts/linkDisplay.js");
    header.addStyleSheet("css/posts.css");
    header.addScript("scripts/stars.js");
    header.addStyleSheet("css/stars.css");
    header.addStyleSheet("css/course.css");

    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    out.println(header.toString());
%>
<%
    String studentId = auth.getUser();
    String courseId = request.getParameter("courseId");
    String offeringIdStr = request.getParameter("offeringId");
    long offeringId = -1;
    if (offeringIdStr != null){
        offeringId = Long.parseLong(offeringIdStr);
    }
    CoursePage coursePage = new CoursePage();
%>
<input type="hidden" id="courseId" value="<%=courseId%>" />
<input type="hidden" id="offeringId" value="<%=offeringId%>" />

<div id="sidebar" class="left">
    <div class="linkset">
        <div id="linkset_header">Important Links</div>
            <ul>
                <a href="coursePage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li>Course Page</li></a>
                <a href="reviewPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li class="sb_selected">Reviews</li></a>
                <a href="discussionPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li>Discussion</li></a>
            </ul>
    </div>

    <div class="linkset">
        <div id="linkset_header">Course Offerings</div>
            <ul>
<%
    ArrayList<offeringListElement> offerings = coursePage.getOfferings(courseId);
    if (offerings != null){
        for (int i = 0; i < offerings.size(); i++){
            offeringListElement elem = offerings.get(i);
%>
            <a href="coursePage.jsp?courseId=<%=courseId%>&offeringId=<%=elem.offeringId%>"
               id="coursePage.jsp?courseId=<%=courseId%>&offeringId=<%=elem.offeringId%>">
                <li><%=elem.semester +  " " + elem.year%></li>
            </a>  
<%
        }
    }
%>
            </ul>
    </div>
            
    <div class="linkset">
        <div id="linkset_header">Feedback</div>
            <ul>
<%
    ArrayList<instructorInfo> instructorList = coursePage.getInstructorList(offeringId);
    if (instructorList != null){
        for (int i = 0; i < instructorList.size(); i++){
            instructorInfo newEntry = instructorList.get(i);
%>  
            <a href="feedback.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>&instructorId=<%=newEntry.instructor_id%>"
               id ="instructorId<%=newEntry.instructor_id%>">
                <li><%=newEntry.instructor_name%></li>
            </a>
<%
       }
    }
%>
            </ul>
    </div>
            
    <div class="linkset">
        <div id="linkset_header">Other Links</div>
            <ul>
                <a href="courseComparePage.jsp"><li>Course compare</li></a>
            </ul>
    </div>

</div>

<div id="body" class="right">
<%    
    // Check if the given input is valid.
    if (courseId != null){
        // Display the information for the course-id and offering passed.
        courseInfo course_info = coursePage.getCourseInfo(courseId); 
%>
        <input type="hidden" id="courseRating" value="<%=course_info.averageRating%>" />
        <div id="courseContainer">
            <div id="courseInfo">
                <div class="courseInfoHeader">
                    <div class="courseTitle left"><%=course_info.course_id%>&nbsp;<%=course_info.name%></div>
                </div>
                <div class="courseContent">
                    <table class="courseInfo">
                        <tr>
                            <td class="tleft">Department</td>
                            <td><%=course_info.department%></td>
                        </tr>
                        <tr>
                            <td class="tleft">Rating</td>
                            <td><span class="stars" id ="courseRatingStars"><span></span></span></td>
                        </tr>
                        <tr>
                            <td class="tleft">Description</td>
                            <td><%=course_info.description%></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
<%
   }
%> 

    <div class="courseInfoHeader">
        <div class="courseTitle">Reviews</div>
    </div>

    <div id="courseReviews">
        <ul class="posts">
<%
    ArrayList<reviewInfo> reviewList = coursePage.getReviews(courseId);
    if (reviewList != null){
        for (int i = 0; i < reviewList.size(); i++){
            reviewInfo elem = reviewList.get(i);
%>
                <li class="Post">
                    <div class="post">
                        <div class="postHeader">
                            <div class="postHeaderName"><%=elem.author%></div>
                            <div class="postHeaderTime"><%=elem.mytime%></div>
                        </div>
                        <div class="postContent"><%=elem.content%></div>

                </li>
                <hr />
<%
        }
    }
    
%>    
        </ul>
        <li class="Post">
            <div id="postBox">
                <textarea class="postTextArea" id="reviewContent" name="reviewContent" placeholder="Add a new Review"></textarea>
                <span style="float:right" class="postHeaderTime">Display name: <input type="checkbox" id ="author" /></span>
               <div id="reviewSubmit" name="reviewSubmit" class="SubmitButton">Post</div>
            </div>
        </li>
    </div>
</div>

<%
    coursePage.closeConnection();
    out.println(footer.toString());
%>
