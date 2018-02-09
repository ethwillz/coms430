package hw2.list;

import java.util.Iterator;

public interface SimpleIterator<E> extends Iterator<E>
{
  void add(E item);
}
