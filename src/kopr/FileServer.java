package kopr;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServer {
    
    public static File FILE_TO_SEND = new File("E:\\Poznamky.txt");
    public static int SERVER_PORT = 5000;
    public static String BROADCAST_IP = "localhost";
    private static ExecutorService executor;
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket[] sokety = new Socket[FileClient.pocetVlakien];
            executor = Executors.newFixedThreadPool(FileClient.pocetVlakien);
            for (int i = 0; i < sokety.length; i++) {
                sokety[i] = serverSocket.accept();
                executor.execute(new ThreadSend(sokety[i], i));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}