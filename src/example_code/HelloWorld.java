package example_code;
/**
 * Simple program with hw1.b independent threads.
 */
public class HelloWorld
{

  /**
   * Starts hw1.b threads.
   * @param args
   *     ignored
   */
  public static void main(String[] args)
  {
    // create an implementation of Runnable and pass
    // to the Thread constructor (alternatively, 
    // extend the Thread class and override run())
    Runnable r = new MyTask("Hello", 300);
    Thread helloThread = new Thread(r);
    
    //
    // Alternative: creating a thread anonymously
    //
//    final String myMessage = "Hello";
//    
//    Thread helloThread = new Thread(new Runnable()
//    {
//      public void run()
//      {
//        while (true)
//        {
//          System.out.println(myMessage);
//          takeNap((long)(Math.random() * delay));
//        }        
//      }
//    });
//    
    

    // Create a second thread
    r = new MyTask("      World", 300);
    Thread worldThread = new Thread(r);
    
    // names are useful for debugging
    worldThread.setName("World Thread");
    helloThread.setName("Hello Thread");
    
    // start both threads
    helloThread.start();
    worldThread.start();
    System.out.println("Main.java thread exiting...");
    

  }

}



class MyTask implements Runnable
{
  private long delay;
  private String display;
  
  public MyTask(String display, long delay)
  {
    this.display = display;
    this.delay = delay;
  }
  
  private void takeNap(long napTime)
  {
    try
    {
      Thread.sleep(napTime);
    }
    catch (InterruptedException ignore) {}
  }
  
  @Override
  public void run()
  {
    while (true)
    {
      //System.out.println(Thread.currentThread().getName());
      System.out.println(display);
      if (delay > 0)
      {
        takeNap((long)(Math.random() * delay));
      }
    }
  }
}