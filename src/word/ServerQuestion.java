package word;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerQuestion {
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5432);
            while (true) {
                Socket socket = serverSocket.accept();
                (new ServerQuestionThread(socket)).start();
            }
        } catch (Exception e) {
            System.err.println(e);
        }finally{
            if(serverSocket != null)
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
