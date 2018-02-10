package hw2.d;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Partial implementation of the List interface using 
 * single links and a dummy node. 
 */
public class ImmutableList<E>
{
  private final Node head;
  
  public ImmutableList()
  {
    head = new Node(null, null);
  }   
  
  public SimpleIterator<E> listIterator()
  {
    return new LinkedIterator();
  }
  
  /**
   * Node type for this class.
   */
  private class Node
  {
    private final E data;
    private final Node next;

    public Node(E pData, Node pNext)
    {
      data = pData;
      next = pNext;
    }
  }
  
  /**
   * Implementation of ListIterator for this class
   */
  private class LinkedIterator implements SimpleIterator<E>
  {
    // points to node preceding the next element
    private Node cursor;

    public LinkedIterator()
    {
      cursor = head; 
    }
    
    public boolean hasNext()
    {
      return cursor.next != null;
    }
    
    public E next()
    {
      if (!hasNext()) throw new NoSuchElementException();
      
      cursor = cursor.next;
      return cursor.data;
    }
        
    public void add(E item)
    {
      synchronized(ImmutableList.this){
        Node temp = cursor;
        cursor = head;
        while(cursor.next != temp){
          if(cursor.next == null) throw new ConcurrentModificationException();
          cursor = cursor.next;
        }
        cursor = cursor.next;
        cursor = new Node(item, cursor.next);
      }
    }
  }

}
