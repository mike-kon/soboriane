// todo Надо научится текстовые константы подгружать из application.yaml
let stompClient = null;
let retryConnect = 0;

function stompConnect() {
    const socket = new SockJS(endpoint);
    stompClient = Stomp.over(socket);

    // отключаем отладку
    stompClient.debug = () => {
    };

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        retryConnect = 0;

        // Подписываемся на топик обновлений
        const topicSubscribe = userPrefix + topic;
        stompClient.subscribe(topicSubscribe, function (message) {
            console.log('Received update from ' + message.headers["destination"] + ",  id:" + message.headers["message-id"]);
            $('#messageCanvas').html(message.body);
        });

    }, function (error) {
        console.error('STOMP error (' + retryConnect + '):', error);
        if (retryConnect++ < maxRetryConnect) {
            setTimeout(stompConnect, timeOutValue);
        } else {
            alert("Соединение потеряно");
            stompDisconnect();
        }
    });
}

function stompDisconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

// При загрузке страницы подключаемся
$(document).ready(function () {
    initVariable().then(() => {
        console.log("stomp processor init");
        retryConnect = 0;
        stompConnect();
        // Отключаемся при закрытии страницы
        window.onbeforeunload = function () {
            console.log("disconnect");
            stompDisconnect();
        };
    });
});

