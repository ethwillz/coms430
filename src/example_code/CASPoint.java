package example_code;

import java.util.concurrent.atomic.AtomicReference;

// Example of an optimistic update:
//
// Get copy of object state (atomically, e.g. holding lock)
// Calculate modification to state (without lock)
// Update state, IF it has not been modified in the meantime (atomically)
//
// Operation may have to be retried, subject (theoretically) to livelock

public class CASPoint
{
  private final AtomicReference<ImmutablePoint> ref;
  
  public CASPoint(int x, int y)
  {
    ref = new AtomicReference<ImmutablePoint>(new ImmutablePoint(x, y));
  }
  
  public ImmutablePoint get()
  {
    return ref.get();
  }

  // typical operation where update depends on current value
  public void shift(int dx, int dy)
  {
    boolean success = false;
    while (!success)
    {
      ImmutablePoint p = ref.get();
      ImmutablePoint newP = new ImmutablePoint(p.getX() + dx, p.getY() + dy);
      success = ref.compareAndSet(p, newP);
    }
  }

  // immutable representation of point data
  class ImmutablePoint
  {
    private final int x;
    private final int y;
    public ImmutablePoint(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
    public int getX()
    {
      return x;
    }
    public int getY()
    {
      return y;
    }
  }
}