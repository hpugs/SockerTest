package com.hpugs.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocketTest {

    public static void main(String[] args) throws IOException {
        // 方式一：
//        Socket socket = new Socket("localhost", 7000);

        // 方式二：
        Socket socket = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 7000);
        socket.setKeepAlive(true);
        //设置超时为3秒
        socket.connect(socketAddress, 3*1000);

        //这样才能捕捉异常：java.net.SocketTimeoutException: Read timed out，非常重要，否则
        //当在连接上后，刚好服务器器socket server断开，直接运行readLine（）会死锁
//        socket.setSoTimeout(5*1000);

        // 封装socket
        ClientHandler clientHandler = new ClientHandler(socket);
        clientHandler.start();
    }
}

class ClientHandler {

    Socket socket = null;
    boolean flag = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    Thread sendMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("IOException");
            }
            while (flag) {
                try {
                    String message = scanner.nextLine();
                    if ("".equals(message)) continue;//发送内容为空，则不发送

                    printWriter.println(message);

                    if ("end".equals(message)) {
                        flag = false;//如果发送内容为end，则表示结束通信
                        break;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("输入流已关闭");
                    flag = false;
                }
            }
        }
    });

    Thread getMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                while (flag) {
                    String sr = socketReader.readLine();
                    if(sr != null){
                        System.out.println(sr);
                    }
                }
            } catch (SocketTimeoutException e) {
                System.out.println("SocketTimeoutException");
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    });

    public void start() {
        sendMessage.start();
        getMessage.start();
    }

}
