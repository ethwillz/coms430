package example_code;

// Fails, because there is still a potential access path to the
// data after reset(), since the caller may retain an alias to the array.
public class FooContinued
{
  private int[] a = new int[1];
  public synchronized void increment()
  {
    a[0] += 1;
  }
  public synchronized int get()
  {
    return a[0];
  }
  public synchronized void reset(int[] arr)
  {
    a = arr;
  }
}

// Fails, since the data is static, but threads with different instances of Bar
// will be using the lock associated with the instance ('this').
class Bar
{
  // number of operations over all instances of Bar
  private static int count;

  public synchronized void doOperation()
  {
    count += 1;
    doStuff();
  }

  public synchronized int getCount()
  {
    return count;
  }

  private void doStuff() {/* whatever */ }
}

// This works, using the class object's intrinsic lock.
class Bar1
{
  // number of operations over all instances of Bar
  private static int count;

  public  void doOperation()
  {
    synchronized(Bar1.class) //can also just make signature public static synchronzied void doOperation
    {
      count += 1;
    }
    doStuff();
  }

  // "static synchronized" is a shorthand for syncing on the class object
  public static synchronized int getCount()
  {
    return count;
  }

  private void doStuff() {/* whatever */ }
}
