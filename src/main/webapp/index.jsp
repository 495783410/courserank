<%@page import="java.util.ArrayList"%>
<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    header.addScript("scripts/jquery.js");
    header.addScript("scripts/discussionPageJs.js");
    header.addScript("scripts/linkDisplay.js");
    header.addStyleSheet("css/posts.css");
    header.addScript("scripts/stars.js");
    header.addStyleSheet("css/stars.css");
    header.addStyleSheet("css/course.css");
    out.println(header.toString());
    Authenticator auth = new Authenticator(request);
    if(!auth.Login()) {
        response.sendRedirect("login.jsp");
    }
%>

<div id="sidebar" class="left">
    <div class="linkset">
            <div id="linkset_header">Course</div>
            <ul>
                <%  
                    String offeringId=request.getParameter("id");
                    String courseId = request.getParameter("course_id");
                    String Student_id = auth.getUser(); 
                    Course_reg c =new Course_reg(Student_id);
                    ArrayList<String> links = new ArrayList<String>();
                    links = c.result();
                    for(String l:links)
                        out.println(l);
                %>
            </ul>
    </div>
            <div class="linkset">
                <div id="linkset_header">Account</div>
                <ul>
                    <a href="editProfile.jsp"><li>Edit Profile</li></a>
                </ul>
            </div>
</div>

<div id="body" class="right">
    <ul class="posts">
<%
    CoursePage con = new CoursePage();
    if (offeringId != null){
        ArrayList<CoursePage.postInfo> postList = CoursePage.getPosts(Long.parseLong(offeringId), false);
        CoursePage.closeConnection();
        if (postList != null){
            for (int i = 0; i < postList.size();){
                CoursePage.postInfo elem = postList.get(i++);
                if (elem.postId == elem.parentPostId){
%>  
                <hr />
                <li class="Post">
                    <div class="post">
                        <div class="postHeader">
                            <a href="studentProfile.jsp?id=<%=elem.studentId%>">
                                <div class="postHeaderName"><%=elem.studentName%></div>
                            </a>
                            <a href="postPage.jsp?courseId=<%=courseId%>&offeringId=<%=offeringId%>&postId=<%=elem.postId%>">
                                <div class="postHeaderTime"><%=elem.mytime%></div>
                            </a>
                        </div>
                        <div class="postContent"><%=elem.content%></div>
                        
<%
                    if (elem.studentId.equals(Student_id)){
%>                                                       
                        <a id="deletePost<%=elem.postId%>" href="javascript:deletePost(<%=elem.postId%>, <%=elem.parentPostId%>)">
                            <div class="deletePostComment"></div>
                        </a>
                    </div>
<%
                    }
%>
                    <ul class="comments">
<%
                }
           
                while (i < postList.size()){
                    CoursePage.postInfo childElem = postList.get(i++);
                    if (childElem.parentPostId != elem.postId){
                        i--;
                        break;
                    }
%>
                        <hr/>
                        <li class="comment">
                            <div>
                                <a href="studentProfile.jsp?id=<%=childElem.studentId%>">
                                    <span class="commentHeaderName"><%=childElem.studentName%></span>
                                </a>
                                <%=childElem.content%>
                            </div>
                            <div class="commentTime"><%=childElem.mytime%></div>

<%
                        if (childElem.studentId.equals(Student_id)){
%>
                            <a id="deletePost<%=childElem.postId%>" href="javascript:deletePost(<%=childElem.postId%>, <%=childElem.parentPostId%>)">
                                <div class="deletePostComment"></div>
                            </a>
<%
                        }
%>
                        </li>
<%            
                }
%>  
                      <%--  <hr />
                        <div id="commentBox">
                            <textarea class="commentTextArea" id="postContent<%=elem.postId%>" placeholder="Add a new Comment"></textarea>
                            <a href="javascript:insertPost(<%=elem.postId%>)">
                                <div name="<%=elem.postId%>" class="SubmitButton">Post</div>
                            </a>
                        </div>--%>

                    </ul>     
                </li>
<%
            }
        }
    }
%>    
        <hr />
        </ul>  
</div>

<%
    out.println(footer.toString());
%>
