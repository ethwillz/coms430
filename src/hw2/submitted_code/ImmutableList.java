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

    public LinkedIterator() {
      synchronized(ImmutableList.this){
        cursor = copyList(head);
      }
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
      synchronized(ImmutableList.this) {
        if (isNextReachable(cursor)) {
          head = copyListAndAdd(head, item);
        } else {
          throw new ConcurrentModificationException();
        }
      }
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

    /**
     * Checks to make sure the next reference from cursor is reachable from head
     * @param cursor Node whose next is being evaluated
     * @return true if the next reference is reachable
     */
    private boolean isNextReachable(Node cursor){
      Node cur, cursorNext = cursor.next;
      synchronized(ImmutableList.this) { cur = head; }
      while(cur.next != cursorNext) {
        if(cur.next == null) return false; //because .equals args must be non-null so can't handle in while loop
        cur = cur.next;
      }
      return cur.next.equals(cursorNext);
    }

    /**
     * Copies list from head adding in a new item after the cursor
     * @param cur Current cursor of the list
     * @param item Item to be added to list
     * @return start of list with new node added
     */
    private Node copyListAndAdd(Node cur, E item){
      if(cur.equals(cursor)) return new Node(cur.data, new Node(item, cur.next));
      return new Node(cur.data, copyListAndAdd(cur.next, item));
    }
  }

}
