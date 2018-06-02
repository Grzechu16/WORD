package word.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WordServer {
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                (new WordServerThread(socket)).start();
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
