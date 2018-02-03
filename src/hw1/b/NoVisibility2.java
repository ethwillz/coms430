package hw1.b;

/**
 * TODO NEED RESPONSE HERE
 */
public class NoVisibility2 {
    private int number;
    private volatile int count;
    private final int ITERATIONS = 10000;

    public static void main(String[] args)
    {
        NoVisibility2 nv = new NoVisibility2();
        nv.go();
    }

    private void go()
    {
        new NoVisibility2.ReaderThread().start();

        System.out.println("Main.java thread starting loop");

        for (int i = 0; i <= ITERATIONS; ++i)
        {
            synchronized(NoVisibility2.this) //Locked on encapsulating NoVisibility2 instance
            {
                number = i;
            }
            Thread.yield();
        }
    }

    class ReaderThread extends Thread
    {

        public int get()
        {
            while (true)
            {
                count++;
                synchronized(NoVisibility2.this) //Locked on encapsulating NoVisibility2 instance
                {
                    if (number >= ITERATIONS) break;
                }
            }
            return number;
        }

        public void run()
        {
            System.out.println("Reader starting...");
            int result = get();
            System.out.println("Reader sees number: " + result + " and count is: " + count);
        }
    }
}
