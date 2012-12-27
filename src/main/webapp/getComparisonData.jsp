<%-- 
    Document   : getComparisonData
    Created on : 28 Dec, 2012, 12:42:16 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.CourseComparePage.comparisionInfo"%>
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
    String offeringIdStr = request.getParameter("offeringId");
    
    if (courseId != null && offeringIdStr != null){
        long offeringId = Long.parseLong(offeringIdStr);
        
        PrintWriter out1 = response.getWriter();
        CourseComparePage temp = new CourseComparePage();
        comparisionInfo data = temp.getComparisionData(courseId, offeringId);
        if (data != null){
            out1.print(data.courseId + "&&" + data.courseName + "&&" + data.courseDepartment + "&&" + data.courseDescription + "&&");
            out1.print(data.year + "&&" + data.semester + "&&" + data.credits + "&&" + data.offeringRating + "&&");
            out1.print(data.offeringWorkLoad + "&&" + data.venue + "&&" + data.strength + "&&");
            for (int i = 0; i < 10; i++){
                out1.print(data.grades[i] + "&&");
            }
            for (int i = 0; i < data.instructorList.size(); i++){
                out1.print(data.instructorList.get(i).instructorId + "&&");
                out1.print(data.instructorList.get(i).instructorName + "&&");
                out1.print(data.instructorList.get(i).instructorRating + "&&");
            }
        }
        temp.closeConnection();
    }
%>