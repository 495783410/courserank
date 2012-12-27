<%-- 
    Document   : feedback
    Created on : 28 Dec, 2012, 12:41:57 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.profiles.profiles"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.FBInstructorInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.FBCourseInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.instructorInfo"%>
<%@page import="acad.db.courserank.dbis.CoursePage.offeringListElement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    Header header = new Header();
    Footer footer = new Footer();

    String s_id = auth.getUser();
//    String s_id = "0908843742";
    
    int o_id = (request.getParameter("offeringId") == "" || request.getParameter("offeringId") == null) ? -1 : Integer.parseInt(request.getParameter("offeringId"));
    String i_id = request.getParameter("instructorId");
    
    FBCourseInfo course = null;
    FBInstructorInfo instructor = null;
    
    if (s_id != null && i_id != null && o_id != -1 && s_id != "" && i_id != "") {
        course = profiles.getFBCourseInfo(o_id);
        instructor = profiles.getFBInstructorInfo(i_id);
    }
    
    
    header.addScript("scripts/feedback.js");
    header.addScript("scripts/linkDisplay.js");
    header.addScript("scripts/jquery-1.7.1.js");
    header.addScript("scripts/jquery.rating.js");
    header.addScript("scripts/jquery.MetaData.js");
    header.addStyleSheet("scripts/jquery.rating.css");
    out.println(header.toString());
%>
<%   
    String courseId = request.getParameter("courseId");
    int offeringId = o_id;
    String instructorId = i_id;
    CoursePage coursePage = new CoursePage();
%>

<input type="hidden" id="courseId" value="<%=courseId%>" />
<input type="hidden" id="offeringId" value="<%=offeringId%>" />
<input type="hidden" id="instructorId" value="<%=instructorId%>" />


<div id="sidebar" class="left">
    <div class="linkset">
        <div id="linkset_header">Important Links</div>
            <ul>
                <a href="coursePage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li>Course Page</li></a>
                <a href="reviewPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li>Reviews</li></a>
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

<!--<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Information</div>
            <ul>-->
                    <%
/*                        if (s_id != null && o_id != -1 && i_id != null && s_id != "" && i_id != "" && course.course_id != "" && instructor.instructor_id != "") {
                            out.println("<li><a href=\"coursePage.jsp?courseId=" + course.course_id + "&offeringId=" + o_id + "\">" + course.course_id + "</a></li>");
                            out.println("<li><a href=\"instructorProfile.jsp?id=" + i_id + "\">" + instructor.name + "</a></li>");
                        }
*/                   %>
<!--            </ul>
    </div>
</div>-->

<%
    if (s_id != null && o_id != -1 && i_id != null && s_id != "" && i_id != "" && course.course_id != "" && instructor.instructor_id != "") {
%>

<div id="body" class="right">
    <p>
        <%
            boolean check = profiles.checkFeedback(s_id, i_id, o_id);
            if(check) {
                out.println("<h3>You are not allowed to submit feedback for this (offering, instructor) pair</h3>");
                out.println("<h5>Either you have already submitted feedback for this pair, or the pair is invalid.</h5>");
            }
            else {
        %>
        <h3>Please Fill up the following and click on submit to finalize.</h3>
        <table>
        <tr>
        <td>
        Course: <%=course.course_id%> <%=course.name%>
        <br/>
        Semester: <%=course.semester%> <%=course.year%>
        <br/>
        Department: <%=course.department%>
        </td>
        <td>
        Instructor: <%=instructor.name%>
        <br/>
        ID: <%=instructor.instructor_id%>
        <br/>
        Department: <%=instructor.department%>
        </td>
        </tr>
    </table>
        <form>
            <table>
                <tr>
                    <td id="cr">Course Rating</td>
                    <td>
                    <input type="radio" name="courseRating" class="star" />
                    <input type="radio" name="courseRating" class="star" />
                    <input type="radio" name="courseRating" class="star" checked="true" />
                    <input type="radio" name="courseRating" class="star" />
                    <input type="radio" name="courseRating" class="star" />
                    </td>
                </tr>
                <tr>
                    <td id="ir">Instructor Rating</td>
                    <td>
                    <input type="radio" name="instructorRating" class="star" />
                    <input type="radio" name="instructorRating" class="star" />
                    <input type="radio" name="instructorRating" class="star" checked="true" />
                    <input type="radio" name="instructorRating" class="star" />
                    <input type="radio" name="instructorRating" class="star" />
                    </td>
                </tr>
                <tr>
                    <td id="dr">Difficulty Rating</td>
                    <td>
                    <input type="radio" name="difficultyRating" class="star" />
                    <input type="radio" name="difficultyRating" class="star" />
                    <input type="radio" name="difficultyRating" class="star" checked="true" />
                    <input type="radio" name="difficultyRating" class="star" />
                    <input type="radio" name="difficultyRating" class="star" />
                    </td>
                </tr>
                <tr>
                    <td>Average Weekly Workload</td>
                    <td><input type="text" name="AvgWW" id="avgWWfield" /></td>
                </tr>
            </table><br/>
            <%
                out.println("<input type='button' value='Submit' enabled id='submitButton' onClick='submitFeedback(\"" + s_id + "\", " + o_id + ", \"" + i_id + "\")' />");
            %>
        </form>
        <%
            }
        %>
    </p>
</div>

<%
    }
    else {
        out.println("<br/>");
        out.println("<h2>The given IDs are incorrect. Please check.</h2>");
    }
    out.println(footer.toString());
%>