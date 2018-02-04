package hw1.b;

/**
 * This class violates rule 1 because number was being accessed in two methods with two different locks. Locking on this
 * in the go method locks on the NoVisibility2 object whereas the get method on the reader thread is locked on the
 * ReaderThread object. Locking it instead on NoVisibility.this locks on the encapsulating NoVisibility2 object which
 * solves the rule 1 violation.
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
            synchronized(this)
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
