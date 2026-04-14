// Ширина, меньше которой включается механизм меню в кнопке
const MENU_BUTTON_WIDTH = 500;

function clickMainButton(div) {
    if ($(div).hasClass("button_press")) {
        return;
    }
    $(".button_press").each(function (i, elem) {
        $(elem).removeClass("button_press");
        $(elem).addClass("button_unpress")
    });
    $(div).removeClass("button_unpress");
    $(div).addClass("button_press");
    loadData($(div).attr("id"));
}

function loadData(tag) {
    console.log("Will be loaded data from server " + tag);
    $.post('api/menu', {"command": tag}, function (html) {
        $('#content').html(html);
    });
}

function sendMessage() {
    const msg = $('#inputMessageText');
    let message = $(msg).val();
    $(msg).val('');
    console.log(message);
    $.post('api/message', {"message":message});
}

function updateTextByWidth() {
    $('.dynamic-title').each(function () {
        updateTextByWidthElement(this);
    });
}

function updateTextByWidthElement(elem) {
    const dataname = ['data-full', 'data-medium', 'data-small'];
    const fullWidth = $(elem).width();
    const elems = $(elem).children('.dynamic-elem');
    const chLen = elems.length;
    let arr = new Array();
    // инициализация
    for (i = 0; i < chLen; i++) {
        arr[i] = 0;
    }
    // подбор
    tooWide = true;
    tooSmall = false;
    cur = 0;
    while (tooWide) {
        w = 0;
        for (i = 0; i < chLen; i++) {
            elems[i].textContent = elems[i].getAttribute(dataname[arr[i]]);
            w += $(elems[i]).width();
        }
        if (w < fullWidth) {
            tooWide = false;
        } else {
            arr[cur++]++;
            if (cur >= chLen) {
                cur = 0;
            }
            if (arr[0] >= dataname.length) {
                tooSmall = true;
                tooWide = false;
            }
        }
    }
    if (tooSmall) {
        for (i = 0; i < chLen; i++) {
            elems[i].textContent = '';
        }
    }
}

function menuClick(event) {
    const windowWidth = $(window).width();
    if (windowWidth < MENU_BUTTON_WIDTH) {
        event.stopPropagation();
        $('#menuContext').css('display', 'block');
    }
}

function menuUnClick() {
    const windowWidth = $(window).width();
    if (windowWidth < MENU_BUTTON_WIDTH) {
        $('#menuContext').css('display', '');
    }
}

function canvasClear() {
    menuUnClick();
}

function init() {
    $('#main').on('click', function () {
        canvasClear();
    });
    $('#menu').on('click', function (event) {
        menuClick(event);
    });
    updateTextByWidth();
    $('#messageCanvas').scrollTop(1000000);
}

$(function () {
    // Берем токен и имя заголовка из meta-тегов
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    // Глобально настраиваем все AJAX-запросы
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    loadData("btnMessage");
});


window.addEventListener('load', init);
window.addEventListener('resize', init);