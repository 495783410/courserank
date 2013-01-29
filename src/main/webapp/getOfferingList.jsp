<%-- 
    Document   : getOfferingList
    Created on : 28 Dec, 2012, 12:43:19 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CourseComparePage.offeringInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="acad.db.courserank.dbis.*"%>
<%@page import="acad.db.courserank.dbis.CourseComparePage"%>

<%
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }

    String courseId = request.getParameter("courseId");
    PrintWriter out1 = response.getWriter();
    CourseComparePage temp = new CourseComparePage();
    ArrayList<offeringInfo> offeringList = temp.getOfferingList(courseId);
    if (offeringList != null && offeringList.size() != 0){
        int i;
        for (i = 0; i < offeringList.size() - 1; i++){
            out1.print(offeringList.get(i).offeringId + "&&" + offeringList.get(i).year + "&&" + offeringList.get(i).semester + "&&");
        }
        out.print(offeringList.get(i).offeringId + "&&" + offeringList.get(i).year + "&&" + offeringList.get(i).semester);
   }
   temp.closeConnection();
%>