package com.github.whimax07;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BrowserConnection extends WebSocketServer {

    private PlotData lastPlotted;



    public BrowserConnection(InetSocketAddress address) {
        super(address);
    }



    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection to " + conn.getRemoteSocketAddress());
        if (lastPlotted != null) {
            conn.send(lastPlotted.toJson());
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    public void updateGraph(PlotData plotData) {
        lastPlotted = plotData;
        broadcast(plotData.toJson());
    }


    /**
     * title: Graph title displayed.
     * xData, yData: Graph points.
     * metaData: Saved as a comment to the top of a downloaded CSV.
     */
    public static class PlotData {
        public String title;
        public List<Double> xData;
        public List<Double> yData;
        public String metaData = "";

        public PlotData(String title, List<Double> xData, List<Double> yData) {
            this.title = title;
            this.xData = xData;
            this.yData = yData;
        }

        public PlotData(String title, List<Double> xData, List<Double> yData, String metaData) {
            this.title = title;
            this.xData = xData;
            this.yData = yData;
            this.metaData = metaData;
        }

        public String toJson() {
            final ArrayList<Double> xArray = new ArrayList<>(xData);
            final ArrayList<Double> yArray = new ArrayList<>(yData);

            return String.format(
                    "{\"title\": \"%s\", \"xData\": %s, \"yData\": %s, \"metaData\": \"%s\"}",
                    title, xArray, yArray, metaData
            );
        }
    }

}
