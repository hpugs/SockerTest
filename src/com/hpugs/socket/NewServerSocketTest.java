package com.hpugs.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewServerSocketTest {

    public static void main(String[] args) throws IOException {
        Socket OldSocket = new Socket("127.0.0.1", 64617);
        SocketHandler socketHandler = new SocketHandler(OldSocket);
        ClientManager.list.add(socketHandler);
        socketHandler.start();
        System.out.println("服务器重启完成!");
    }

}
