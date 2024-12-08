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

    try {
        const asJson = JSON.parse(event.data);
        plot(asJson.title, asJson.xData, asJson.yData);
        document.getElementById("downloadButton").addEventListener(
            "click",
            () => addDownloadButton(asJson.title, asJson.xData, asJson.yData, asJson.metaData)
        );
    } catch (error) {
        console.log("Error: ", error.message);
    }
}

function onclose(e) {
    console.log("Connection closed.");
}

function initPlot() {
    plot("Hello!", ['2020-10-04', '2021-11-04', '2023-12-04'], [90, 40, 60]);
}

function plot(title, xData, yData) {
    console.log(`Received plot request. [Title=${title}]`)
    const numLabels = Math.min(20, xData.length);

    const step = Math.min(1, Math.floor(xData.length / numLabels));
    const visibleLabels = xData.filter((_, i) => i % step === 0 || i === xData.length - 1);

    const trace = {
        x: xData,
        y: yData,
        mode: 'lines',
        type: 'scatter'
    };

    const layout = {
        title: {
            text: title
        },
        xaxis: {
            tickmode: 'array',
            tickvals: visibleLabels,
            ticktext: visibleLabels.map(x => toLabelString(x, 2)),
            showspikes: true
        },
        yaxis: {
            showspikes: true
        },
        hovermode: 'closest',
        autosize: true
    };

    const config = {
        responsive: true,
        displayModeBar: true,
    }

    Plotly.react('graph', [trace], layout, config);
}

function addDownloadButton(title, xData, yData, metaData) {
    // Format metadata for the link.
    let csvContent = `# Title: ${title}\n`;

    // Add the metadata if it exists.
    if (metaData != null && metaData.length > 0) {
        const comment = metaData.replace(/\n/g, "\n# ");
        csvContent += "# " + comment + "\n\n";
        console.log("Metadata: " + csvContent);
    }

    // Add the row headings and the data.
    csvContent += "X-Data,Y-Data\n";
    for (let i = 0; i < xData.length; i++) {
        csvContent += `${toLabelString(xData[i], 4)},${yData[i]}\n`
    }

    // Save the file.
    const blob = new Blob([csvContent], { type: "text/plain" });
    const encodedUri = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", "GraphData.csv");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function toLabelString(value, numDp) {
    if (isFloat(value)) {
        return value.toFixed(numDp).toString();
    } else {
        return value.toString();
    }
}

function isFloat(value) {
    return typeof value === "number" && !Number.isInteger(value);
}



window.addEventListener("load", connect, false);
window.addEventListener("load", initPlot, false);
