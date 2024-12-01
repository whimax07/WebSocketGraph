package com.github.whimax07;


import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {

    private static void sendA(BrowserConnection browserConnection) {
        System.out.println("Sending A");
        browserConnection.broadcast("TEXT A");
    }

    private static void sendB(BrowserConnection browserConnection) {
        System.out.println("Sending B");
        browserConnection.broadcast("TEXT B");
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