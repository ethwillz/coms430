package hw3;

import java.util.Random;

/**
 * Creates a histogram of values produced by Java's random
 * number generator.
 */
public class Histogram
{  
  public static void main(String[] args)
  {
    new Histogram(10000, 100000000).go();
  }
  
  private int maxValue;
  private int numSamples;
  private int[] results;
  
  public Histogram(int givenMax, int givenNum)
  {
    maxValue = givenMax;
    numSamples = givenNum;
    results = new int[maxValue];
  }
  
  public void go()
  {
    long start = System.currentTimeMillis();

    Random rand = new Random();
    for (int i = 0; i < numSamples; ++i)
    {
      int next = rand.nextInt(maxValue);
      results[next]++;
    }
    
    long elapsed = System.currentTimeMillis() - start;

    int total = 0;
    for (int i = 0; i < results.length; ++i)
    {
      total += results[i];
      System.out.println(i + ": " + results[i]);
    }
    System.out.println();
    System.out.println("Check total samples: " + total);
    System.out.println("Elapsed: " + elapsed);

  }

}
