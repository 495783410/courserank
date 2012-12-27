window.onload = initAll;

function initAll(){
    
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

    populateDeptList(ajaxRequest);
    
    document.getElementById("compareCourse_deptList1").onchange = function(){
        populateCourses(ajaxRequest, 1);
    }
    document.getElementById("compareCourse_deptList2").onchange = function(){
        populateCourses(ajaxRequest, 2);
    }    

    document.getElementById("compareCourse_courseList1").onchange = function(){
        populateOfferings(ajaxRequest, 1);
    }
    document.getElementById("compareCourse_courseList2").onchange = function(){
        populateOfferings(ajaxRequest, 2);
    }

    document.getElementById("compareCourse_offeringList1").onchange = function(){
        displayOfferingInfo(ajaxRequest, 1);
    }
    document.getElementById("compareCourse_offeringList2").onchange = function(){
        displayOfferingInfo(ajaxRequest, 2);
    }
}

function draw(givenData, placeholderId){
    var placeholder = $("#" + placeholderId);
    
    var data = [
        {
            label: "grading stats", 
            data: givenData,
            lines: {show: false, steps: false },
            bars: {
                show: true,
                barWidth: 0.8, 
                align: 'center',
                fill: true,
                showNumbers: true,
                fillColor: { colors: [ {opacity: 0.8 }, { opacity: 0.1} ] }
            }
        }
    ];


    var options = {
        colors: ["blue"],
        stack: 0,
        grid: {
            clickable: true
        },
        xaxis: {
            ticks: [[1,'AP'], [2,'AA'], [3,'AB'], [4,'BB'], [5,'BC'], [6, 'CC'], [7, 'CD'], [8, 'DD'], [9, 'FR'], [10, 'XX']],
            autoscaleMargin: 0.01
        },
        yaxis: {
            min: 0,
            minTickSize: 2,
            tickSize : 2,
            autoscaleMargin: 0.1
        }
    };

    var plot = $.plot(placeholder, data, options);
}

function populateDeptList(ajaxRequest){
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
            var values = ajaxRequest.responseText.split("&&");
            var deptList1 = document.getElementById("compareCourse_deptList1");            
            var deptList2 = document.getElementById("compareCourse_deptList2");
            
            for(var i = 0; i < values.length; i++){
                deptList1.options[deptList1.length] = new Option(values[i], values[i]);
                deptList2.options[deptList2.length] = new Option(values[i], values[i]);
            }
        }
    }

    //Replace the folllowing command
    ajaxRequest.open("GET", "getDeptList.jsp", true);
    ajaxRequest.send(null);  
}

function populateCourses(ajaxRequest, num){
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
            var values = ajaxRequest.responseText.split("&&");
            var courseList = document.getElementById("compareCourse_courseList" + num);            

            for (var i = courseList.length - 1; i > 0; i--){
                courseList.options[i] = null;
            }

            var offeringList = document.getElementById("compareCourse_offeringList" + num);
            for (var i = offeringList.length -1; i > 0; i--){
                offeringList.options[i] = null;
            }

            if (values.length != 1){            
                for(var i = 0; i < values.length; i+=2){
                    courseList.options[courseList.length] = new Option(values[i]+" " + values[i+1], values[i]);
                }
            }
        }
    }

    var deptName = document.getElementById("compareCourse_deptList" + num).value;
    
    //Replace the folllowing command
    ajaxRequest.open("GET", "getCourseList.jsp?deptName=" + deptName, true);
    ajaxRequest.send(null);      
}

function populateOfferings(ajaxRequest, num){
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
            var values = ajaxRequest.responseText.split("&&");
            var offeringList = document.getElementById("compareCourse_offeringList" + num);            

            for (var i = offeringList.length - 1; i > 0; i--){
                offeringList.options[i] = null;
            }

            if (values.length != 1){            
                for(var i = 0; i < values.length; i+=3){
                    offeringList.options[offeringList.length] = new Option(values[i]+" " + values[i+1] + " " + values[i+2], values[i]);
                }
            }
        }
    }

    var courseId = document.getElementById("compareCourse_courseList" + num).value;
    
    //Replace the folllowing command
    ajaxRequest.open("GET", "getOfferingList.jsp?courseId=" + courseId, true);
    ajaxRequest.send(null);      
}

$.fn.stars = function(ratingValue) {
    return $(this).each(function() {
        var size = Math.max(0, (Math.min(5, ratingValue))) * 16;
        // Create stars holder
        var $span = $('<span />').width(size);
        // Replace the numerical value with stars
        $(this).html($span);
    });
}


function displayOfferingInfo(ajaxRequest, num){
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4){
            var values = ajaxRequest.responseText.split("&&");
            
            var sideBar = document.getElementById("sideBarLink" + num);
            sideBar.innerHTML = "<a href=coursePage.jsp?courseId=" + values[0] + "><li>" + values[1] + "</li></a>";
            
            var output = document.getElementById("courseCompareInfo").getElementsByTagName('tr');
            var tableEntry;
            var data = [];
            for (var i = 0; i < 11; i++){
                tableEntry = output[i].getElementsByTagName('td')[num];
                if (i == 7){
                    $("#offeringRating" + num).stars(values[i]);
                }
                else{
                    tableEntry.innerHTML = values[i];
                }
            }
            
            tableEntry = output[i].getElementsByTagName('td')[num];
            var gradeOutput = "";
            gradeOutput += "<div id='gradingStats' class='left'>";
            gradeOutput += "<ul class='gradingStats'>";
            gradeOutput += "<li><b>AP:</b>&nbsp;<span>" + values[11] + "</span></li>";                            
            gradeOutput += "<li><b>AA:</b>&nbsp;<span>" + values[12] + "</span></li>";                            
            gradeOutput += "<li><b>AB:</b>&nbsp;<span>" + values[13] + "</span></li>";                            
            gradeOutput += "<li><b>BB:</b>&nbsp;<span>" + values[14] + "</span></li>";                            
            gradeOutput += "<li><b>BC:</b>&nbsp;<span>" + values[15] + "</span></li>";                            
            gradeOutput += "<li><b>CC:</b>&nbsp;<span>" + values[16] + "</span></li>";                            
            gradeOutput += "<li><b>CD:</b>&nbsp;<span>" + values[17] + "</span></li>";                            
            gradeOutput += "<li><b>DD:</b>&nbsp;<span>" + values[18] + "</span></li>";                            
            gradeOutput += "<li><b>FR:</b>&nbsp;<span>" + values[19] + "</span></li>";                            
            gradeOutput += "<li><b>XX:</b>&nbsp;<span>" + values[20] + "</span></li>";                            
            gradeOutput += "</ul>";
            gradeOutput += "</div>";
            tableEntry.innerHTML = gradeOutput;
            
            for (var i = 11; i < 21; i++){
                
                data.push([i-10, values[i]]);
            }
            for (var i = 21; i < values.length; i++){
                
            }

            var placeholder = "courseDia" + num;
            draw(data, placeholder);
        }
    }
    
    var offeringId = document.getElementById("compareCourse_offeringList" + num).value;
    var courseId = document.getElementById("compareCourse_courseList" + num).value;
        
    //Replace the folllowing command
    ajaxRequest.open("GET", "getComparisionData.jsp?courseId=" + courseId + "&&offeringId=" + offeringId , true);
    ajaxRequest.send(null);          
}