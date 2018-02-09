package hw2.a.function_plotter;

public class Foo implements SVFunction
{

  @Override
  public double yValue(double x)
  {
    // TODO Auto-generated method stub
    return Math.abs(Math.sin(x));
  }

}
