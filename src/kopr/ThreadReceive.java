package kopr;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadReceive implements Runnable {
    
    private Socket soket;
    private int indexSoketu;
    private int[] offsety;

    public ThreadReceive(Socket soket, int indexSoketu, int[] offsety) {
        this.soket = soket;
        this.indexSoketu = indexSoketu;
        this.offsety = offsety;
    }

    @Override
    public void run() {
        InputStream is = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(FileClient.FILE_TO_SAVE, "rw");
            is = soket.getInputStream();
            int castSuboru = (int) Math.ceil((double)FileServer.FILE_TO_SEND.length()/FileClient.pocetVlakien);
            //int offset = castSuboru * indexSoketu;
            byte[] poleBajtov = new byte[ThreadSend.chunkSize];
            //raf.seek(offset);
            raf.seek(offsety[indexSoketu] + indexSoketu*castSuboru);
            int precitalSom = is.read(poleBajtov, 0, poleBajtov.length);
            while(precitalSom > 0){
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                //raf.seek(offset);
                raf.write(poleBajtov, 0, precitalSom);
                offsety[indexSoketu] = offsety[indexSoketu] + precitalSom;
                //offset = offset + precitalSom;
                FileClient.fileController.setPrecitaneZoSuboru(precitalSom);
                FileClient.fileController.setOffset(offsety[indexSoketu], indexSoketu);
                precitalSom = is.read(poleBajtov, 0, poleBajtov.length);
            }
            FileClient.pocitadlo.countDown();

            if(FileClient.pocitadlo.getCount() == 0) {
                System.out.println("Koniec kopirovania");
            }
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                System.out.println("finally");
                is.close();
                soket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
