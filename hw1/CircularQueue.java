package hw1;

import java.util.NoSuchElementException;

/**
 * A standard singly-linked circular FIFO queue with tail pointer.
 * For an empty queue, the tail pointer is null; for a nonempty
 * queue, the head of the queue is tail.next.  (When the queue has 
 * one element, tail.next points to tail.)
 *
 * TODO: Put your response for Problem 4a here.
 *
 * TODO: Put your response for Problem 4b here.
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
