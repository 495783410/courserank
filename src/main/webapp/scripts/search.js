/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = mySearchFunction;

var tables = ['courses', 'student', 'instructor', 'projects', 'work_profile', 'posts'];
var maxTables = 6;
var lastSelected = 0;

var pageNo = 0;
var noOfResults = 12;
var maxPages = 0;

function mySearchFunction() {
    var str = document.getElementById("initialSearch").value;
    document.getElementById("search").value = str;
    SearchQuery();
}

function SearchQuery() {
    Search(lastSelected);
}

function Search(a) {
    links = document.getElementById("sidebar").getElementsByTagName("ul")[0].getElementsByTagName("li");
    $(links[lastSelected]).removeClass("sb_selected");
    $(links[a]).addClass("sb_selected");
    if(lastSelected!=a) {
        pageNo = 0;
        maxPages = 0;
    }
    lastSelected = a;
    query = document.getElementById("search").value;
    if(query!="") {
        getResults(query);
    }
}

function getResults(query) {
    var startingIndex = pageNo*noOfResults;
    var formParams = "query="+query + "&starting_index="+startingIndex+"&max_results="+noOfResults+"&table_no="+lastSelected;
    $.ajax({
        type: 'POST',
        url: 'search',
        async: true,
        data: formParams,
        success: function(data) {
            displayResults(data);
        }
    });
}

function displayResults(data) {
    //Process Data;
    var obj = jQuery.parseJSON(data);
    results = obj.results;
    entries = results.count;
    maxEntries = results.max_count;
    result_entries = results.entries;
    if(entries==0) {
        var1 = 0;
    } else {
        var1 = 1;
    }
    document.getElementById("body").innerHTML = "<div id='search_container'> </div>";
    document.getElementById("search_container").innerHTML = "<div id='search_header'><div id='search_counter'></div></div><div id='search_entries'></div><div id='search_pagination'></div>";
    document.getElementById("search_counter").innerHTML = "showing <b>" + (pageNo*noOfResults+var1) + "-" + (pageNo*noOfResults+entries) + "</b> out of <b>" + maxEntries + "</b>";
    entryText = "";
    for(i=0; i<entries; i++) {
        addSearchEntry(result_entries[i]);
    }
    if(entries==0) {
        document.getElementById("search_entries").innerHTML = "<img src='images/no-results.jpg' alt='no-results'/>";
    }
    generatePagination(maxEntries);
}

function addSearchEntry(entry) {
    //Process entry and extract title, url, description, table_name
    /*entry_title = "CS-101 Programming and Computer Utilization";
    entry_url = "http://localhost:8080/dbis/course.jsp?course_id=CS101";
    entry_description = "Topics include overview of high-level languages, introduction to C/C++ Library, basic data types, function definitions and declarations, conditional and iteration statement, array and string manipulation, recursive programming, introduction to searching and sorting and introduction to structures and pointers.";
    entry_table = "course";
    */
    entry_title = entry[0];
    entry_description = entry[1];
    entry_url = entry[2];
    entry_string = "<div class='search_entry'>";
    entry_string += "<div class='search_title'><a href='" + entry_url + "'>" + entry_title + "</a></div>";
    entry_string += "<div class='search_url'>" + entry_url + "</div>";
    entry_string += "<div class='search_description'>" + entry_description + "</div>"
    entry_string += "</div>";
    document.getElementById("search_entries").innerHTML += entry_string;
}

function generatePagination(max) {
    max = Math.floor(max);
    if(max%noOfResults==0) {
        maxPages = max/noOfResults;
    } else {
        maxPages = max/noOfResults + 1;
    }
    maxPages = Math.floor(maxPages);
    if(maxPages <= 1) {
        
    } else {
        search_pagination = document.getElementById("search_pagination");
        resultStr = "<ul>";
        resultStr += "<a href='javascript:searchPrev()'><li class='arrow arrowleft'></li></a>";
        for(i=1; i<=maxPages; i++) {
            if(i==pageNo) {
                resultStr += "<a href='javascript:MySearch(" + (i-1) + ")'><li class='pageid pageSelected'>" + i + "</li></a>";
            } else {
                resultStr += "<a href='javascript:MySearch(" + (i-1) + ")'><li class='pageid'>" + i + "</li></a>";
            }
        }
        resultStr += "<a href='javascript:searchNext()'><li class='arrow arrowright'></li></a>";
        resultStr += "</ul>";
        search_pagination.innerHTML = resultStr;
    }
}

function searchNext() {
    if(pageNo < maxPages) {
        pageNo = pageNo + 1;
        Search(lastSelected);
    }
}
function searchPrev() {
    if(pageNo > 0) {
        pageNo = pageNo - 1;
        Search(lastSelected);
    }
}
function MySearch(pno) {
    pageNo = pno;
    Search(lastSelected);
}
