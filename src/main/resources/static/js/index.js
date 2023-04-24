let websocket;
const onConnect = (event) => {

    event.preventDefault();

    if (websocket) {
        alert("Already Connected");
    }

    const name = document.getElementById("client_name").value;

    websocket = new WebSocket("ws://localhost:8080/ws/?name=" + name);
    // websocket.onmessage = onMessage;
    websocket.onopen = onOpen;
    websocket.onclose = onClose;

    //채팅창에서 나갔을 때
    function onClose(evt) {
        let str = name + ": 님이 방을 나가셨습니다.";
        websocket.send(str);
    }

    //채팅창에 들어왔을 때
    function onOpen(evt) {
        let str = name + ": 님이 입장하셨습니다.";
        websocket.send(str);
    }

    return false;
}

window.onload = () => {
    document.getElementById("client_login_button").addEventListener("click", onConnect, false);
}




