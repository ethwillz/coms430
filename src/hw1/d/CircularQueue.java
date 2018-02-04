package hw1.d;

import java.util.NoSuchElementException;

/**
 * A standard singly-linked circular FIFO queue with tail pointer.
 * For an empty queue, the tail pointer is null; for a nonempty
 * queue, the head of the queue is tail.next.  (When the queue has 
 * hw1.d.a element, tail.next points to tail.)
 *
 * 4a.
 * One possible interleaving of instructions assuming rule 2 isn't taken into account is if 2 threads try and add to
 * the queue simultaneously. The first thread could see tail is null and then experiences a context switch to where the
 * second thread also sees tail to be null. Which value will become the only element in the queue is dependent on which
 * thread writes last.
 *
 * 4b.
 *
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
