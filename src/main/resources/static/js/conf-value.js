const path = '/api/init/clientProperties'

let socketName; // = '/ws-stomp';
let topic; // = '/topic/updates';
let maxRetryConnect; // = 5;
let timeOutValue; // = 5000;

async function initVariable() {
    console.log("conf-value init");
    await $.post(path, {}, function (prop) {
        socketName = prop.socketName;
        topic = prop.topic;
        maxRetryConnect = prop.maxRetryConnect;
        timeOutValue = prop.timeOutValue;
        console.log("Переменные проинициализированы");
    });
}