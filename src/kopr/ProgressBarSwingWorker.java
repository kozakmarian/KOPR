package kopr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressBarSwingWorker extends SwingWorker<Void, Integer>{

    private JProgressBar progressBar;

    public ProgressBarSwingWorker(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setMinimum(0);
        //this.progressBar.setMaximum((int) FileClient.velkostSuboru);
        this.progressBar.setStringPainted(true);
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        int suma = 0;
        int start = 0;
        File file = FileClient.suborNaZapisanie;
        if (file.exists()) {
            System.out.println("som v podmienke");
            start = nastavProgressbar();
        }
        
        while(FileClient.velkostSuboru == 0){
            Thread.sleep(500);
        }
        this.progressBar.setMaximum((int) FileClient.velkostSuboru);
        while((!Thread.currentThread().isInterrupted() && suma < FileClient.velkostSuboru)){
            
            if(FileClient.fileController != null){
                suma = FileClient.fileController.getPrecitaneZoSuboru();
            }
            publish(suma + start);
            Thread.sleep(500);
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        //System.out.println("process: " + chunks.get(chunks.size()-1));
        progressBar.setValue(chunks.get(chunks.size()-1));
    }

    @Override
    protected void done() {
        System.out.println("done");
        progressBar.setValue((int) FileClient.velkostSuboru);
    }
    
    public int nastavProgressbar() {
        Scanner scanner = null;
        int sucet = 0;
        File file = FileClient.suborNaZapisanie;
        try {
            //System.out.println("try");
            scanner = new Scanner(file);
            int pocitadlo = 0;
            while (scanner.hasNextInt()) {
                if (pocitadlo == 0){
                    int pocetVlakien = scanner.nextInt();
                    pocitadlo++;
                }
                //System.out.println("while");
                sucet += scanner.nextInt();
            }
            //System.out.println(sucet);
        } catch (FileNotFoundException e) {
            sucet = 0;
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        
        //sucetOffsetov = sucet;
        return sucet;
    }
}
