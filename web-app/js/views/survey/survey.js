$(document).ready(function () {
    $("#save-btn").click(function () {
        if ($('#ethnicity input:checked').length > 1) {
            $("#errorMessage").empty()
            $("#errorMessage").append("You may only select one checkbox for Ethnicity")
        }
        else {
            $("#errorMessage").empty()
            var form = document.getElementById('surveyForm')
            form.submit()
        }
    });

    $("#ask-me-later-btn").click(function () {
        /*window.location = "${createLink(controller: "survey", action: "completed")}"*/
        var href = $(this).attr("data-endpoint")
        window.location = href
    });

})