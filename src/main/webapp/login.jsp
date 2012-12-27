<%-- 
    Document   : login
    Created on : 28 Dec, 2012, 12:43:44 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    header.setMenu(1);
    header.addStyleSheet("css/login.css");
    out.println(header.toString());
%>

<style type="text/css">
    #login_link {
        color: #D14836;
    }
    
    #login_link:hover {
        text-decoration: underline;
    }
</style>

<div id="login-body">
    <div id="login-info" class="left">
        <h2>CourseRank IIT Bombay</h2>
        <p>
            Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>
        <p>It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        </p>
    </div>
    
    <div id="login-box" class="right">
            <div style="font-size:20px"> Login </div>
            <hr /><br />
        <form method="post" action="authenticate">
            <div><b>Username</b></div>
            <div><input type="text" name="username" class="inputField"/></div>
            <br /><br />
            <div><b>Password</b></div>
            <div><input type="password" name="password" class="inputField"/></div>
            <br /><br />
            <input type="hidden" name="src" value="login" />
            <div style="overflow: auto">
                <input type="submit" class ="left" value="Sign In" id="submit_button" />
                <div class="right"><a id="login_link" href="register.jsp"> Not Registered Yet? </a><br />Its easy to get Registered</div>
            </div>
        </form>
            <%
                if(request.getParameter("error") != null) {
                    out.println("<br /><br /><span style='color:red;'>Please enter correct credentials</span>");
                }
            %>
    </div>
    
</div>

<%
    out.println(footer.toString());
%>