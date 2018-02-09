package example_code.BoundedBuffer;

// INCORRECT attempt to implement a bounded buffer using
// suspend() and resume().  This is subject to race conditions
// including 
//     "missed signals" - resume() occurs just before thread suspends()
//     "slipped conditions" - check-then-act race condition 
//
public class BoundedBufferWrong
{
  private Thread reader;
  private Thread writer;
  
  /**
   * Elements in the buffer
   */
  private Object[] queue;
  
  /**
   * The number of elements in the queue
   */
  private int dataCount; 

  /**
   * Index into the buffer for inserting
   */
  private int putIndex;
  
  /**
   * Index into the buffer for removing
   */
  private int getIndex;
   
  /**
   * The size of the buffer being used.
   */
  private final int bufferSize;
  
  
  public BoundedBufferWrong(int size, Thread reader, Thread writer)
  {
    this.reader = reader;
    this.writer = writer;   
    bufferSize = size;
    queue = new Object[bufferSize];
  }
  
  /**
   * Write an object to the data queue.  If the queue is full, this method 
   * blocks until space is available.
   *
   * @param data
   *    The data to be entered into the queue.
   */
  public void put( Object data ) throws InterruptedException
  {
      // Wait if the buffer is full.
      if (dataCount == queue.length)
      {
        Thread.currentThread().suspend();
      }

      // Write the data.
      queue[putIndex] = data;
      putIndex = (putIndex + 1) % queue.length;
      ++dataCount;

      // Notify reader that the buffer is no longer empty, 
      reader.resume();
  }


  /**
   * Reads an object from the data queue.  If the queue is empty,
   * this method blocks  until an object is
   * available.
   *
   * @return
   *    The object removed from the queue.
   */
  public Object take( ) throws InterruptedException
  {
    Object data;
      // Wait if the buffer is empty
      if( dataCount == 0 )
      {
        Thread.currentThread().suspend();
      }

      // Read the next data, wrap the index around if necessary
      data = queue[getIndex];
      getIndex = (getIndex + 1) % queue.length;
      --dataCount;

      // Notify writer that the buffer is no longer full
      writer.resume();
    return data;
  }
}
