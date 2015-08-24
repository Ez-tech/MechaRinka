package com.eztech.mechrinka.clientside;


import com.eztech.mechrinka.serverside.MachineServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yami
 */
public class MechClient {

    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public MechClient(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(),
                true);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    System.out.println(in.readLine());
                } catch (Exception ex) {
                    Logger.getLogger(MechClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    static public MechClient connect(String host) throws IOException {
        Socket socket = new Socket(host, MachineServer.PORT);
        return new MechClient(socket);
    }

}
