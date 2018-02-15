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
    public static DataOutputStream dos;
    
    public static void odosli(int[] offsety) throws IOException {                
        try {
            dos.writeInt(SERVER_PORT);
            dos.writeLong(FILE_TO_SEND.length());
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket[] sokety = new Socket[pocetVlakien];
            executor = Executors.newFixedThreadPool(pocetVlakien);
            for (int i = 0; i < sokety.length; i++) {
                sokety[i] = serverSocket.accept();
                executor.execute(new ThreadSend(sokety[i], i, pocetVlakien, offsety));
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws IOException {
        pocetVlakien = 1;
        ServerSocket prvyServerSocket = new ServerSocket(1234);
        System.out.println("Cakam na pripojenie klienta");
        while(true){
            Socket soket = prvyServerSocket.accept();
            System.out.println("Klient sa pripojil");
            dos = new DataOutputStream(soket.getOutputStream());
            InputStream is = soket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            System.out.println("Zacinam komunikovat");
            while(soket.isConnected()){
                try {
                    int sprava = dis.readInt();
                    System.out.println(sprava);
                    if(sprava == 0){
                        System.out.println("Zacinam posielat subor");
                        pocetVlakien = dis.readInt();
                        odosli(new int[pocetVlakien]);
                        System.out.println("skoncil som if");
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
                } catch (SocketException e) {
                    break;
                }
            }
            soket.close();
            dis.close();
            is.close();
            //prvyServerSocket.close();
        }
        //odosli();
    }
}