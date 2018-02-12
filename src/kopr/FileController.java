package kopr;

public class FileController {
    
    private int precitaneZoSuboru;
    private int[] offsety = new int[FileClient.pocetVlakien];

    public synchronized int getPrecitaneZoSuboru() {
        return precitaneZoSuboru;
    }

    public synchronized void setPrecitaneZoSuboru(int stav) {
        this.precitaneZoSuboru += stav;
    }
    
    public synchronized int[] getOffset() {
        return offsety;
    }

    public synchronized void setOffset(int offset, int indexSoketu) {
        offsety[indexSoketu] = offset;
    }
    
    
}
