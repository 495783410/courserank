<%-- 
    Document   : getCourseList
    Created on : 28 Dec, 2012, 12:42:45 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CourseComparePage.courseInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="acad.db.courserank.dbis.*"%>
<%@page import="acad.db.courserank.dbis.CourseComparePage"%>

<%
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    String deptName = request.getParameter("deptName");
    PrintWriter out1 = response.getWriter();
    CourseComparePage temp = new CourseComparePage();
    ArrayList<courseInfo> courseList = temp.getCourseList(deptName);
    if (courseList != null && courseList.size() != 0){
        int i;
        for (i = 0; i < courseList.size() - 1; i++){
            out1.print(courseList.get(i).courseId + "&&" + courseList.get(i).courseName + "&&");
        }
        out.print(courseList.get(i).courseId + "&&" + courseList.get(i).courseName);
    }
    temp.closeConnection();
%>
