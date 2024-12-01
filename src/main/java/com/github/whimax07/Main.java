package com.github.whimax07;


import java.net.InetSocketAddress;
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
                case "EXIT", "Q" -> stop = true;
            }
        }

        scanner.close();
        browserConnection.stop();
    }

}