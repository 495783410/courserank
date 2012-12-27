<%-- 
    Document   : editProfile
    Created on : 28 Dec, 2012, 12:38:32 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.profiles.profiles"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }
    
    // Loading student data
    String id = auth.getUser();
    
    SPStudentInfo stud = null;
    ArrayList<SPCourseInfo> courses = null;
    ArrayList<SPProjectInfo> projects = null;
    
    if (id != null && id != "") {
        stud = profiles.getSPStudentInfo(id);
        courses = profiles.getSPCourseInfo(id);
        projects = profiles.getSPProjectInfo(id);
    }
    
    header.addStyleSheet("css/profiles.css");
    header.addScript("scripts/jquery-1.7.1.js");
    header.addScript("scripts/studentEditProfile.js");
    header.addStyleSheet("css/course.css");
    
    
    if (id == null || id == "" || stud.photo == null) {
    }
    else {
        header.setPicUrl("loadImage?s_id=" + stud.student_id, "Profile Picture");
    }
    
    out.println(header.toString());
%>

<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Details</div>
            <ul>
                    <%
                        if (id != null && id != "" && stud.student_id != "") {
                            out.println("<a href=\"#\" onclick=\"showBasic()\"><li class='sb_selected'>Basic Info</li></a>");
                            out.println("<a href=\"#\" onclick=\"showCourses()\"><li>Courses</li></a>");
                            out.println("<a href=\"#\" onclick=\"showProjects()\"><li>Projects</li></a>");
                        }
                    %>
            </ul>
    </div>
</div>

<%
    if (id != null && id != "" && !stud.student_id.equals("")) {
%>
            
<div id="body" class="right">
    <div style="margin-top:30px">
        <div id="courseInfo">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Student Information</div>
            </div>
            <div class="courseContent">
                <table class="courseInfo">
                    <tr>
                        <td class="tleft">Name</td>
                        <td><%=stud.name%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Department</td>
                        <td><%=stud.department%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Id</td>
                        <td><%=id%></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <!-- Editing Basic Information -->
    <div id="Basic">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Basic Information</div>
            </div>
    <p>
        <%
            if (id != null && stud.student_id != "") {
                out.println("<h4>Name and CPI</h4>");
                out.println("<form id=\"basicForm\">");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>");
                out.println("Name:");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type=\"text\" name=\"StudentName\" value=\"" + stud.name + "\" /><br/>");
                out.println("</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>");
                out.println("CPI:");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type=\"text\" name=\"CPI\" value=\"" + (stud.cpi2/100.0) + "\" /><br/>");
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<input class='SubmitButton' type=\"button\" id=\"submitButtonBasic\" onclick=\"submitBasic('" + id + "')\" value=\"Submit\" /><br/>");
                out.println("</form><br/>");
                
                out.println("<h4>Password</h4>");
                out.println("<form id=\"passwordForm\">");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>");
                out.println("Current Password:");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type=\"password\" name=\"currentPassword\" /><br/>");
                out.println("</td>");
                out.println("</tr>");;
                out.println("<tr>");
                out.println("<td>");
                out.println("New Password:");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type=\"password\" name=\"newPassword1\" /><br/>");
                out.println("</td>");
                out.println("</tr>");;
                out.println("<tr>");
                out.println("<td>");
                out.println("Repeat New Password:");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type=\"password\" name=\"newPassword2\" /><br/>");
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<input class='SubmitButton' type=\"button\" id=\"submitButtonPassword\" onclick=\"submitPassword('" + id + "')\" value=\"Submit\"/><br/>");
                out.println("</form><br/>");
            }
            else {
                out.println("No such student profile exists");
            }
        %>
    </p>
    </div>
    <!-- Editing Course Grades -->
    <div id="Courses" style="display: none">
    <h3>Courses Undertaken</h3>
        <%
            if (id != null && courses != null && stud.student_id != "") {
                out.println("<table class=\"SPCourseTable\">");
                out.println("<tr class=\"SPCourseTableHeader\">");
                out.println("<th class=\"SPCourseTableColumn\" width=\"3%\">Course ID</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"12%\">Name</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"5%\">Department</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"3%\">Credits</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"3%\">Semester</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"3%\">Year</th>");
                out.println("<th class=\"SPCourseTableColumn\" width=\"3%\">Grade</th>");
                out.println("</tr>");
                int i=0;
                for (SPCourseInfo c: courses) {
                    out.println("<tr>");
                    out.println("<td><a href=\"coursePage.jsp?courseId=" + c.course_id + "&offeringId=" + c.offering_id + "\">" + c.course_id + "</a></td>");
                    out.println("<td>" + c.name + "</td>");
                    out.println("<td>" + c.department + "</td>");
                    out.println("<td>" + c.credits + "</td>");
                    out.println("<td>" + c.semester + "</td>");
                    out.println("<td>" + c.year + "</td>");
                    out.println("<td id=\"" + i + "grade\">");
                    out.println("<select name=\"" + c.offering_id + "\" value=\"" + c.offering_id + "\">");
                    {
                        out.println("<option value=\"AP\"" + (c.grade.equals("AP") ? " selected" : "") + " />AP</option>");
                        out.println("<option value=\"AA\"" + (c.grade.equals("AA") ? " selected" : "") + " />AA</option>");
                        out.println("<option value=\"AB\"" + (c.grade.equals("AB") ? " selected" : "") + " />AB</option>");
                        out.println("<option value=\"BB\"" + (c.grade.equals("BB") ? " selected" : "") + " />BB</option>");
                        out.println("<option value=\"BC\"" + (c.grade.equals("BC") ? " selected" : "") + " />BC</option>");
                        out.println("<option value=\"CC\"" + (c.grade.equals("CC") ? " selected" : "") + " />CC</option>");
                        out.println("<option value=\"CD\"" + (c.grade.equals("CD") ? " selected" : "") + " />CD</option>");
                        out.println("<option value=\"DD\"" + (c.grade.equals("DD") ? " selected" : "") + " />DD</option>");
                        out.println("<option value=\"XX\"" + (c.grade.equals("XX") ? " selected" : "") + " />XX</option>");
                        out.println("<option value=\"FR\"" + (c.grade.equals("FR") ? " selected" : "") + " />FR</option>");
                        out.println("<option value=\"\"" + (c.grade.equals("") ? " selected" : "") + " />Null</option>");
                    }
                    out.println("</select>");
                    out.println("</td>");
                    out.println("</tr>");
                    i++;
                }
                out.println("</table>");
                out.println("<input type=\"button\" id=\"submitButtonGrades\" value=\"Update\" onclick=\"submitGrades('" + id + "', " + i + ")\" />");
            }
            else {
                out.println("No such student profile exists");
            }
        %>
    <br/>
    </div>
    <!-- Editing Project Scores -->
    <div id="Projects" style="display: none">
    <h3>Projects Undertaken</h3>
    <p>
        <%
            if (id != null && stud.student_id != "") {
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>Name</th>");
                out.println("<th>Description</th>");
                out.println("<th>Course ID</th>");
                out.println("<th>Instructor</th>");
                out.println("<th>Score</th>");
                out.println("</tr>");
                int i=0;
                for (SPProjectInfo p: projects) {
                    out.println("<tr>");
                    out.println("<td>" + p.name + "</td>");
                    out.println("<td>" + p.description + "</td>");
                    out.println("<td><a href=\"coursePage.jsp?courseId=" + p.course_id + "&offeringId=" + p.offering_id + "\">" + p.course_id + "</a></td>");
                    out.println("<td><a href=\"instructorProfile.jsp?id=" + p.instructor_id + "\">" + p.instructor_name + "</a></td>");
                    out.println("<td id=\"" + i + "project\">");
                    out.println("<input type=\"text\" name=\"" + p.project_id + "\" value=\"" + p.score + "\" />");
                    out.println("</td>");
                    out.println("</tr>");
                    i++;
                }
                out.println("</table>");
                out.println("<input type=\"button\" id=\"submitButtonProjects\" value=\"Update\" onclick=\"submitProjects('" + id + "', " + i + ")\" />");
            }
            else {
                out.println("No such student profile exists");
            }
        %>
    </p>
    <br/>
    </div>
</div>

<%
    }
    else {
        out.println("<br/>");
        out.println("<h2>The requested student profile does not exist. Please check the student ID</h2>");
    }
    out.println(footer.toString());
%>