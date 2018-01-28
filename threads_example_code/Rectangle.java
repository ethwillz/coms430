 /**
 * A class that violates the "second" aspect of Rule 1.  Even though the same intrinsic lock
 * is acquired for all data access, the getArea method may return an incorrect
 * value since the lock is not held for the duration of the two accesses to width and
 * height.
 */
public class Rectangle
{
  private int width;
  private int height;
  
  public Rectangle(int width, int height)
  {
    this.width = width;
    this.height = height;
  }
  
  public synchronized int getWidth() 
  { 
    return width; 
  }
  
  public synchronized int getHeight() 
  { 
    return height; 
  }
  
  public synchronized void grow(int amount)
  {
    width += amount;
    height += amount;
  }
  
  public int getArea()
  {
    return getWidth() * getHeight();
  }
}
