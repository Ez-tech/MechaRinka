/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eztech.mechrinka.clientside;

import com.eztech.mechrinka.MachineMessage;
import static com.eztech.mechrinka.MachineMessage.Name;
import static com.eztech.mechrinka.MachineMessage.VerfiyMachine;
import com.eztech.mechrinka.MasterMessage;
import com.eztech.mechrinka.SocketPeerHandler;
import com.eztech.mechrinka.serverside.MachineServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yami
 */
public class Machine extends SocketPeerHandler {

    String name = "Ez-Machine";

    public Machine(Socket socket) throws IOException {
        super(socket);
    }

    private void MessageHandler(MachineMessage msg, String line) {
        switch (msg) {
            case Name:
                name = line;
                Thread.currentThread().setName(toString());
                break;
            case VerfiyMachine:
                verfiy();
                break;
        }
    }

    public void verfiy() {
        sendMessage(MasterMessage.VerifyResponse, "Ez-Tech");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return name.substring(3);
    }

    @Override
    public String toString() {
        return name + " on " + socket.getInetAddress();
    }

    @Override
    protected void MessageHandler(String msg, String line) {
        MessageHandler(MachineMessage.valueOf(msg), line);
    }

    public static List<Machine> getAvailableMachines() {
        List<Machine> availableList = new ArrayList<>();
        for (int i = 1; i < 255; i++) {
            try {
                Socket server = new Socket();
                server.connect(new InetSocketAddress("192.168.1." + i, MachineServer.PORT), 10);
                if (server.isConnected()) {
                    availableList.add(new Machine(server));
                }
            } catch (Exception e) {
            }
        }
        return availableList;
    }

}
