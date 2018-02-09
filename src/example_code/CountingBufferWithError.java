package example_code;

import example_code.BoundedBuffer.BoundedBuffer;

// Demonstrates nested monitor lockout.
// Attempt to create a new class by composing a BoundedBuffer object.
// THIS WILL DEADLOCK if take() is called before put()
public class CountingBufferWithError
{
  private BoundedBuffer buf;
  private int count;
  
  public CountingBufferWithError(int size)
  {
    buf = new BoundedBuffer(size);
    count = 0;
  }
  
  public synchronized void put(Object obj) throws InterruptedException
  {
    count += 1;
    buf.put(obj);
  }
  
  public synchronized Object take() throws InterruptedException
  {
    return buf.take();
  }
  
  public synchronized int getCount()
  {
    return count;
  }
}
