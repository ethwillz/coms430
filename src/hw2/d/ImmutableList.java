package hw2.d;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Partial implementation of the List interface using
 * single links and a dummy node.
 */
public class ImmutableList<E>
{
  private Node head;

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
    final E data;
    final Node next;

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

    public LinkedIterator() { cursor = copyList(head); }

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
      synchronized(ImmutableList.this) { head = copyAndSearch(head, item); }
    }

    /**
     * Copies all nodes in an immutable linked list
     * @param start starting node to copy immutable list from
     * @return head node of copy of linked list
     */
    private Node copyList(Node start){
      if(start == null) return null;
      return new Node(start.data, copyList(start.next));
    }

    private Node copyAndSearch(Node start, E searchObj){
      if(start.data.equals(searchObj)) return new Node(start.data, new Node(searchObj, start.next));
      if(start == null) throw new ConcurrentModificationException();
      return new Node(start.data, copyAndSearch(start.next, searchObj));
    }
  }

}