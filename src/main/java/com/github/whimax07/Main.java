package com.github.whimax07;


import com.github.whimax07.BrowserConnection.PlotData;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static void sendA(BrowserConnection browserConnection) {
        System.out.println("Sending A");
        browserConnection.broadcast("{\"title\": \"A\", \"xData\": [1,2], \"yData\": [3,4]}");
    }

    private static void sendB(BrowserConnection browserConnection) {
        System.out.println("Sending B");
        browserConnection.broadcast("{\"title\": \"B\", \"xData\": [12.5, 13.0, 13.5], \"yData\": [0.333, 0.666, 0.333]}");
    }

    private static void sendC(BrowserConnection browserConnection) {
        System.out.println("Sending C");
        final PlotData optionC = new PlotData(
                "Option C",
                List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                List.of(100.0, 120.0, 200.0, 205.0, 300.0, 295.0),
                "This is a demo data set. \\nIt was picked at random and isn't great."
        );
        browserConnection.updateGraph(optionC);
    }



    public static void main(String[] args) throws InterruptedException {
        final BrowserConnection browserConnection = new BrowserConnection(new InetSocketAddress("localhost", 12345));
        browserConnection.start();

        boolean stop = false;
        final Scanner scanner = new Scanner(System.in);

        while (!stop) {
            final String line = scanner.nextLine().strip().toUpperCase();

            switch (line) {
                case "A" -> sendA(browserConnection);
                case "B" -> sendB(browserConnection);
                case "C" -> sendC(browserConnection);
                case "EXIT", "Q" -> stop = true;
            }
        }

        scanner.close();
        browserConnection.stop();
    }

}