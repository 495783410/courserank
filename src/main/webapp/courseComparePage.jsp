<%-- 
    Document   : courseComparePage
    Created on : 28 Dec, 2012, 12:36:12 AM
    Author     : rishabh
--%>

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
    header.addScript("scripts/jquery.flot.js");
    header.addScript("scripts/jquery.flot.barnumbers.js");
    header.addScript("scripts/courseComparePageJs.js");
    header.addStyleSheet("css/courseCompare.css");
    header.addStyleSheet("css/course.css");
    header.addStyleSheet("css/stars.css");

    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    out.println(header.toString());
%>
 
<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Courses compared 1</div>
                <ul id="sideBarLink1"></ul>
            <div id="linkset_header">Courses compared 2</div>
                <ul id="sideBarLink2"></ul>
    </div>

</div>

<div id="body" class="right">
    <table>
        <tr>
            <td width="400px">
                <select id="compareCourse_deptList1" name="courseCompare_dept1">
                    <option value="">Select 1st dept.</option>
                </select>
            </td>
            <td width="300px">
                <select id="compareCourse_deptList2" name="courseCompare_dept2">
                    <option value="">Select 2nd dept.</option>
                </select>    
            </td>           
        </tr>
        <tr>
            <td>
                <select id="compareCourse_courseList1" name="courseCompare_course1">
                    <option value="">Select 1st course</option>
                </select>
            </td>
            <td>
                <select id="compareCourse_courseList2" name="courseCompare_course2">
                    <option value="">Select 2nd course</option>
                </select>                
            </td>
        </tr>
        <tr>
            <td>
                <select id="compareCourse_offeringList1" name="courseCompare_offering1">
                    <option value="-1">Select offering</option>
                </select>                
            </td>
            <td>
                <select id="compareCourse_offeringList2" name="courseCompare_offering2">
                    <option value="-1">Select offering</option>
                </select>
            </td>                
        </tr>
    </table>

    <table id="courseCompareInfo">
        <tr>
            <td width="120px"class="label">ID</td>
            <td width="300px"></td>
            <td width="300px"></td>
        </tr>
        <tr class="alt">
            <td class="label">Name</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="label">Department</td>
            <td></td>
            <td></td>
        </tr>
        <tr class="alt">
            <td class="label">Description</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="label">Year</td>
            <td></td>
            <td></td>
        </tr>
        <tr class="alt">
            <td class="label">Semester</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="label">Credits</td>
            <td></td>
            <td></td>
        </tr>
        <tr class="alt">
            <td class="label">Rating</td>
            <td><span id="offeringRating1" class="stars"><span></span></span>
            </td>
            <td><span id="offeringRating2" class="stars"><span></span></span>
            </td>
        </tr>
        <tr>
            <td class="label">Word Load</td>
            <td></td>
            <td></td>
        </tr>
        <tr class="alt">
            <td class="label">Venue</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="label">Strength</td>
            <td></td>
            <td></td>
        </tr>
        <tr class="alt">
            <td class="label">Grades</td>
            <td></td>
            <td></td>
        </tr>
    </table>

    <div id="courseDia1" style="width:350px;height:300px;float:left"></div>
    <div id="courseDia2" style="width:350px;height:300px;float:right;margin-right: 20px"></div>

</div>


<%
    out.println(footer.toString());
%>
