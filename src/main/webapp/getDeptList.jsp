<%-- 
    Document   : getDeptList
    Created on : 28 Dec, 2012, 12:43:00 AM
    Author     : rishabh
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="acad.db.courserank.dbis.*"%>
<%@page import="acad.db.courserank.dbis.CourseComparePage"%>

<%
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    PrintWriter out1 = response.getWriter();
    CourseComparePage temp = new CourseComparePage();
    ArrayList<String> deptList = temp.getDeptList();
    if (deptList != null && deptList.size() != 0){
        int i;
        for (i = 0; i < deptList.size() - 1; i++){
            out1.print(deptList.get(i) + "&&");
        }
        out.print(deptList.get(i));
    }
    temp.closeConnection();
%>
