package kopr;

import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressBarSwingWorker extends SwingWorker<Void, Integer>{

    private JProgressBar progressBar;
    //private FileController fileController;

    public ProgressBarSwingWorker(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum((int) FileServer.FILE_TO_SEND.length());
        this.progressBar.setStringPainted(true);
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        int suma = 0;
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
}
