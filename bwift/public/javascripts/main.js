$(document).ready(function() {

    $("#add-contact-button").click(function () {
        showOverlay();
    });
    $("#overlay-container .close").click(function () {
        hideOverlay();
    });
    function showOverlay() {
        $("#overlay").show();
        $("#overlay-container").show(100);
    }
    function hideOverlay() {
        $("#overlay").hide();
        $("#overlay-container").hide();
    }


});