package kopr;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileClient {
    
    public static File FILE_TO_SAVE = new File("E:\\Kopr\\Copy.txt");
    public static int pocetVlakien = 3;
    private static ExecutorService executor;
    
    public static void main(String[] args) {
        try {
            Socket[] klientskeSokety = new Socket[pocetVlakien];
            executor = Executors.newFixedThreadPool(pocetVlakien);
            for (int i = 0; i < klientskeSokety.length; i++) {
                klientskeSokety[i] = new Socket(FileServer.BROADCAST_IP, FileServer.SERVER_PORT);
                executor.execute(new ThreadReceive(klientskeSokety[i], i));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}