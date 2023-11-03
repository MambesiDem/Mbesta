package com.example.project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Server extends Thread implements Subscriber{

    Scanner sc = new Scanner(System.in);
    ServerSocket server;
    Socket connection;
    DataOutputStream dos;
    DataInputStream dis;
    int counter = 1;

    String info = "";
    public Server(String infoToSendToStudent){
        info=infoToSendToStudent;
    }
    @Override
    public void run(){
        Broker.subscribe(this,"Server");

        try {
            server = new ServerSocket(5050);
            System.out.println("SERVER: Server started "
                    + InetAddress.getLocalHost().getHostAddress());
            connection = server.accept();

            dos = new DataOutputStream(connection.getOutputStream());
            if(!info.equals(""))
                dos.writeUTF(info);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published){
            if(msg.getKey().equals("info")){
                info = (String)msg.getValue();
            }
        }
    }
}
