<%-- 
    Document   : instructorProfile
    Created on : 28 Dec, 2012, 12:43:35 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.profiles.profiles.IPProjectInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.IPCourseInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.IPInstructorInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles"%>
<%@page import="java.util.ArrayList"%>
<%@page import="acad.db.courserank.dbis.profiles.*"%>
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
    header.addStyleSheet("css/course.css");
    header.addStyleSheet("css/courseCompare.css");
    
    // Loading instructor data
    String id = request.getParameter("id");
    
    IPInstructorInfo instr = null;
    ArrayList<IPCourseInfo> courses = null;
    ArrayList<IPProjectInfo> projects = null;
    
    System.out.println(id);
    
    if (id != null && id != "") {
        instr = profiles.getIPInstructorInfo(id);
        courses = profiles.getIPCourseInfo(id);
        projects = profiles.getIPProjectInfo(id);
        
        System.out.println(courses);
        System.out.println(projects);
    }
    
    header.addScript("scripts/jquery-1.7.1.js");
    header.addScript("scripts/stars.js");
    header.addStyleSheet("css/stars.css");
    header.addScript("scripts/instructorProfile.js");
    header.addStyleSheet("css/profiles.css");
    out.println(header.toString());
%>

<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Details</div>
            <ul>
                    <%
                        if (id != null && id != "" && instr.instructor_id != "") {
                            out.println("<a href='javascript:showCourses()'><li class='sb_selected'>Courses</li></a>");
                            out.println("<a href='javascript:showProjects()'><li>Projects</li></a>");
                        }
                    %>
            </ul>
    </div>
</div>

<%
    if (id != null && id != "" && !instr.instructor_id.equals("")) {
%>
            
<div id="body" class="right">
    <div style="margin-right:30px">
        <div id="courseInfo">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Instructor Information</div>
            </div>
            <div class="courseContent">
                <table class="courseInfo">
                    <tr>
                        <td class="tleft">Name</td>
                        <td><%=instr.name%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Department</td>
                        <td><%=instr.department%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Rating</td>
                        <td><span class="stars" id="instRating"><span></span></span></td>
                    </tr>
                    <tr>
                        <td class="tleft">ID</td>
                        <td><%=id%></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <input type="hidden" id="instRatingValue" value="<%=(instr.rating2/100.0)%>" />
    </div>
    <!-- Writing Courses -->
    <div id="Courses" class="Space">
        <div class="courseInfoHeader">
            <div class="courseTitle left">Courses Taught</div>
        </div>
        <%
            if (id != null && courses != null && instr.instructor_id != "") {
                out.println("<table id='courseCompareInfo' width=760px>");
                out.println("<tr>");
                out.println("<td class='labelcenter'>Course ID</td>");
                out.println("<td class='labelcenter'>Name</td>");
                out.println("<td class='labelcenter'>Department</td>");
                out.println("<td class='labelcenter'>Credits</td>");
                out.println("<td class='labelcenter'>Semester</td>");
                out.println("<td class='labelcenter'>Year</td>");
                out.println("<td class='labelcenter'>Rating</td>");
                out.println("</tr>");
                for (IPCourseInfo c: courses) {
                    out.println("<tr>");
                    out.println("<td>" + c.course_id + "</td>");
                    out.println("<td>" + c.name + "</td>");
                    out.println("<td>" + c.department + "</td>");
                    out.println("<td>" + c.credits + "</td>");
                    out.println("<td>" + c.semester + "</td>");
                    out.println("<td>" + c.year + "</td>");
                    out.println("<td>" + c.rating2/100.0 + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            else {
                out.println("No such instructor profile exists");
            }
        %>
    <br/>
    </div>
    <!-- Writing Projects -->
    <div id="Projects" class="Space" style="display: none">
        <div class="courseInfoHeader">
            <div class="courseTitle left">Projects Advised</div>
        </div>
        <%
            if (id != null && instr.instructor_id != "") {
                out.println("<div id='profileElements'>");
                for (IPProjectInfo p: projects) {
                    %>
                        <div class="courseContent">
                            <table class="courseInfo">
                                <tr>
                                    <td class="tleft">Name</td>
                                    <td><%=p.name%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Course ID</td>
                                    <td><%=("<a href='coursePage.jsp?courseId=" + p.course_id + "&offeringId=" + p.offering_id + "'>")%> <%=p.course_id%> </a></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Students (Score)</td>
                                    <td>
                                        <%
                                            out.println("<ul>");
                                            for (int k=0; k<p.student_ids.size(); k++){
                                                out.println("<li><a href=\"/studentProfile.jsp?id=" + p.student_ids.get(k) + "\">" + p.student_names.get(k) + " (" + p.student_scores2.get(k)/100.0 + ")</a></li>");
                                            }
                                            out.println("</ul>");
                                        %>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tleft">Description</td>
                                    <td><%=p.description%></td>
                                </tr>
                            </table>
                        </div>
                        <hr />
                    <%
                }
                out.println("</div>");
            }
            else {
                out.println("No such instructor exists");
            }
        %>
    </div>
</div>

<%
    }
    else {
        out.println("<br/>");
        out.println("<h2>No such instructor exists. Please check the instructor ID</h2>");
    }
    out.println(footer.toString());
%>