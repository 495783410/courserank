<%-- 
    Document   : discussionPage
    Created on : 28 Dec, 2012, 12:38:16 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CoursePage.courseInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.instructorInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.postInfo"%>
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
    header.addScript("scripts/discussionPageJs.js");
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
                <a href="reviewPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li>Reviews</li></a>
                <a href="discussionPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li class="sb_selected">Discussion</li></a>
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
        <div class="courseTitle left">Discussions</div>
    </div>

    <li class="Post">
        <div id="postBox">
            <textarea class="postTextArea" id="postContent" name="postContent" placeholder="Add a new Post"></textarea>
    
<%
    boolean privilegedUser = coursePage.isPrivilegedUser(studentId, offeringId);
    if (privilegedUser){
%>  
            <span style="float:right" class="postHeaderTime">Broadcast: <input type="checkbox" id="privilegeCheckBox" /></span>
<%
   }
%>   
            <div id="postSubmit" class="SubmitButton">Post</div>
        </div>
    </li>

    <div id="offeringPosts">
        <ul class="posts">
<%
    if (offeringId != -1){
        ArrayList<postInfo> postList = coursePage.getPosts(offeringId, false);
        if (postList != null){
            for (int i = 0; i < postList.size();){
                postInfo elem = postList.get(i++);
                if (elem.postId == elem.parentPostId){
%>  
                <hr />
                <li class="Post">
                    <div class="post">
                        <div class="postHeader">
                            <a href="studentProfile.jsp?id=<%=elem.studentId%>">
                                <div class="postHeaderName"><%=elem.studentName%></div>
                            </a>
                            <a href="postPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>&postId=<%=elem.postId%>">
                                <div class="postHeaderTime"><%=elem.mytime%></div>
                            </a>
                        </div>
                        <div class="postContent"><%=elem.content%></div>
                        
<%
                    if (elem.studentId.equals(studentId)){
%>                                                       
                        <a id="deletePost<%=elem.postId%>" href="javascript:deletePost(<%=elem.postId%>, <%=elem.parentPostId%>)">
                            <div class="deletePostComment"></div>
                        </a>
                    </div>
<%
                    }
%>
                    <ul class="comments">
<%
                }
           
                while (i < postList.size()){
                    postInfo childElem = postList.get(i++);
                    if (childElem.parentPostId != elem.postId){
                        i--;
                        break;
                    }
%>
                        <hr/>
                        <li class="comment">
                            <div>
                                <a href="studentProfile.jsp?id=<%=childElem.studentId%>">
                                    <span class="commentHeaderName"><%=childElem.studentName%></span>
                                </a>
                                <%=childElem.content%>
                            </div>
                            <div class="commentTime"><%=childElem.mytime%></div>

<%
                        if (childElem.studentId.equals(studentId)){
%>
                            <a id="deletePost<%=childElem.postId%>" href="javascript:deletePost(<%=childElem.postId%>, <%=childElem.parentPostId%>)">
                                <div class="deletePostComment"></div>
                            </a>
<%
                        }
%>
                        </li>
<%            
                }
%>  
                        <hr />
                        <div id="commentBox">
                            <textarea class="commentTextArea" id="postContent<%=elem.postId%>" placeholder="Add a new Comment"></textarea>
                            <a href="javascript:insertPost(<%=elem.postId%>)">
                                <div name="<%=elem.postId%>" class="SubmitButton">Post</div>
                            </a>
                        </div>

                    </ul>     
                </li>
<%
            }
        }
    }
%>    
        <hr />
        </ul>
    </div>
</div>

<%
    coursePage.closeConnection();
    out.println(footer.toString());
%>
