package kopr;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServer {
    
    public static File FILE_TO_SEND = new File("E:\\SrdecneVasVitame.avi");
    public static int SERVER_PORT = 5000;
    public static String BROADCAST_IP = "localhost";
    private static ExecutorService executor;
    
    public static void main(String[] args) {
        int pocetVlakien = 1;
        try {
            ServerSocket prvyServerSocket = new ServerSocket(1234);
            Socket soket = prvyServerSocket.accept();
            InputStream is = soket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            pocetVlakien = dis.read();
            soket.close();
            dis.close();
            is.close();
            prvyServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket[] sokety = new Socket[pocetVlakien];
            executor = Executors.newFixedThreadPool(pocetVlakien);
            for (int i = 0; i < sokety.length; i++) {
                sokety[i] = serverSocket.accept();
                executor.execute(new ThreadSend(sokety[i], i, pocetVlakien));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}