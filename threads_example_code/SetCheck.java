
public class SetCheck
{
  private int a = 0;
  private long b = 0;

  public void set()
  {
    a = 1;
    b = 42;
  }

  // can this ever return "FAIL"?
  public String check()
  {
    if (b == 42 && a == 0) return "FAIL";

    return "OK";
  }

}
