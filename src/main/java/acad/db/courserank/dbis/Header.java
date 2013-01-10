package acad.db.courserank.dbis;

/**
 *
 * @author rishabh
 */
public class Header {
    String head_start_text;
    String head_end_text;
    String scripts;
    String title;
    String styleSheets;
    
    String superheader_start_text;
    String superheader_end_text;
    
    String header_start_text;
    String picurl, picalt;
    String header_mid_text; //url will be before header_mid_text and alt will be just after it
    String header_end_text1;
    String header_end_text;
    
    String menubar_start_text;
    String menus[];
    int menuNum;
    String menubar_end_text;
    
    String onloadJS;
    String searchStr;
    
    public Header() {
        head_start_text = "<html> <head>";
        head_end_text = "</head>";
        styleSheets = "<link rel='stylesheet' href='css/index.css' type='text/css' />";
        //scripts = "<script type='text/javascript' src='scripts/index.js'></script>";
        
        superheader_start_text = "<body><div id='super-header'>";
        superheader_end_text = "</div>  <div>"; //Also taken the starting of body which is closed in the footer class
        
        header_start_text = "<div id='header' class='center'> <div id='header-image' class='left'><div id='header-image-wrapper'><img src='";
        header_mid_text = "' alt='";
        header_end_text = "' /></div></div>";
	header_end_text += "<div id='search-bar' class='right'><form action='";
        header_end_text1 = "'><input id='search' type='search' name='query' placeholder='Search Anything' /></form></div>";
	header_end_text1 += "<div id='logo'><div style='overflow:hidden'><img src='images/course_rank.png' alt='Course Rank' /></div></div></div>";
        
        menubar_start_text = "<div id='menubar'><div class='center'>";
        menubar_end_text = "</div></div>";
        menuNum = 0;
        menus = new String[2];
        menus[0] = "<a href='authenticate?src=logout'><div class='menu right'>Logout</div></a>";
	menus[0] += "<a href='studentProfile.jsp'><div class='menu right'>Profile</div></a>";
        menus[0] += "<a href='index.jsp'><div class='menu right'>Home</div></a>";
        menus[1] = "<a href='register.jsp'><div class='menu right'>Register</div></a>";
        menus[1] += "<a href='login.jsp'><div class='menu right'>Login</div></a>";
        picurl = "images/defaultImage.jpg";
        picalt = "Default Image";
        title = "<title>CourseRank</title>";
        
        onloadJS = "<div id='main-body'>";
        searchStr = "search.jsp";
    }
    
    public void addStyleSheet(String url) {
        styleSheets += "<link rel='stylesheet' href='" + url + "' type='text/css' />";
    }
    
    public void addScript(String url) {
        scripts += "<script type='text/javascript' src='" + url + "'></script>";
    }
    
    public void setPicUrl(String url, String alt) {
        picurl = url;
        picalt = alt;
    }
    
    public void setMenu(int n) {
        menuNum = n;
    }
    
    public void setTitle(String t) {
        title = "<title>" + t + "</title>";
    }
    
    public void setSearchTarget(String st) {
        searchStr = st;
    }
    
    @Override
    public String toString() {
        String result = head_start_text + title + scripts + styleSheets + head_end_text;
        result += superheader_start_text;
        result += header_start_text + picurl + header_mid_text + picalt + header_end_text + searchStr + header_end_text1;
        result += menubar_start_text + menus[menuNum] + menubar_end_text;
        result += superheader_end_text + onloadJS;
        return result;
    }
    
    public void setOnloadJS(String str) {
        onloadJS = "<div id='main-body' onload='" + str + "'>";
    }
}

/*
        <div id="super-header">
		<div id="header" class="center">
			<div id="header-image" class="left">
				<div id="header-image-wrapper"><img src="images/saif_hasan.jpg" alt="IIT Bombay"/></div>
			</div>
			<div id="search-bar" class="right">
				<input id="search" type="search" placeholder="Search Anything" />
			</div>
			<div id="logo">
				<div style="overflow:hidden"><img src="images/course_rank.png" alt="Course Rank" /></div>
			</div>
		</div>
		<div id="menubar">
			<div class="center">
				<div class="menu right">Account</div>
				<div class="menu right">Profile</div>
				<div class="menu right">Home</div>
			</div>
		</div>
	</div>

        <div>
            <div id="main-body" class=>
*/