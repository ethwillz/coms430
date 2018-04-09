package hw4.employees;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTest
{
  public static void main(String[] args)
  {
    
    int[] test = {3, 4, 10, 3, 5, 1, 8, 6};
    System.out.println(Arrays.toString(test));
    int[] result = evensToFront(test);
    System.out.println("Result  : " + Arrays.toString(result));
    System.out.println("Expected: [4, 10, 8, 6, 3, 3, 5, 1]");
  }
  
  public static int[] evensToFront(int[] arr)
  {
    return Arrays.stream(arr)
            .boxed()
            .sorted((o1, o2) -> {
              if(o1.intValue() == o2.intValue()) return 0;
              else if((o1 % 2 == 0 && o2 % 2 == 0) || (o1 % 2 != 0 && o2 % 2 != 0) || o2 % 2 == 0) return 1;
              else return -1;
            })
            .mapToInt(Integer::intValue)
            .toArray();
  }
}

