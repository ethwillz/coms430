package hw3.submitted_code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;

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
    try {
      int numCPUs = Runtime.getRuntime().availableProcessors();
      ExecutorService exec = Executors.newFixedThreadPool(numCPUs);
      int samplesPerThread = numSamples / numCPUs;
      int rangePerThread = maxValue / numCPUs;

      long start = System.currentTimeMillis();
      ArrayList<Future<int[]>> resultFutures = (ArrayList<Future<int[]>>) exec.invokeAll(
              Collections.nCopies(numCPUs, new ThreadOperation(rangePerThread, samplesPerThread))
      );

      final int[] index = {0};
      resultFutures.forEach(x -> {
        try {
          System.arraycopy(x.get(), 0, results, index[0]++ * rangePerThread, rangePerThread);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
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
      exec.shutdown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  class ThreadOperation implements Callable<int[]> {
    private int rangePerThread, samplesPerThread;

    public ThreadOperation(int rangePerThread, int samplesPerThread){
      this.rangePerThread = rangePerThread;
      this.samplesPerThread = samplesPerThread;
    }

    @Override
    public int[] call() throws Exception {
      int[] threadSpecificResults = new int[rangePerThread];
      Random random = new Random();
      for (int i = 0; i < samplesPerThread; ++i) { threadSpecificResults[random.nextInt(rangePerThread)]++; }
      return threadSpecificResults;
    }
  }

}
