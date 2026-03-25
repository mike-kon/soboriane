
function clickMainButton(div) {
    if ($(div).hasClass("button_press")) {
        return;
    }
    $(".button_press").each(function(i, elem) {
        $(elem).removeClass("button_press");
        $(elem).addClass("button_unpress")
    });
    $(div).removeClass("button_unpress");
    $(div).addClass("button_press");
    loadData($(div).attr("id"));
}

function loadData(tag) {
    console.log("Will be loaded data from server " + tag + ". Under construction");
}

function sendMessage() {
    console.log("Under construction");
}
