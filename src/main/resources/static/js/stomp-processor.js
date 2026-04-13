// todo Надо научится текстовые константы подгружать из application.yaml
let stompClient = null;
let retryConnect = 0;

function connect() {
    const socket = new SockJS(socketName);
    stompClient = Stomp.over(socket);

    // отключаем отладку
    stompClient.debug = () => {};

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        retryConnect = 0;

        // Подписываемся на топик обновлений
        stompClient.subscribe(topic, function (message) {
            console.log('Received update from ' + message.headers["destination"] + ",  id:" + message.headers["message-id"]);
            $('#load').html(message.body);
        });

    }, function (error) {
        console.error('STOMP error (' + retryConnect + '):', error);
        if  (retryConnect++ < maxRetryConnect) {
            setTimeout(connect, timeOutValue);
        } else {
            alert("Соединение потеряно");
            disconnect();
        }
    });
}

function disconnect() {
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
        connect();

        // Отключаемся при закрытии страницы
        window.onbeforeunload = function () {
            disconnect();
        };
    });
});
