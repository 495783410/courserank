window.onload = init;
var last_selected = 0;

function init() {
    initStarsIP();
}

function initStarsIP(){
    $("#instRating").stars("instRatingValue");
}

function hideAll() {
    document.getElementById("Courses").style.display = 'none';
    document.getElementById("Projects").style.display = 'none';
}

function showCourses() {
    hideAll();
    document.getElementById("Courses").style.display = 'block';
    navigate(0);
}

function showProjects() {
    hideAll();
    document.getElementById("Projects").style.display = 'block';
    navigate(1);
}

function navigate(a) {
    links = document.getElementById("sidebar").getElementsByTagName("ul")[0].getElementsByTagName("li");
    $(links[last_selected]).removeClass("sb_selected");
    $(links[a]).addClass("sb_selected");
    last_selected = a;
}