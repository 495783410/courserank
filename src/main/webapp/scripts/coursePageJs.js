window.onload = initCoursePage;

function initCoursePage(){
    initLinkDisplay();
    
    //Functions to draw stars
    $("#offeringRatingStars").stars("offeringRating");
    $("#courseRatingStars").stars("courseRating");
    
    draw("gradingStatsImg");
}



function draw(placeholderId){
    
    var dataItems = [];
    var gradeData = document.getElementById("gradingStats").getElementsByTagName('span');
    for (var i = 1; i < 11; i++){
        dataItems.push([i, gradeData[i-1].innerHTML]);
    }
    
    var placeholder = $("#" + placeholderId);
    
    var data = [
        {label: "grading stats", data: dataItems},
    ];

    var options = {
        colors: ["blue"],
        series: {
            stack: 0,
            lines: {show: false, steps: false },
            bars: {
                show: true,
                barWidth: 0.9, 
                align: 'center',
                fill: true,
                showNumbers: true,
                fillColor: { colors: [ {opacity: 0.8 }, { opacity: 0.1} ] }
            },
            grid: {
                clickable: true
            }
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
    
    $.each(plot.getData()[0].data, function(i, el){
        var o = plot.pointOffset({x: el[0], y: el[1]});
        $('<div class="data-point-label">' + el[1] + '</div>').css( {
            position: 'absolute',
            left: o.left - 4,
            top: o.top - 20,
            display: 'none'
        }).appendTo(plot.getPlaceholder()).fadeIn('slow');
    });
}



function alterSubscription() {
    if(document.getElementById("subscribeText").innerHTML=="Subscribe") {
        var ele2 = document.getElementById("subscribeText");
        ele2.innerHTML = "Unsubscribe";
        ele = document.getElementById("courseSubscription");
        $(ele).removeClass("Subscribed");
        $(ele).removeClass("UnSubscribed");
        $(ele).addClass("UnSubscribed");
    } else {
        ele2 = document.getElementById("subscribeText");
        ele2.innerHTML = "Subscribe";
        ele = document.getElementById("courseSubscription");
        $(ele).removeClass("Subscribed");
        $(ele).removeClass("UnSubscribed");
        $(ele).addClass("Subscribed");
    }
    var o_id = document.getElementById("offeringId").value;
    var s_id = document.getElementById("studentId").value;
    submitRegistration(s_id, o_id,"");
}

