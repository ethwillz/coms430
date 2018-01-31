package threads_example_code;

import java.util.ArrayList;

// A simple counter, not thread safe
public class Foo
{
  private int i;
  public void increment()
  {
    i += 1;
  }
  public int get()
  {
    return i;
  }
}


// a futile attempt to add a kind of locking
class Foo0
{
  private int i;
  private boolean locked = false;
  public void increment()
  {
    while (locked) { /* wait */ }
    locked = true;
    i += 1;
    locked = false;
  }
  
  public int get()
  {
    return i;
  }
}


// Using an arbitrary Java object as a synchronization lock.  This works.
class Foo1
{
  private int i;
  private Object myLock = new Object();
  public void increment()
  {
    synchronized(myLock)
    {
      i += 1;
    }  // lock automatically released!
  } 
  public int get()
  {
    synchronized(myLock)
    {
      return i;
    }
  }
}



// A more idiomatic approach for Java, using 'this' as the sync lock.
class Foo2
{
  private int i;
  public void increment()
  {
    synchronized(this)
    {
      i += 1;
    }
  }
  public int get()
  {
    synchronized(this)
    {
      return i;
    }
  }
}

// Same as above, using the shorthand for syncing an entire method body.
class Foo3
{
  private int i;
  public synchronized void increment()
  {
    i += 1;
  }
  public synchronized int get()
  {
    return i;
  }
}

// Same as above; note that the lock is "reentrant", meaning that the same
// thread can acquire it multiple times
class Foo3a
{
  private int i;
  public synchronized void increment()
  {
    int temp = get();
    i = temp + 1;
  }
  public synchronized int get()
  {
    return i;
  }
}


// Fails, because there is still an access path to the data that does
// not use synchronization.  (The "radio fence" problem.)
class Foo4
{
  private int i;
  public synchronized void increment()
  {
    i += 1;
  }
  public synchronized int get()
  {
    return i;
  }
  public void reset(int n)
  {
    i = n;
  }
}


