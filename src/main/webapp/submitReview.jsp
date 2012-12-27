<%-- 
    Document   : submitReview
    Created on : 28 Dec, 2012, 12:46:01 AM
    Author     : rishabh
--%>

<%@page import="java.sql.Timestamp"%>
<%@page import="acad.db.courserank.dbis.*"%>
<%@page import="acad.db.courserank.dbis.CoursePage"%>

<%
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    String courseId = request.getParameter("courseId");
    String content = request.getParameter("reviewContent");
    String author = request.getParameter("author");
    boolean showAuthor;
    String authorName = "";
    if (author.equals("false")){
        showAuthor = false;
        authorName = "Anonymous";
    }
    else{
        showAuthor = true;
        authorName = auth.getUser();
    }
    
    CoursePage tempPage = new CoursePage();
    tempPage.addReview(courseId, content, showAuthor, authorName);
    tempPage.closeConnection();
%>