package com.eztech.mechrinka;

import com.eztech.mechrinka.clientside.Machine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
public abstract class SocketPeerHandler implements Runnable {

    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected Thread handler;
    protected transient boolean close;

    public SocketPeerHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(),
                    true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            socket.setSoTimeout(1000);
            handler = new Thread(this, this.toString());
            handler.start();
        } catch (IOException ex) {
            Logger.getLogger(SocketPeerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void MessageHandler(String msg, String line);

    public void sendMessage(Object msg, String line) {
        out.println(msg + " " + line);
    }

    public void sendMessage(Object msg) {
        sendMessage(msg, null);
    }

    protected String getMessageContent(String line) {
        String content = line.substring(line.indexOf(" ")).trim();
        content = content.equals("null") ? null : content;
        return content;
    }

    protected String getMessageHeader(String line) {
        return line.substring(0, line.indexOf(" "));
    }

    @Override
    public void run() {
        init();
        while (socket.isConnected() && !socket.isClosed()) {
            try {
                if (close) {
                    socket.close();
                    System.err.println(socket + "is Closed");
                    continue;
                }
                String line = in.readLine();
                if (line != null) {
                    System.out.println(socket + "=>" + line);
                    MessageHandler(
                            getMessageHeader(line),
                            getMessageContent(line));
                } else {
                    closeConnection();
                   //out.println("ConnectionTest n");
                }
            } catch (IOException ex) {
                if (ex instanceof SocketTimeoutException) {
                } else if (ex instanceof SocketException ) {
                    closeConnection();
                } else {
                    System.err.println(socket + "IO++++>" + ex);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        close = true;
    }

    public Socket getSocket() {
        return socket;
    }

    protected void init() {
    }

}
