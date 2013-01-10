window.onload = init;
last_selected = 0;

function init() {
    showBasic();
    document.getElementById("submitButtonBasic").disabled = false;
    document.getElementById("submitButtonPassword").disabled = false;
    document.getElementById("submitButtonGrades").disabled = false;
    document.getElementById("submitButtonProjects").disabled = false;
}

function hideAll() {
    document.getElementById("Basic").style.display = 'none';
    document.getElementById("Courses").style.display = 'none';
    document.getElementById("Projects").style.display = 'none';
}

function showBasic() {
    hideAll();
    document.getElementById("Basic").style.display = 'block';
    navigate(0);
}

function showCourses() {
    hideAll();
    document.getElementById("Courses").style.display = 'block';
    navigate(1);
}

function showProjects() {
    hideAll();
    document.getElementById("Projects").style.display = 'block';
    navigate(2);
}

function submitBasic(s_id) {
    var name = document.getElementsByName("StudentName")[0].value;
    var cpi = document.getElementsByName("CPI")[0].value;
    console.log("submitBasic called" + s_id);
    if (name.length < 1 || name.length > 30) {
        alert("Name must be non-empty and less than 30 characters. You entered " + name);
        return;
    }
    if (cpi < 0 || cpi > 10) {
        alert("CPI must be between 0 and 10. You entered " + cpi);
        return;
    }
    
    document.getElementById("submitButtonBasic").disabled = true;
    document.getElementById("submitButtonPassword").disabled = true;
    document.getElementById("submitButtonGrades").disabled = true;
    document.getElementById("submitButtonProjects").disabled = true;
    
    var formParams = "s_id=" + s_id + "&name=" + name + "&cpi=" + cpi;
    $.ajax({
        type: 'POST',
        url: 'editProfileBasic',
        async: true,
        data: formParams,
        success: function(data) {
            alert(data == "true" ? "Information Updated Succesfully" : "Information Update UNSUCCESFULL");
            document.location.reload();
        }
    });
}

function submitPassword(s_id) {
    var curPass = document.getElementsByName("currentPassword")[0].value;
    var newPass1 = document.getElementsByName("newPassword1")[0].value;
    var newPass2 = document.getElementsByName("newPassword2")[0].value;
    if (newPass1 != newPass2) {
        alert("The 2 Passwords are not equal. Please retype your passwords.");
        return;
    }
    if (newPass1.length <= 0 || newPass2.length <= 0) {
        alert("New Password must be non-empty");
        return;
    }
    if (newPass1 == curPass) {
        alert("You have entered the same value in Current Password and New Password. Please type a new password.");
        return;
    }
    
    document.getElementById("submitButtonBasic").disabled = true;
    document.getElementById("submitButtonPassword").disabled = true;
    document.getElementById("submitButtonGrades").disabled = true;
    document.getElementById("submitButtonProjects").disabled = true;
    
    var formParams = "s_id=" + s_id + "&curpass=" + curPass + "&newpass=" + newPass1;
    $.ajax({
        type: 'POST',
        url: 'editProfilePassword',
        async: true,
        data: formParams,
        success: function(data) {
            alert(data == "true" ? "Succesfully updated Password" : "Password Change Failed");
            document.location.reload();
        }
    });
}

function submitGrades(s_id, i) {
    
    document.getElementById("submitButtonBasic").disabled = true;
    document.getElementById("submitButtonPassword").disabled = true;
    document.getElementById("submitButtonGrades").disabled = true;
    document.getElementById("submitButtonProjects").disabled = true;
    
    var formParams = "s_id=" + s_id + "&num=" + i;
    for (var j=0; j<i; j++) {
        var o_id = document.getElementById(j+"grade").getElementsByTagName("select")[0].name;
        var grade = document.getElementById(j+"grade").getElementsByTagName("select")[0].value;
        formParams += "&o" + j + "=" + o_id + "&g" + j + "=" + grade;
    }
    $.ajax({
        type: 'POST',
        url: 'editProfileGrades',
        async: true,
        data: formParams,
        success: function(data) {
            alert(data == "true" ? "Succesfully updated Grades" : "Grades Update did NOT complete succesfully");
            document.location.reload();
        }
    });
}

function submitProjects(s_id, i) {
    
    document.getElementById("submitButtonBasic").disabled = true;
    document.getElementById("submitButtonPassword").disabled = true;
    document.getElementById("submitButtonGrades").disabled = true;
    document.getElementById("submitButtonProjects").disabled = true;
    
    var formParams = "s_id=" + s_id + "&num=" + i;
    
    for (var j=0; j<i; j++) {
        var p_id = document.getElementById(j+"project").getElementsByTagName("input")[0].name;
        var score = document.getElementById(j+"project").getElementsByTagName("input")[0].value;
        if (score < 0 || score > 100) {
            alert("Score must be a percentage between 0 and 100. You entered " + score + " for project index " + j);
            document.getElementById("submitButtonBasic").disabled = false;
            document.getElementById("submitButtonPassword").disabled = false;
            document.getElementById("submitButtonGrades").disabled = false;
            document.getElementById("submitButtonProjects").disabled = false;
            return;
        }
        formParams += "&p" + j + "=" + p_id + "&s" + j + "=" + score;
    }
    $.ajax({
        type: 'POST',
        url: 'editProfileProjects',
        async: true,
        data: formParams,
        success: function(data) {
            alert(data == "true" ? "Succesfully updated Grades" : "Grades Update did NOT complete succesfully");
            document.location.reload();
        }
    });
}

function navigate(a) {
    links = document.getElementById("sidebar").getElementsByTagName("ul")[0].getElementsByTagName("li");
    $(links[last_selected]).removeClass("sb_selected");
    $(links[a]).addClass("sb_selected");
    last_selected = a;
}