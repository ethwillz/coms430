package hw4.subsetsum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import static java.util.Collections.emptyList;

public class SubsetSum
{
  /**
   * Given a nonempty set of integers, finds all subsets whose sum is the given
   * target value.  Returns a list (possibly empty) of lists of values from 
   * the given set.
   * @param set
   * @param target
   * @return
   */
  public static List<List<Integer>> findSubsetSumFJ(List<Integer> set, int target)
  {
    List<List<Integer>> toReturn = new ArrayList<>();
    ForkSubset fs = new ForkSubset(set, 0, set.size(), target, toReturn, set.get(set.size() - 1));
    ForkJoinPool pool = new ForkJoinPool();
    pool.invoke(fs);
    return toReturn;
  }
  
  /**
   * Given a nonempty set of integers, finds all subsets whose sum is the given
   * target value.  Returns a list (possibly empty) of lists of values from 
   * the given set.
   * @param set
   * @param target
   * @return
   */
  public static List<List<Integer>> findSubsetSum(List<Integer> set, int target)
  {
    List<List<Integer>> toReturn = new ArrayList<>();
    
    if (set.size() == 1)
    {
      // base case
      if (set.get(0).equals(target))
      {
        List<Integer> result = new ArrayList<>();
        result.add(target);
        toReturn.add(result);
      }
    }
    else
    {
      // create copy of set, but with one value removed
      ArrayList<Integer> temp = new ArrayList<>();
      temp.addAll(set);
      int lastValue = temp.remove(temp.size() - 1); // remove at index i

      // try finding subsets that add up to target, without the last value
      List<List<Integer>> resultsWithoutLastValue = findSubsetSum(temp, target);
      if (resultsWithoutLastValue.size() != 0)
      {
        toReturn.addAll(resultsWithoutLastValue);
      }
      
      // try finding subsets that add up to target, if last value is added too
      List<List<Integer>> resultsWithLastValue = findSubsetSum(temp, target - lastValue);
      if (resultsWithLastValue.size() != 0)
      {
        // put the missing value back into the solution
        for (List<Integer> lst : resultsWithLastValue)
        {
          lst.add(lastValue);
        }
        toReturn.addAll(resultsWithLastValue);
      }
      else
      {
        // no results, but the one element set with just 'lastValue' 
        // could be a solution too
        if (lastValue == target)
        {
          List<Integer> result = new ArrayList<>();
          result.add(target);
          toReturn.add(result);
        }
      }
    }    
    return toReturn;
  }
}

class ForkSubset extends RecursiveAction {
  private List<Integer> mSource;
  private int mStart;
  private int mLength;
  private int mTarget;
  private int lastValue;
  private List<List<Integer>> mDestination;

  protected static int sThreshold = 2;

  public ForkSubset(List<Integer> mSource, int mStart, int mLength, int mTarget, List<List<Integer>> mDestination, int lastValue){
    this.mSource = mSource;
    this.mStart = mStart;
    this.mLength = mLength;
    this.mTarget = mTarget;
    this.mDestination = mDestination;
    this.lastValue = lastValue;
  }

  @Override
  protected void compute() {
    if(mLength < sThreshold){
      if(mSource.get(0) == mTarget) {
        mDestination.add(new ArrayList<Integer>(){{
          add(mTarget);
          if(lastValue == mTarget) add(lastValue);
        }});
      }
      else if(lastValue == mTarget){
        mDestination.add(new ArrayList<Integer>(){{ add(lastValue); }});
      }
      return;
    }

    List<ForkSubset> forks = new ArrayList<>();

    // try finding subsets that add up to target, without the last value
    int lastValue = mSource.get(mStart + mLength - 1);
    forks.add(new ForkSubset(mSource, mStart, mLength - 1, mTarget, mDestination, 0));
    forks.add(new ForkSubset(mSource, 0, mLength - 1, mTarget - mSource.get(mSource.size() - 1), mDestination, lastValue));

    invokeAll(forks);
  }

}
