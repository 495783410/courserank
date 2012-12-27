<%-- 
    Document   : submitPost
    Created on : 28 Dec, 2012, 12:45:42 AM
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
    String studentId = auth.getUser();
    String content = request.getParameter("postContent");
    long offeringId = Long.parseLong(request.getParameter("offeringId"));
    String privilegedUserStr = request.getParameter("privilegedUser");
    boolean privilegedUser = false;
    if (privilegedUserStr.equals("true")){
        privilegedUser = true;
    }
    String parentPostIdString = request.getParameter("parentPostId");
    long parentPostId = -1;
    if(parentPostIdString != null){
        parentPostId = Long.parseLong(parentPostIdString);
    }
    CoursePage tempPage = new CoursePage();
    tempPage.addPost(studentId, offeringId, content, privilegedUser, parentPostId);
    tempPage.closeConnection();
%>