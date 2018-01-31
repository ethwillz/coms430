package hw1.five;

public class NoSuchEntryException extends Exception
{
  public NoSuchEntryException()
  {
    super();
  }
  
  public NoSuchEntryException(String msg)
  {
    super(msg);
  }
}
