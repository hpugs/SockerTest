package com.hpugs.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xinge
 */
public class ServerSocketTest {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7000);
        System.out.println("服务器已启动!");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(String.format("客户端连接已经创建，客户端地址：" + socket.getRemoteSocketAddress()));
            SocketHandler talkThread = new SocketHandler(socket);
            ClientManager.list.add(talkThread);
            talkThread.start();
        }
    }

}
