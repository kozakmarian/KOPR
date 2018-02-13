package kopr;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class FileClient {
    
    public static File FILE_TO_SAVE = new File("E:\\Kopr\\SrdecneVasVitame.avi");
    public static File suborNaZapisanie = new File("subor.txt");
    public static int pocetVlakien;
    private static ExecutorService executor;
    private static ClientForm cf;
    private static DataOutputStream dos;
    private static Socket soket;
    public static int[] offsety;
    public static FileController fileController;
    public static CountDownLatch pocitadlo;
    private static int sprava;
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                cf = new ClientForm();
                cf.setVisible(true);
            }
        
        });
        //fileController = new FileController();
        soket = new Socket(FileServer.BROADCAST_IP, 1234);
        OutputStream ou = soket.getOutputStream();
        dos = new DataOutputStream(ou);
        System.out.println("Klient sa pripaja na server");
        sprava = 0;
    }
    
    public static void spusti(){
        System.out.println("Zacinam prijimat subor");
        int port = 0;
        try {
            if(sprava == 0){
                offsety = new int[pocetVlakien];
            }
            fileController = new FileController();
            /*fileController = new FileController();
            Socket soket = new Socket(FileServer.BROADCAST_IP, 1234);
            OutputStream ou = soket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(ou);
            System.out.println("Klient sa pripaja na server");*/
            dos.writeInt(0);
            dos.writeInt(pocetVlakien);
            //soket.close();
            //ou.close();
            DataInputStream dis = new DataInputStream(soket.getInputStream());
            port = dis.readInt();
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        Socket[] klientskeSokety = null;
        try {
            pocitadlo = new CountDownLatch(pocetVlakien);
            klientskeSokety = new Socket[pocetVlakien];
            executor = Executors.newFixedThreadPool(pocetVlakien);
            for (int i = 0; i < klientskeSokety.length; i++) {
                klientskeSokety[i] = new Socket(FileServer.BROADCAST_IP, port);
                executor.execute(new ThreadReceive(klientskeSokety[i], i, offsety));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                soket.close();
            } catch (IOException ex) {
                Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void pokracuj(){
        try {
            sprava = 1;
            System.out.println("Pokracujem v prijimani suboru");
            //offsety = new int[pocetVlakien];
            Scanner sc = null;
            try {
                sc = new Scanner(suborNaZapisanie);
                offsety = new int[sc.nextInt()];
                pocetVlakien = offsety.length;
                for (int i = 0; i < offsety.length; i++) {
                    offsety[i] = sc.nextInt();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                sc.close();
            }
            dos.writeInt(1);
            dos.writeInt(pocetVlakien);
            for (int i = 0; i < offsety.length; i++) {
                dos.writeInt(offsety[i]);
            }
            spusti();
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void prerus(){
        executor.shutdownNow();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(suborNaZapisanie);
            System.out.println("zapisujem offsety");
            int[] offsety = fileController.getOffset();
            pw.println(pocetVlakien);
            for (int i = 0; i < pocetVlakien; i++) {
                pw.println(offsety[i]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
            cf.setVisible(false);
            System.exit(1);
        }
    }
    
    public static void zrus(){
        executor.shutdownNow();
        if(FILE_TO_SAVE.exists()){
            System.out.println("subor sa maze");
            FILE_TO_SAVE.delete();
        }
        if(suborNaZapisanie.exists()){
            suborNaZapisanie.delete();
        }
        cf.setVisible(false);
        System.exit(1);
    }
}