package kopr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressBarSwingWorker extends SwingWorker<Void, Integer>{

    private JProgressBar progressBar;
    private static int sucetOffsetov;

    public ProgressBarSwingWorker(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum((int) FileServer.FILE_TO_SEND.length());
        this.progressBar.setStringPainted(true);
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        int suma = 0;
        File file = FileClient.suborNaZapisanie;
        if (file.exists()) {
            suma = sucetOffsetov;
        }
        while(!Thread.currentThread().isInterrupted() && suma < FileServer.FILE_TO_SEND.length()){
            suma = FileClient.fileController.getPrecitaneZoSuboru();
            publish(suma);
            Thread.sleep(500);
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        progressBar.setValue(chunks.get(chunks.size()-1));
    }

    @Override
    protected void done() {
        progressBar.setValue(100);
    }
    
    public static void nastavProgressbar() {
        Scanner scanner = null;
        int sucet = 0;
        File file = FileClient.suborNaZapisanie;
        if (!file.exists()) {
            sucetOffsetov = 0;
        }
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                sucet += scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            sucetOffsetov = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        sucetOffsetov = sucet;
    }
}
