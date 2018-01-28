package basic_thread_examples;

/**
 * Demonstrates a simple problem with memory visibility.  Without any
 * synchronization the auxiliary thread goes into an infinite loop
 * because it never "sees" that the shared variable 'number' has
 * increased to 3000.
 * 
 * Try with a smaller number.
 * 
 * Try with JVM option -XX:CompileOnly=foo to disable the JIT compiler
 * (Use -XX:+PrintCompilation to view compilation.)
 */
public class NoVisibility
{
  private int number;
  private int count;
  
  public static void main(String[] args)
  {
    NoVisibility nv = new NoVisibility();
    nv.go(); 
  }
  
  private void go()
  {
    new ReaderThread().start();
    
    System.out.println("Main thread starting loop");

    for (int i = 0; i <= 3000; ++i)
    {
      number = i;
      Thread.yield();
    }
  }

  public int get()
  {
    while (number < 3000)
    {
      //System.out.println("***" + number);
      ++count;
    }
    return number;
  }
  
  class ReaderThread extends Thread
  {
    public void run()
    {
      System.out.println("Reader starting...");
      int result = get();
      System.out.println("Reader sees number: " + result);
    }
  }
}

