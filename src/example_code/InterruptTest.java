package example_code;

/**
 * Experiment with the behavior of the interrupt() method
 * of the Thread class.
 */
public class InterruptTest
{
  public static void main(String[] args)
  {
    final Thread t = Thread.currentThread();
    System.out.println("Interrupt status: " + t.isInterrupted());
    t.interrupt();
    System.out.println("Interrupt status after interrupting self: "+ t.isInterrupted());
    System.out.println("Now try to sleep for 30 seconds...");
    try
    {
      Thread.sleep(30000);
    }
    catch (InterruptedException e)
    {
      System.out.println("Can't sleep() because interrupt status was set: " + e);
    }
    
    System.out.println("Interrupt status: " + t.isInterrupted());
    
    // Create another thread to interrupt us after 5 seconds
    Runnable r = new Runnable()
    {
      public void run()
      {
        try
        {
          System.out.println("Second thread starting, sleeping for 5 seconds...");
          Thread.sleep(5000);
        }
        catch (InterruptedException ignore){}
        System.out.println("About to interrupt first thread");
        t.interrupt();
      }
    };
    new Thread(r).start();
    
    System.out.println("Start other thread, and try to sleep for 30 seconds...");
    try
    {
      Thread.sleep(30000);
    }
    catch (InterruptedException e)
    {
      System.out.println("Presumably only about 5 seconds have passed: " + e);
    }
  }
}
