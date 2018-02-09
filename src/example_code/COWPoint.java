package example_code;

// the copy-on-write idiom uses an immutable representation
// of state
public class COWPoint
{
  // Why do we make this volatile?
  private volatile ImmutablePoint point;
  
  public ImmutablePoint get()
  {
    return point;
  }
  
  public void setPosition(int x, int y)
  {
    point = new ImmutablePoint(x, y);
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

//thread-safe, but useless, version of a point type
class SyncedPoint
{
  private int x, y;
  public synchronized void setPosition(int newX, int newY)
  {
    x = newX;
    y = newY;
  }
  public synchronized int getX()
  {
    return x;
  }
  public synchronized int getY()
  {
    return y;
  }
}
