package hw3.submitted_code.components;

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
