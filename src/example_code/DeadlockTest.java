package example_code;

public class DeadlockTest
{

  public static void main(String[] args)
  {
    final CountingBufferWithError buf = new CountingBufferWithError(10);
    
    Runnable reader = new Runnable()
    {
      public void run()
      {
        System.out.println("Reader starting");
        try
        {
          Object result = buf.take();
          System.out.println(result);
        }
        catch (InterruptedException ignore){}
        System.out.println("Reader ending");
      }
    };
    new Thread(reader).start();
    
    
    Runnable writer = new Runnable()
    {
      public void run()
      {
        System.out.println("Writer starting");
        try
        {
          buf.put("Hello");
        }
        catch (InterruptedException ignore){}
        System.out.println("Writer ending");
      }
    };
    new Thread(writer).start();
    
    System.out.println("main thread exiting");
  }

  
  
}
