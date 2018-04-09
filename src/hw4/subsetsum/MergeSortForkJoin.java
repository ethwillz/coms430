package hw4.subsetsum;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;



public class MergeSortForkJoin
{

  public static void mergeSort(int[] arr)
  {
    new ForkJoinPool(8).invoke(new SortTask(arr));
  }

  
  private static class SortTask extends RecursiveAction
  {
    private int[] arr;
    public SortTask(int[] a)
    {
      arr = a;
    }
    @Override
    protected void compute()
    {
      // base case
      if (arr.length <= 16)
      {
        selectionSort(arr);
        return;
      }
      
      // split into two arrays
      int mid = arr.length / 2;
      int[] first = Arrays.copyOfRange(arr, 0, mid);
      int[] second = Arrays.copyOfRange(arr, mid, arr.length);
      
      // Recursive task. 
      SortTask t1 = new SortTask(first);
      SortTask t2 = new SortTask(second);
      
      // We are already executing within a ForkJoinPool, so we
      // don't need to specify the pool when calling invokeAll.
      // The following is equivalent to
      //
      // t2.fork();
      // t1.invoke();
      // t2.join();
      // 
      invokeAll(t1, t2);

      merge(first, second, arr);
    }
    
  }
  
  /**
   * Merges two sorted arrays into a third one.
   * The given arrays 'first' and 'second' must already be sorted, the
   * array 'result' must already exist and must be the correct size,  
   * i.e., result.length = first.length + second.length.
   * @param a
   * @param b
   * @param result
   */ 
  private static void merge(int[] a, int[] b, int[] result)
  {
    int i = 0;                  // starting index in a
    int j = 0;                  // starting index in b
    final int iMax = a.length;  // max index in a
    final int jMax = b.length;  // max index in b
    int k = 0;                  // index in result
    
    while (i < iMax && j < jMax)
    {
      if (a[i] <= b[j])
      {
        result[k] = a[i];
        i = i + 1;
        k = k + 1;
       }
      else
      {
        result[k] = b[j];
        j = j + 1;
        k = k + 1;
      }
    }
    
    // pick up any stragglers
    while (i < iMax)
    {
      result[k] = a[i];
      i = i + 1;
      k = k + 1;
    }
    while (j < jMax)
    {
      result[k] = b[j];
      j = j + 1;
      k = k + 1;
    }    
  }
  
  private static void selectionSort(int[] arr)
  {
    for (int i = 0; i < arr.length - 1; ++i)
    {
      // find index of minimal element to the right of i
      int indexOfMin = i;
      for (int j = i + 1; j < arr.length; ++j)
      {
        if (arr[j] < arr[indexOfMin])
        {
          indexOfMin = j;
        }
      }
      
      // swap into position i
      int temp = arr[i];
      arr[i] = arr[indexOfMin];
      arr[indexOfMin] = temp;
    }
  }
}


