package com.eztech.mechrinka.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yami
 */
public class MachineServer {

    public final static int PORT = 5050;
    ServerSocket server;
    List<ClientSocket> clientsList = new ArrayList<>();
    MachineInterface mi;

    public MachineServer(MachineInterface mi) {
        this.mi = mi;
        try {
            server = new ServerSocket(PORT);
            server.setSoTimeout(1000);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        new Thread(Handler, "Clients Handler").start();
    }

    Runnable Handler = () -> {
        while (true) {
            try {
                Socket ss = server.accept();
                ClientSocket client1 = new ClientSocket(ss, mi);
                clientsList.add(client1);
            } catch (Exception ex) {
                // System.out.println(ex.getMessage());
                // Logger.getLogger(MachineServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            clientsList = clientsList.stream().filter((client) -> (!client.getSocket().isClosed())).collect(Collectors.toList());
            //System.out.println("No. Clients: " + clientsList.size());
        }
    };

}
