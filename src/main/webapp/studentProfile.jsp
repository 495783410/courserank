<%-- 
    Document   : studentProfile
    Created on : 28 Dec, 2012, 12:45:10 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.profiles.profiles.SPWorkInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.SPProjectInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.SPCourseInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles.SPStudentInfo"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles"%>
<%@page import="acad.db.courserank.dbis.profiles.profiles"%>
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
    header.addStyleSheet("css/course.css");
    header.addStyleSheet("css/courseCompare.css");
    header.addScript("scripts/jquery.js");
    
    // Loading student data
    String id;
    if(request.getParameter("id")==null) {
        id = auth.getUser();
    } else {
        id = request.getParameter("id");
    }
    
    SPStudentInfo stud = null;
    ArrayList<SPCourseInfo> courses = null;
    ArrayList<SPProjectInfo> projects = null;
    ArrayList<SPWorkInfo> works = null;
    
    if (id != null && id != "") {
        stud = profiles.getSPStudentInfo(id);
        courses = profiles.getSPCourseInfo(id);
        projects = profiles.getSPProjectInfo(id);
        works = profiles.getSPWorkInfo(id);
    }
    
    header.addStyleSheet("css/profiles.css");
    if (id == "" || id == null || stud.student_id == "" || stud.photo == null) {
    }
    else {
        header.setPicUrl("loadImage?s_id=" + stud.student_id, "Profile Picture");
    }
    header.addScript("scripts/studentProfile.js");
    out.println(header.toString());
%>

<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Details</div>
            <ul>
                    <%
                        if (id != null && id != "" && stud.student_id != "") {
                            /*if (stud.photo == null) {
                                out.println("<li style=\"cursor: default;\">Image Unavailable</li>");
                            }
                            else {
                                //out.println("<li style=\"cursor: default;\"><img src=\"loadImage?s_id=" + stud.student_id + "\"></li>");
                            }*/
                            out.println("<a href='javascript:showCourses()'><li class='sb_selected'>Courses</li></a>");
                            out.println("<a href='javascript:showProjects()'><li>Projects</li></a>");
                            out.println("<a href='javascript:showWork()'><li>Work Experience</li></a>");
                        }
                    %>
            </ul>
    </div>
</div>

<%
    if (id != null && id != "" && !stud.student_id.equals("")) {
%>
            
<div id="body" class="right">
    
    <div style="margin-right:30px;">
        <div id="courseInfo">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Student Information</div>
            </div>
            <div class="courseContent">
                <table class="courseInfo">
                    <tr>
                        <td class="tleft">Known as</td>
                        <td><%=stud.name%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Studies</td>
                        <td><%=stud.department%></td>
                    </tr>
                    <tr>
                        <td class="tleft">Identified by</td>
                        <td><%=id%></td>
                    </tr>
                </table>
            </div>
        </div>

        <!-- Writing Courses -->
        <div id="Courses" class="Space">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Courses Undertaken</div>
            </div>
            <%
                if (id != null && courses != null && stud.student_id != "") {
                    out.println("<table id='courseCompareInfo' width=760px>");
                    out.println("<tr>");
                    out.println("<td class='labelcenter' width=\"3%\">Course ID</td>");
                    out.println("<td class='labelcenter' width=\"12%\">Name</td>");
                    out.println("<td class='labelcenter' width=\"5%\">Department</td>");
                    out.println("<td class='labelcenter' width=\"3%\">Credits</td>");
                    out.println("<td class='labelcenter' width=\"3%\">Semester</td>");
                    out.println("<td class='labelcenter' width=\"2%\">Year</td>");
                    out.println("<td class='labelcenter' width=\"2%\">Grade</td>");
                    out.println("</tr>");
                    int course_counter = 0;
                    for (SPCourseInfo c: courses) {
                        if(course_counter%2==1) {
                            out.println("<tr class='alt'>");
                        } else {
                            out.println("<tr>");
                        }
                        course_counter++;
                        out.println("<td><a href=\"coursePage.jsp?courseId=" + c.course_id + "&offeringId=" + c.offering_id + "\">" + c.course_id + "</a></td>");
                        out.println("<td>" + c.name + "</td>");
                        out.println("<td>" + c.department + "</td>");
                        out.println("<td>" + c.credits + "</td>");
                        out.println("<td>" + c.semester + "</td>");
                        out.println("<td>" + c.year + "</td>");
                        out.println("<td>" + c.grade + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
                else {
                    out.println("No such student profile exists");
                }
            %>
        <br/>
        </div>
        <!-- Writing Projects -->
        <div id="Projects" style="display: none" class="Space">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Projects Undertaken</div>
            </div>
            <%
                if (id != null && stud.student_id != "") {
                    out.println("<div id='profileElements'>");
                    for (SPProjectInfo p: projects) {
                        %>
                        <div class="courseContent">
                            <table class="courseInfo">
                                <tr>
                                    <td class="tleft">Project Name</td>
                                    <td><%=p.name%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Course ID</td>
                                <td><%="<a href='coursePage.jsp?courseId=" + p.course_id + "&offeringId=" + p.offering_id + "'>"%> <%=p.course_id%> </a></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Score</td>
                                    <td><%=p.score%></td>
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
                    out.println("No such student profile exists");
                }
            %>
        <br/>
        </div>
        <!-- Writing Work Profiles -->
        <div id="Work" style="display: none" class="Space">
            <div class="courseInfoHeader">
                <div class="courseTitle left">Work Experience</div>
            </div>
        <p>
            <%
                if (id != null && stud.student_id != "") {
                    out.println("<div id='profileElements'>");
                    int count = 0;
                    for (SPWorkInfo w: works) {
                        %>
                        <div class="courseContent">
                            <table class="courseInfo">
                                <tr>
                                    <td class="tleft">Name</td>
                                    <td><%=w.name%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Category</td>
                                    <td><%=w.category%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Period</td>
                                    <td><%=w.period%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Length</td>
                                    <td><%=w.length%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Stipend</td>
                                    <td><%=w.stipend%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Location</td>
                                    <td><%=w.location%></td>
                                </tr>
                                <tr>
                                    <td class="tleft">Description</td>
                                    <td><%=w.description%></td>
                                </tr>
                            </table>
                        </div>
                        <%
                    }
                    out.println("</div>");
                }
                else {
                    out.println("No such student profile exists");
                }
            %>
        </div>
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