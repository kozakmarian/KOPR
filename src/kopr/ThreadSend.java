package kopr;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadSend implements Runnable {

    private Socket soket;
    private int indexSoketu;
    public static int chunkSize = 1000000;
    public static boolean boloPrerusene;
    int pocetVlakien;
    int[] offsety;
    
    public ThreadSend(Socket soket, int indexSoketu, int pocetVlakien, int[] offsety) {
        this.soket = soket;
        this.indexSoketu = indexSoketu;
        this.pocetVlakien = pocetVlakien;
        this.offsety = offsety;
    }

    @Override
    public void run() {
        OutputStream os = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(FileServer.FILE_TO_SEND, "rw");
            os = soket.getOutputStream();
            int castSuboru = (int) Math.ceil((double)FileServer.FILE_TO_SEND.length()/pocetVlakien);
            //int offset = castSuboru * indexSoketu;
            int pocetPaketov = (int) Math.ceil((double) (castSuboru - offsety[indexSoketu]) / chunkSize);
            byte[] poleBajtov = new byte[chunkSize];
            //raf.seek(offset);
            raf.seek(offsety[indexSoketu] + indexSoketu*castSuboru);
            //for (int i = 0; i < (int) Math.ceil((double) castSuboru/chunkSize); i++) {
            for (int i = 0; i < pocetPaketov; i++) {
                //System.out.println(offset);
                int precitalSom = raf.read(poleBajtov, 0, chunkSize);
                //offset = offset + precitalSom;
                //raf.seek(offset);
                os.write(poleBajtov, 0, precitalSom);
                os.flush();
            }
        } catch (IOException ex) {
            boloPrerusene = true;
            //System.out.println("vynimka");
            //Logger.getLogger(ThreadSend.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
                soket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadSend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
