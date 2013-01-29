<%-- 
    Document   : deletePost
    Created on : 28 Dec, 2012, 12:38:01 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.*"%>
<%@page import="acad.db.courserank.dbis.CoursePage"%>

<%
    Authenticator auth = new Authenticator(request);
    boolean flag = auth.Login();
    if(!flag) {
        response.sendRedirect("login.jsp");
    }
    long postId = Long.parseLong(request.getParameter("postId"));
    long parentPostId = Long.parseLong(request.getParameter("parentPostId"));
    CoursePage tempPage = new CoursePage();
    tempPage.deletePost(postId, parentPostId);
    tempPage.closeConnection();
%>