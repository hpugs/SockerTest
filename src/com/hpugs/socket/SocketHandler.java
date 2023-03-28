package com.hpugs.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHandler extends Thread {

    Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    public synchronized void sendTalk(String tellInfo) throws IOException {   //将信息全发送给所有socket连接
        PrintWriter printStream = new PrintWriter(socket.getOutputStream(), true);
        printStream.println(socket.getRemoteSocketAddress() + ":" + tellInfo);
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            handle(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //线程执行完毕
            if (socket != null) {
                ClientManager.removeSocket(this);
                System.out.println(socket.getRemoteSocketAddress() + "客户端连接已断开");
            }
        }
    }

    private void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        bw.write("连接成功\n");
        bw.flush();
        while (true) {
            String message = br.readLine();  //读取客户端发送过来的消息
            System.out.println(String.format("客户%s：%s", socket.getRemoteSocketAddress(), message));

            //用户结束通话，线程执行完毕
            if (message == null || "end".equals(message)) {
                System.out.println(socket.getRemoteSocketAddress() + "退出");
                br.close();
                bw.close();
                break;
            }

            sendTalk(message);
        }
    }
}
