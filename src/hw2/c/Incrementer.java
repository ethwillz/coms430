package hw2.c;

/**
 * Simple demonstration of a race condition in incorrectly
 * synchronized code.
 */
public class Incrementer
{

  private static final int length = 100000;
  private static final int numberOfThreads = 100;
  private static final int numberOfOperations = 10000;
  
  private int[] theArray;

  CountDownLatch startSignal, doneSignal;

  public static void main(String[] args)
  {
    Incrementer i = new Incrementer();
    i.go();
  }
  
  public Incrementer()
  {
    startSignal = new CountDownLatch(1);
    doneSignal = new CountDownLatch(numberOfThreads);
  }
  
  public void go() {
    theArray = new int[length];
    
    // Create a bunch of incrementer threads
    int numberOfIterations = numberOfOperations / numberOfThreads;
    for (int i = 0; i < numberOfThreads; ++i)
    {
      new IncrementWorker(numberOfIterations, this, startSignal, doneSignal).start();
    }

    startSignal.countDown();

    long startTime = System.currentTimeMillis();
    
    System.out.println("Starting " + numberOfThreads + " threads to increment all elements of a " +
        length + " element array " + numberOfIterations + " times");

    try {
      doneSignal.await();

      long elapsed = System.currentTimeMillis() - startTime;

      // Examine array contents
      int expected = numberOfOperations;
      int count = 0;
      for (int i = 0; i < length; ++i)
      {
        if (theArray[i] != expected)
        {
          ++count;
          System.out.println("theArray[" + i + "] = " + theArray[i]);
        }
      }
      System.out.println("There were " + count + " cells not containing " + expected);

      System.out.println("Time: " + elapsed);
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
  }


  /**
   * Iterates through the shared array and increments the value
   * in each cell.
   */
  public synchronized void incrementArray()
  {
    for (int i = 0; i < length; ++i)
    {
      ++theArray[i];
    }
  }


  /**
   * Thread that will attempt to increment each cell
   * of the shared array.
   */
  private class IncrementWorker extends Thread
  {
    private int iterations;
    private Incrementer incrementer;
    private final CountDownLatch startSignal, doneSignal;
    
    public IncrementWorker(int iterations, Incrementer incrementer, CountDownLatch startSignal, CountDownLatch doneSignal)
    {
      this.iterations = iterations;
      this.incrementer = incrementer;
      this.startSignal = startSignal;
      this.doneSignal = doneSignal;
    }
    
    public void run()
    {
      try {
        startSignal.await();
        for (int i = 0; i < iterations; ++i)
        {
          doIncrement();
        }
        doneSignal.countDown();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    private void doIncrement()
    {
      incrementer.incrementArray();
    }
  }

}

