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
    public static int pocetVlakien;
    
    public static void odosli(int[] offsety) {                
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket[] sokety = new Socket[pocetVlakien];
            executor = Executors.newFixedThreadPool(pocetVlakien);
            for (int i = 0; i < sokety.length; i++) {
                sokety[i] = serverSocket.accept();
                executor.execute(new ThreadSend(sokety[i], i, pocetVlakien, offsety));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        pocetVlakien = 1;
        try {
            ServerSocket prvyServerSocket = new ServerSocket(1234);
            System.out.println("Cakam na pripojenie klienta");
            Socket soket = prvyServerSocket.accept();
            System.out.println("Klient sa pripojil");
            InputStream is = soket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            System.out.println("Zacinam komunikovat");
            while(soket.isConnected()){
                try {
                    int sprava = dis.readInt();
                    if(sprava == 0){
                        System.out.println("Zacinam posielat subor");
                        pocetVlakien = dis.readInt();
                        odosli(new int[pocetVlakien]);
                    }
                    if(sprava == 1){
                        System.out.println("Pokracujem v posielani suboru");
                        pocetVlakien = dis.readInt();
                        int[] offsety = new int[pocetVlakien];
                        for (int i = 0; i < pocetVlakien; i++) {
                            offsety[i] = dis.readInt();
                        }
                        odosli(offsety);
                    }
                } catch (EOFException e) {

                }
            }
            soket.close();
            dis.close();
            is.close();
            prvyServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //odosli();
    }
}