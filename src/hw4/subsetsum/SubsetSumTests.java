package hw4.subsetsum;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hw4.subsetsum.SubsetSum.findSubsetSum;
import static hw4.subsetsum.SubsetSum.findSubsetSumFJ;
import static java.util.Collections.emptyList;


public class SubsetSumTests
{
  private static final List<Integer> LARGE_SET = Arrays.asList(222, 688, 936, 833, 218,
      729, 335, 508, 612, 695,
      853, 305, 765, 813, 623,
      119, 962, 364, 380,   3,
      355, 604, 230, 969, 851,
      83, 505);

  
  private static final List<Integer> LARGER_SET = Arrays.asList(222, 688, 936, 833, 218,
                                                               729, 335, 508, 612, 695,
                                                               853, 305, 765, 813, 623,
                                                               119, 962, 364, 380,   3,
                                                               355, 604, 230, 969, 851,
                                                               83, 505, 10, 10, 10);

  public static void main(String args[]) throws FileNotFoundException
  {
    basicSetTest();
    largeSetTest();
    largerSetTest();
  }

  public static void basicSetTest()
  {
    System.out.println("~~~ Basic Test non-FJ ~~~");
    int[] test = {1, 6, 3, 10, 4};
    ArrayList<Integer> lst = new ArrayList<>();
    for (int i : test) lst.add(i);

    List<List<Integer>> results = SubsetSum.findSubsetSum(lst, 10);
    System.out.println("Results: " + results.size());
    for (List<Integer> l : results)
    {
      System.out.println(l);
    }
    System.out.println();

    System.out.println("~~~ Basic Test FJ ~~~");
    results = findSubsetSumFJ(lst, 10);
    System.out.println("Results: " + results.size());
    for (List<Integer> l : results)
    {
      System.out.println(l);
    }
    System.out.println();
  }

  private static void largeSetTest()
  {
    System.out.println("~~~ Large Set Test non-FJ ~~~");
    long start = System.currentTimeMillis();
    List<List<Integer>> results = findSubsetSum(LARGE_SET, 2000);
    System.out.println("Duration (ms): " + (System.currentTimeMillis() - start));
    System.out.println("Num Subsets Found: " + results.size());
    System.out.println();

    System.out.println("~~~ Large Set Test FJ ~~~");
    start = System.currentTimeMillis();
    results = findSubsetSumFJ(LARGE_SET, 2000);
    System.out.println("Duration (ms): " + (System.currentTimeMillis() - start));
    System.out.println("Num Subsets Found: " + results.size());
    System.out.println();
  }
  
  private static void largerSetTest()
  {
    System.out.println("~~~ Larger Set Test non-FJ ~~~");
    long start = System.currentTimeMillis();
    List<List<Integer>> results = findSubsetSum(LARGER_SET, 2000);
    System.out.println("Duration (ms): " + (System.currentTimeMillis() - start));
    System.out.println("Num Subsets Found: " + results.size());
    System.out.println();

    System.out.println("~~~ Larger Set Test FJ ~~~");
    start = System.currentTimeMillis();
    results = findSubsetSumFJ(LARGER_SET, 2000);
    System.out.println("Duration (ms): " + (System.currentTimeMillis() - start));
    System.out.println("Num Subsets Found: " + results.size());
    System.out.println();
  }
}

