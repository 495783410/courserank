window.onload = init_form;

function init_form() {
    initLinkDisplay();
    
    var avgWWfield = document.getElementById("avgWWfield");
    if (avgWWfield){
        avgWWfield.value = "";
    }
    var submitButton = document.getElementById("submitButton");
    if (submitButton){
        submitButton.disabled = false;
    }

    var selectedInstructor = document.getElementById("instructorId");
    if(selectedInstructor){
        var aLink = document.getElementById("instructorId" + selectedInstructor.value)
        aLink.getElementsByTagName('li')[0].className = "sb_selected";
    }
}

function submitFeedback(s_id, o_id, i_id) {
    if (document.getElementById("avgWWfield").value == "" || isNaN(document.getElementById("avgWWfield").value)) {
        alert("Please enter a valid number for Average Weekly Workload");
        return;
    }
    
    var cr=-1, dr=-1, ir=-1;
    for (var i=0; i<5; i++){
        if (document.getElementsByName("courseRating")[i].checked == true) {
            cr = i+1;
        }
    }
    if (cr == -1) {
        alert("Please Select a value for Course Rating");
        return;
    }
    
    for (i=0; i<5; i++){
        if (document.getElementsByName("instructorRating")[i].checked == true) {
            ir = i+1;
        }
    }
    if (ir == -1) {
        alert("Please Select a value for Instructor Rating");
        return;
    }
    
    for (i=0; i<5; i++){
        if (document.getElementsByName("difficultyRating")[i].checked == true) {
            dr = i+1;
        }
    }
    if (dr == -1) {
        alert("Please Select a value for Difficulty Rating");
        return;
    }
    document.getElementById("submitButton").disabled = true;
    //document.getElementById("submitButton").onclick = null;
    
    var formParams = "s_id=" + s_id + "&o_id=" + o_id + "&i_id=" + i_id + "&cr=" + cr + "&ir=" + ir + "&dr=" + dr + "&aww=" + document.getElementById("avgWWfield").value;
    $.ajax({
        type: 'POST',
        url: 'submitFeedback',
        async: true,
        data: formParams,
        success: function(data) {
            document.location.reload();
        }
    });
}