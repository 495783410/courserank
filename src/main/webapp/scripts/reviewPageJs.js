/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = initSubmitReview;

function initSubmitReview(){
    initLinkDisplay();
    
    $("#courseRatingStars").stars("courseRating");

    document.getElementById("reviewSubmit").onclick = insertReview;
}

function insertReview(){
    var ajaxRequest;  // The variable that makes Ajax possible!

    try{
            // Opera 8.0+, Firefox, Safari
            ajaxRequest = new XMLHttpRequest();
    } catch (e){
            // Internet Explorer Browsers
            try{
                    ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                    try{
                            ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e){
                            // Something went wrong
                            alert("Your browser broke!");
                            return false;
                    }
            }
    }

    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
            document.location.reload();
        }
    }

    var content = document.getElementById("reviewContent").value;
    var courseId = document.getElementById("courseId").value;
    var author = document.getElementById("author").checked;
    ajaxRequest.open("GET", "submitReview.jsp?reviewContent=" + content + "&courseId=" + courseId + "&author=" + author, true);
    ajaxRequest.send(null);
}

function deleteReview(postId, parentPostId){
    var ajaxRequest;  // The variable that makes Ajax possible!

    try{
            // Opera 8.0+, Firefox, Safari
            ajaxRequest = new XMLHttpRequest();
    } catch (e){
            // Internet Explorer Browsers
            try{
                    ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                    try{
                            ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e){
                            // Something went wrong
                            alert("Your browser broke!");
                            return false;
                    }
            }
    }

    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
           document.location.reload();
        }
    }

    ajaxRequest.open("GET", "deletePost.jsp?postId=" + postId + "&parentPostId=" + parentPostId, true);
    ajaxRequest.send(null);
}
