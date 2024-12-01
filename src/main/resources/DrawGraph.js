var wsocket;

function connect() {
    console.log("Connecting")
    try {
        wsocket = new WebSocket("ws://localhost:12345");
    } catch (error) {
        console.log("Error: ", error.message)
    }
    wsocket.onopen = onopen;
    wsocket.onmessage = onmessage;
    wsocket.onclose = onclose;
}

function onopen() {
    console.log("Connected!");
    wsocket.send('hello from client');
}

function onmessage(event) {
   console.log("Data received: " + event.data);
   const label = document.getElementById("receivedMessage");
   label.innerText = event.data;
}

function onclose(e) {
   console.log("Connection closed.");
}

function plot(title, xData, yData) {
    const trace = {
        x: xData,
        y: yData,
        type: 'scatter'
    };

    const layout = {
        title: {
            text: title
        },
        showlegend: false
    };

    Plotly.react('graph', [trace], layout);
}

function init_plot() {
    const trace1 = {
        x: ['2020-10-04', '2021-11-04', '2023-12-04'],
        y: [90, 40, 60],
        type: 'scatter'
    };

    const data = [trace1];

    const layout = {
        title: {
            text: 'Scroll and Zoom'
        },
        showlegend: false
    };

    Plotly.react('graph', data, layout);
}

window.addEventListener("load", connect, false);
window.addEventListener("load", init_plot, false);
