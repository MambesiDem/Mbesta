package com.example.washit;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Client extends Thread{

    private Displayer<String> displayer;

    private String serverAddress;
    private String info = "";

    public Client(String serverAddress, Displayer<String> displayer){
        this.serverAddress = serverAddress;
        this.displayer = displayer;
    }

    @Override
    public void run(){
        Socket client;
        DataInputStream input;
        DataOutputStream output;

        try {
            client = new Socket(serverAddress, 5050);


            while(true){
                input = new DataInputStream(client.getInputStream());

                info = input.readUTF();
                display(info);
            }
            //client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void display(String msg){
        if(displayer == null)return;
        displayer.display(msg);
    }
    public String getInfo(){
        return info;
    }
}