$.fn.stars = function(ratingSource) {
    return $(this).each(function() {
        // Get the value
        var valBox = document.getElementById(ratingSource);
        var val;
        if (valBox){
            val = parseFloat(valBox.value);
        }
        // Make sure that the value is in 0 - 5 range, multiply to get width
        var size = Math.max(0, (Math.min(5, val))) * 16;
        // Create stars holder
        var $span = $('<span />').width(size);
        // Replace the numerical value with stars
        $(this).html($span);
    });
}


