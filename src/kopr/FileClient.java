package kopr;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class FileClient {
    
    public static File FILE_TO_SAVE = new File("E:\\Kopr\\Copy.txt");
    public static int pocetVlakien;
    private static ExecutorService executor;
    private static ClientForm cf;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                cf = new ClientForm();
                cf.setVisible(true);
            }
        
        });
    }
    
    public static void spusti(){
        try {
            Socket soket = new Socket(FileServer.BROADCAST_IP, 1234);
            OutputStream ou = soket.getOutputStream();
            ou.write(pocetVlakien);
            soket.close();
            ou.close();
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
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