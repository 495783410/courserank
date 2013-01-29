<%-- 
    Document   : search
    Created on : 28 Dec, 2012, 12:44:55 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    header.addScript("scripts/search.js");
    header.addScript("scripts/jquery.min.js");
    out.println(header.toString());
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }
    header.setOnloadJS("javascript:SearchQuery()");
    String query = "";
    if(request.getParameter("query") != null) {
        query = request.getParameter("query");
    }
    
%>
<input type="hidden" id="initialSearch" value="<%=query%>" />

<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Search For</div>
            <ul>
                <a href="javascript:Search(0)"><li class="sb_selected">Everything</li></a>
                <a href="javascript:Search(1)"><li>Courses</li></a>
                <a href="javascript:Search(2)"><li>Students</li></a>
                <a href="javascript:Search(3)"><li>Instructors</li></a>
                <a href="javascript:Search(4)"><li>Projects</li></a>
                <a href="javascript:Search(5)"><li>Work Profile</li></a>
                <a href="javascript:Search(6)"><li>Posts</li></a>
            </ul>
    </div>
</div>

<div id="body" class="right">
    
    <div id="search_container">
        <div id="search_header">
            <div id="search_counter">showing <b>1-10</b> out of <b>100</b></div>
        </div>
        <div id="search_entries">
            <div class="search_entry">
                <div class="search_title"><a href="#">CS101 Computer Programming and Utilization</a></div>
                <div class="search_url"> <% out.print(global.getHost()); %>/course.jsp?course_id=CS101</div>
                <div class="search_description">Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.</div>
            </div>
        </div>
        <div id="search_pagination">
            <ul>
                <li class="arrow arrowleft"></li>
                <li class="pageid">1</li>
                <li class="pageid">2</li>
                <li class="arrow arrowright"></li>
            </ul>
        </div>
    </div>
    
    
</div>

<%
    out.println(footer.toString());
%>


<!--
<div id="search_container">
        <div id="search_header">
            <div id="search_counter">showing <b>1-10</b> out of <b>100</b></div>
        </div>
        <div id="search_entries">
            <div class="search_entry">
                <div class="search_title"><a href="#">CS101 Computer Programming and Utilization</a></div>
                <div class="search_url">http://localhost:8080/dbis/course.jsp?course_id=CS101</div>
                <div class="search_description">Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.</div>
            </div>

            <div class="search_entry">
                <div class="search_title"><a href="#">CS101 Computer Programming and Utilization</a></div>
                <div class="search_url">http://localhost:8080/dbis/course.jsp?course_id=CS101</div>
                <div class="search_description">Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.</div>
            </div>

            <div class="search_entry">
                <div class="search_title"><a href="#">CS101 Computer Programming and Utilization</a></div>
                <div class="search_url">http://localhost:8080/dbis/course.jsp?course_id=CS101</div>
                <div class="search_description">Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.</div>
            </div>

            <div class="search_entry">
                <div class="search_title"><a href="#">CS101 Computer Programming and Utilization</a></div>
                <div class="search_url">http://localhost:8080/dbis/course.jsp?course_id=CS101</div>
                <div class="search_description">Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.</div>
            </div>
        </div>
        <div id="search_pagination">
            
        </div>
    </div>
-->