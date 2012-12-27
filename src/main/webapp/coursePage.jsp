<%-- 
    Document   : coursePage
    Created on : 28 Dec, 2012, 12:37:44 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CoursePage.offeringInfo"%>
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
    header.addScript("scripts/coursePageJs.js");
    header.addScript("scripts/linkDisplay.js");
    header.addScript("scripts/jquery.flot.js");
    header.addScript("scripts/stars.js");
    header.addStyleSheet("css/stars.css");
    header.addStyleSheet("css/course.css");
    header.addScript("scripts/registerOffering.js");
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    out.println(header.toString());
%>
<%
    String courseId = request.getParameter("courseId");
    String offeringIdStr = request.getParameter("offeringId");
    String studentId = auth.getUser();
    long offeringId = -1;
    if (offeringIdStr != null){
        offeringId = Long.parseLong(offeringIdStr);
    }
    CoursePage coursePage = new CoursePage();
%>
<input type="hidden" id="courseId" value="<%=courseId%>" />
<input type="hidden" id="offeringId" value="<%=offeringId%>" />
<input type="hidden" id="studentId" value="<%=studentId%>" />

<div id="sidebar" class="left">
    <div class="linkset">
        <div id="linkset_header">Important Links</div>
            <ul>
                <a href="coursePage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>"><li class="sb_selected">Course Page</li></a>
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

<div id="body" class="right">
<%    
    // Check if the given input is valid.
    if (courseId != null){
        // Display the information for the course-id and offering passed.
        courseInfo course_info = coursePage.getCourseInfo(courseId); 
%>
        <input type="hidden" id="courseRating" value="<%=course_info.averageRating%>">
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
<%
        // Check if the offeringId provided is valid.
        if (offeringId == -1){
            offeringId = coursePage.getLatestOfferingId(courseId);
        }

        // Check if the offering exists for the given course.  
        offeringInfo offering_info;
        if (offeringId != -1){
            // Get offering info.
            offering_info = coursePage.getOfferingInfo(offeringId, courseId); 
%>
            <input type="hidden" id="offeringRating" value="<%=offering_info.rating%>">
            <div id="offeringInfo">
                <div class="courseInfoHeader">
                    <div class="courseTitle left">Offering: <%=offering_info.course_id%>&nbsp;<%=offering_info.semester%>&nbsp;<%=offering_info.year%></div>
                    <a href="javascript:alterSubscription()">
                        <%
                        String value = (acad.db.courserank.dbis.profiles.profiles.checkTakes(studentId, (int)offeringId) ? "UnSubscribed" : "Subscribed");
                        System.err.println(value);
                        %>
                    <div id="courseSubscription" class="courseSubscribe <%=value%> right">
                        <div class="courseSubscribeLogo left"></div>
                        <div id="subscribeText" class="courseSubscribeText left"><%=(value=="Subscribed"? "Subscribe" : "Unsubscribe")%></div>
                    </div>
                    </a>
                </div>
                <div class="courseContent">
                    <table class="courseInfo">
                        <tr>
                            <td class="tleft">Credits</td>
                            <td><%=offering_info.credits%></td>
                        </tr>
                        <tr>
                            <td class="tleft">Rating</td>
                            <td><span class="stars" id="offeringRatingStars"><span></span></span></td>
                        </tr>
                        <tr>
                            <td class="tleft">Venue</td>
                            <td><%=offering_info.venue%></td>
                        </tr>
                        <tr>
                            <td class="tleft">Time Slot</td>
                            <td><%=offering_info.timeslot_id%></td>
                        </tr>
                        <tr>
                            <td class="tleft">Avg Work Load</td>
                            <td><%=offering_info.averageLoad%></td>
                        </tr>
                        <tr>
                            <td class="tleft">Grading Statistics</td>
                            <td>
                                <div>
                                    <div id="gradingStats" class="left">
                                        <ul class="gradingStats">
                                            <li><b>AP:</b>&nbsp;<span><%=offering_info.numGrade[0]%></span></li>
                                            <li><b>AA:</b>&nbsp;<span><%=offering_info.numGrade[1]%></span></li>
                                            <li><b>AB:</b>&nbsp;<span><%=offering_info.numGrade[2]%></span></li>
                                            <li><b>BB:</b>&nbsp;<span><%=offering_info.numGrade[3]%></span></li>
                                            <li><b>BC:</b>&nbsp;<span><%=offering_info.numGrade[4]%></span></li>
                                            <li><b>CC:</b>&nbsp;<span><%=offering_info.numGrade[5]%></span></li>
                                            <li><b>CD:</b>&nbsp;<span><%=offering_info.numGrade[6]%></span></li>
                                            <li><b>DD:</b>&nbsp;<span><%=offering_info.numGrade[7]%></span></li>
                                            <li><b>FR:</b>&nbsp;<span><%=offering_info.numGrade[8]%></span></li>
                                            <li><b>XX:</b>&nbsp;<span><%=offering_info.numGrade[9]%></span></li>
                                        </ul>
                                   </div>
                                   <div id="gradingStatsImg" class="right">
                                   </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
<%
        }
    }
    else{
        out.println("<p> Please enter some course id to view the information </p>");
    }
%>
</div>

<%
    coursePage.closeConnection();
    out.println(footer.toString());
%>
