import java.util.ArrayList;
import java.util.Iterator;

public class FPSCounter {
    public int FPS;
    private ArrayList<Long> frameTimes;

    private static final int SDerivative = 1;


    public FPSCounter() {
        frameTimes = new ArrayList<Long>();
    }

    public void update() {
        frameTimes.add(System.currentTimeMillis());
        cull();
        this.FPS = frameTimes.size() / SDerivative;
    }

    private void cull() {
        Iterator<Long> iterator = frameTimes.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() < System.currentTimeMillis() - SDerivative*1000) {
                iterator.remove();
            }
        }
    }
}
