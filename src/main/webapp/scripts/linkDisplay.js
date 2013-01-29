function initLinkDisplay(){
    var courseId = document.getElementById("courseId").value;
    var offeringId = document.getElementById("offeringId").value;
    var aLink = document.getElementById("coursePage.jsp?courseId="+ courseId +"&offeringId="+ offeringId);
    if (aLink != null){
        aLink.getElementsByTagName('li')[0].className = "sb_selected";
    }
}