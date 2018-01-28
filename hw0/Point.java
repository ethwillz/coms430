import java.awt.Point;

public class Trajectory {
    private Point[] data;

    public static void main(String[] args){

    }

    public Trajectory(Point[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public synchronized Point[] getValues() {
        return data;
    }

    public synchronized Point getValue(int index) {
        return data[index];
    }

    public synchronized void update(int i, int dx, int dy) {
        data[i].x += dx;
        data[i].y += dy;
    }

    private class UpdateWorker extends Thread{
        Random rand;
        Trajectory t;

        public UpdateWorker(Trajectory t){
            this.rand = new Random();
            this.t = t;
        }

        public void run(){
            update(rand.nextInt(t.getValues().length), rand.nextInt(100), rand.nextInt(100));
        }

        public void update(int i, int dx, int dy){
            t.update(i. dx, dy);
        }
    }
}