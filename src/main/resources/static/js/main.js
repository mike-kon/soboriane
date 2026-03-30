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
    console.log("Will be loaded data from server " + tag + ". Under construction");
}

function sendMessage() {
    console.log("Under construction");
}

function updateTextByWidth() {
    $('.dynamic-title').each(function () {
        updateTextByWidthElement(this);
        });
}

function updateTextByWidthElement(elem) {
    const dataname = ['data-full','data-medium','data-small'];
    const fullWidth = $(elem).width();
    const elems = $(elem).children('.dynamic-elem');
    const chLen = elems.length;
    let arr = new Array();
    // инициализация
    for (i=0 ; i< chLen; i++) {
        arr[i] = 0;
    }
    // подбор
    tooWide = true;
    tooSmall = false;
    cur=0;
    while (tooWide) {
        w = 0;
        for( i=0; i< chLen; i++) {
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
        for( i=0; i< chLen; i++) {
            elems[i].textContent = '';
        }
    }
}

function getTextWidthFromElement($element, text) {
    // Получаем стили элемента
    const fontSize = $element.css('fontSize');
    const fontFamily = $element.css('fontFamily');
    const font = `${fontSize} ${fontFamily}`;

    // Измеряем текст
    const canvas = $('<canvas></canvas>')[0];
    const context = canvas.getContext('2d');
    context.font = font;

    return context.measureText(text || $element.text()).width;
}

function init() {
    updateTextByWidth();
    $('#messageCanvas').scrollTop(1000);
}

window.addEventListener('load', init);
window.addEventListener('resize', init);