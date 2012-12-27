function submitRegistration(s_id, o_id, grade) {
    
    var formParams = "s_id=" + s_id + "&o_id=" + o_id + "&grade=" + grade;
    $.ajax({
        type: 'POST',
        url: 'registerOffering',
        async: true,
        data: formParams,
        success: function(data) {
            alert(data == "true" ? "Operation Successfull" : "Operation UNSUCCESSFUL");
            document.location.reload();
        }
    });
}