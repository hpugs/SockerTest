package com.hpugs.socket;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    public static List<SocketHandler> list = new ArrayList<>();

    public static void removeSocket(SocketHandler handler) {   //移除socket连接
        synchronized (list) {
            list.remove(handler);
        }
    }

}
