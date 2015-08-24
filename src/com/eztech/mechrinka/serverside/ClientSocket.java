package com.eztech.mechrinka.serverside;

import com.eztech.mechrinka.MachineMessage;
import com.eztech.mechrinka.MasterMessage;
import static com.eztech.mechrinka.MasterMessage.AddGcodeLine;
import static com.eztech.mechrinka.MasterMessage.EndGcode;
import static com.eztech.mechrinka.MasterMessage.GetName;
import static com.eztech.mechrinka.MasterMessage.InitGcode;
import static com.eztech.mechrinka.MasterMessage.VerifyResponse;
import com.eztech.mechrinka.SocketPeerHandler;
import java.io.IOException;
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
public class ClientSocket extends SocketPeerHandler {

    boolean verfied = false;
    MachineInterface mi;

    public ClientSocket(Socket socket, MachineInterface mi) throws IOException {
        super(socket);
        this.mi = mi;
    }

    private void MessageHandler(MasterMessage msg, String line) {
        switch (msg) {
            case GetName:
                sendName();
                break;
            case VerifyResponse:
                verfied = "Ez-Tech".equals(line);
                break;
            case InitGcode:
                mi.initGcode();
                break;
            case AddGcodeLine:
                mi.addGcodeLine(line);
                break;
            case EndGcode:
                mi.endGcode();
                break;
        }
    }

    private void verify() {
        sendMessage(MachineMessage.VerfiyMachine);
    }

    public void sendName() {
        sendMessage(MachineMessage.Name, mi.getName());
    }

    @Override
    protected void MessageHandler(String msg, String line) {
        MessageHandler(MasterMessage.valueOf(msg), line);
    }

    @Override
    protected void init() {
        try {
            verify();
            String line = in.readLine();
            MessageHandler(
                    getMessageHeader(line),
                    getMessageContent(line));
            if (verfied) {
                sendName();
            } else {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "Client" + this.socket.getInetAddress().toString();
    }

}
