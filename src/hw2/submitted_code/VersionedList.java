package hw2.submitted_code;

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
      synchronized(head) {
        cursor = head;
      }
      version = findMaxId(head);
    }

    public boolean hasNext()
    {
      synchronized(cursor) {
        return cursor.next != null;
      }
    }

    public E next() {
      synchronized(cursor) {
        if (!hasNext()) throw new NoSuchElementException();
        cursor = cursor.next;
      }

      while (true) synchronized (cursor) { //needs to update lock each node traversed
          if (cursor.next == null) throw new NoSuchElementException();
          if (cursor.next.id < version) break;
          cursor = cursor.next;
      }

      synchronized(cursor) {
        cursor = cursor.next;
        return cursor.data;
      }
    }

    public void add(E item)
    {
      synchronized(cursor) { cursor.next = new Node(version++, item, cursor.next); }
    }

    /**
     * Finds maximum ID in list in the current version of the list
     * @param start starting node to find max ID from (should always be head)
     * @return
     */
    private int findMaxId(Node start){
      int maxId = Integer.MIN_VALUE;
      while(true) synchronized (start) {
        if (start == null) break; //needs to be under lock for consistency so can't be in loop condition
        if (start.id > maxId) maxId = start.id;
        start = start.next;
      }
      return maxId;
    }
  }

}
