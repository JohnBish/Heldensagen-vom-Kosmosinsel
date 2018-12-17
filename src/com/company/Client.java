package com.company;

import java.io.IOException;
import java.net.*;

public class Client {

    private DatagramSocket socket;
    private InetAddress address;

    public Client() {
        try {
            socket = new DatagramSocket();
        } catch(SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName("localhost");
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String send(String message) {
        byte[] buffer = message.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);
        try {
            socket.send(packet);
        } catch(IOException e) {
            e.printStackTrace();
        }

        packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.setSoTimeout(1000);
        } catch(SocketException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                socket.receive(packet);
                break;
            } catch(SocketTimeoutException e) {
                System.out.println("Socket timed out: " + e.toString());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return new String(packet.getData(), 0, packet.getLength());
    }

    public void close() {
        socket.close();
    }
}
