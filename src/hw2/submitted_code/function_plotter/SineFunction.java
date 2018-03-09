package hw2.submitted_code.function_plotter;

public class SineFunction implements SVFunction
{
  private double amplitude;
  private double period;

  public SineFunction(double a, double p)
  {
    amplitude = a;
    period = p;
  }

  public double yValue(double x)
  {
    return amplitude * Math.sin((2 * Math.PI * x) / period);
  }

}
