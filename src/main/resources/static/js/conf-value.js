const path = '/api/init/clientProperties'

let maxRetryConnect;
let timeOutValue;
let endpoint;
let userPrefix;
let topic;

async function initVariable() {
    console.log("conf-value init");
    await $.post(path, {}, function (prop) {
        endpoint = prop.endpoint;
        userPrefix = prop.userPrefix;
        topic = prop.topic;
        maxRetryConnect = prop.maxRetryConnect;
        timeOutValue = prop.timeOutValue;
        console.log("Переменные проинициализированы");
    });
}