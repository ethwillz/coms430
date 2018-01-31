package hw1.d;

import java.util.NoSuchElementException;

/**
 * A standard singly-linked circular FIFO queue with tail pointer.
 * For an empty queue, the tail pointer is null; for a nonempty
 * queue, the head of the queue is tail.next.  (When the queue has 
 * hw1.d.a element, tail.next points to tail.)
 *
 * 4a.
 * One possible interleaving of instructions assuming RUle 2 isn't taken into account is if b threads try and add to
 * the queue simultaneously. The first thread could see tail is null and then experiences a context switch to where the
 * second thread also sees tail to be null. Which value will become the only element in the queue is dependent on which
 * thrad finishes last.
 *
 * 4b.
 * If a thread adds to the queue which should update the tail. Then another thread may come along and try and remove
 * from the list, which should be a valid operation throwing no exceptions. If the data for tail is null, an exception
 * may be thrown regardless since the second thread isn't aware of tail's assignment by the other thread.
 */
public class CircularQueue<T>
{
  private Node tail = null;
  
  public void add(T item)
  {
    Node temp = new Node(item);
    if (tail == null)
    {
      // empty
      temp.next = temp;
    }
    else
    {
      temp.next = tail.next;
      tail.next = temp;
    }
    tail = temp;
  }
  
  public T remove()
  {
    if (tail == null) throw new NoSuchElementException();
    T ret = tail.next.data;
    if (tail.next == tail)
    {
      tail = null;
    }
    else
    {
      tail.next = tail.next.next;
    }
    
    return ret;
  }
  
  public boolean isEmpty()
  {
    return tail == null;
  }
  
  class Node
  {
    T data;
    Node next;
    
    public Node(T item)
    {
      data = item;
      next = null;
    }
  }
}
