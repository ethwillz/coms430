package hw1;

/**
 * TODO: Put your response here.
 */
public class NoVisibility2
{
  private int number;
  private int count;
  private final int ITERATIONS = 10000;
  
  public static void main(String[] args)
  {
    NoVisibility2 nv = new NoVisibility2();
    nv.go(); 
  }
  
  private void go()
  {
    new ReaderThread().start();
    
    System.out.println("Main thread starting loop");

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
        ++count;
        synchronized(this)
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
      System.out.println("Reader sees number: " + result);
    }
  }
}


