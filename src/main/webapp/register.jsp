<%-- 
    Document   : register
    Created on : 28 Dec, 2012, 12:44:08 AM
    Author     : rishabh
--%>

<%@page import="acad.db.courserank.dbis.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Header header = new Header();
    Footer footer = new Footer();
    header.setMenu(1);
    out.println(header.toString());
    String error = request.getParameter("error");
%>

<div id="body" class="right">
    <br />
    <div>
        <div id="fb-root"></div>
        <script>
                window.fbAsyncInit = function() {
                  FB.init({
                        appId      : '510263249007057',
                        status     : true, 
                        cookie     : true,
                        xfbml      : true
                  });
                };
                (function(d){
                   var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
                   js = d.createElement('script'); js.id = id; js.async = true;
                   js.src = "//connect.facebook.net/en_US/all.js";
                   d.getElementsByTagName('head')[0].appendChild(js);
                 }(document));
          </script>
        <div>
            <div class="fb-registration" data-fields="[{'name':'name'}, {'name':'email'}, {'name':'username', 'description':'Username', 'type':'text'}, {'name':'password'}, {'name':'department', 'description':'Department', 'type':'select', 'options':{'Computer Science':'Computer Science','Aerospace':'Aerospace', 'Electrical':'Electrical', 'Chemical':'Chemical', 'Civil':'Civil', 'Mechanical':'Mechanical', 'Metallurgical':'Metallurgical', 'Humanities':'Humanities', 'Biology':'Biology', 'Mathematics':'Mathematics', 'Physics':'Physics'}}]" data-redirect-uri="<% out.print(global.getHost()); %>/authenticate"></div>
                <div class="declaration">* Course Rank password must be same as your LDAP Password</div>
                <div class="declaration" style="font-size:16px"><% if(error!=null) {out.println("* Error in registration. Try with someother Username"); } %></div>
        </div>
    </div>
</div>

<%
    out.println(footer.toString());
%>