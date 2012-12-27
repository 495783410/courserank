/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = initAll;

function initAll(){
    document.getElementById("postSubmit").onclick = function(){insertPost(null)};
}

function insertPost(parent_post_id){
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

    var content;
    if (parent_post_id == null){
        content = document.getElementById("postContent").value;
        document.getElementById("postContent").value = "";
    }
    else{
        content = document.getElementById("postContent" + parent_post_id).value;
        document.getElementById("postContent" + parent_post_id).value = "";
    }
    var offeringId = document.getElementById("offeringId").value;
    var parentPostId = parent_post_id;
    if (parent_post_id == null){
        ajaxRequest.open("GET", "submitPost.jsp?postContent=" + content + "&offeringId=" + offeringId, true);    
    }
    else{
        ajaxRequest.open("GET", "submitPost.jsp?postContent=" + content + "&offeringId=" + offeringId + "&parentPostId=" + parentPostId , true);    
    }
    ajaxRequest.send(null);
}

