package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Runnable {

    private static DatagramSocket socket;
    private static boolean running;
    private static byte[] buffer = new byte[256];

    public Server() {
        try {
            socket = new DatagramSocket(4445);
        } catch(SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Server hosted on port " + socket.getLocalPort());
    }

    @Override
    public void run() {
        running = true;

	    while(running) {
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	        try {
                socket.receive(packet);
            } catch(IOException e) {
	            e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
	        int port = packet.getPort();
	        packet = new DatagramPacket(buffer, buffer.length, address, port);

	        String received = new String(packet.getData(), 0, packet.getLength());
	        if(received.equals("end")) {
                running = false;
                continue;
            }

            buffer = "Hej Hej Monika".getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
	            e.printStackTrace();
            }
        }

        socket.close();
    }
}
