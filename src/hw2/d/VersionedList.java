package hw2.d;
import java.util.NoSuchElementException;

/**
 * Partial implementation of the List interface using 
 * single links and a dummy node. 
 */
public class VersionedList<E>
{
  private final Node head;

  private volatile int version = 0;
  
  public VersionedList()
  {
    head = new Node(version++, null, null);
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
    final int id;
    final E data;
    Node next;

    public Node(int id, E pData, Node pNext)
    {
      this.id = id;
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
    private int version;

    public LinkedIterator()
    {
      cursor = head;
      version = findMaxId(head);
    }
    
    public boolean hasNext()
    {
      return cursor.next != null;
    }
    
    public E next() {
      if (!hasNext()) throw new NoSuchElementException();

      cursor = cursor.next;
      while (true) {
        synchronized (cursor) {
          if(cursor.next == null) throw new NoSuchElementException();
          if (cursor.next.id < version) break;
          cursor = cursor.next;
        }
      }
      cursor = cursor.next;
      return cursor.data;
    }
        
    public void add(E item)
    {
      synchronized(cursor){ cursor.next = new Node(version++, item, cursor.next); }
    }

    private int findMaxId(Node head){
      int maxId = Integer.MIN_VALUE;
      while(head != null){
        if(head.id > maxId) maxId = head.id;
        head = head.next;
      }
      return maxId;
    }
  }

}
