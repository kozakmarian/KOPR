package kopr;

public class FileController {
    
    private int precitaneZoSuboru;

    public synchronized int getPrecitaneZoSuboru() {
        return precitaneZoSuboru;
    }

    public synchronized void setPrecitaneZoSuboru(int stav) {
        this.precitaneZoSuboru += stav;
    }
    
    
}
