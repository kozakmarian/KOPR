package kopr;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadReceive implements Runnable {
    
    private Socket soket;
    private int indexSoketu;

    public ThreadReceive(Socket soket, int indexSoketu) {
        this.soket = soket;
        this.indexSoketu = indexSoketu;
    }

    @Override
    public void run() {
        InputStream is = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(FileClient.FILE_TO_SAVE, "rw");
            is = soket.getInputStream();
            int castSuboru = (int) Math.ceil((double)FileServer.FILE_TO_SEND.length()/FileClient.pocetVlakien);
            int offset = castSuboru * indexSoketu;
            byte[] poleBajtov = new byte[ThreadSend.chunkSize];
            raf.seek(offset);
            int precitalSom = is.read(poleBajtov, 0, poleBajtov.length);
            System.out.println("som tu");
            while(precitalSom > 0){
                System.out.println(offset);
                raf.seek(offset);
                raf.write(poleBajtov, 0, precitalSom);
                offset = offset + precitalSom;
                precitalSom = is.read(poleBajtov, 0, poleBajtov.length);
            }
            System.out.println("koniec");
        } catch (IOException ex) {
            Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
                soket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
