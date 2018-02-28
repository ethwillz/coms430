package parallel_streams_presentation.code_examples;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html
 */
public class CollectorsExample {
    public static void run(ArrayList<Employee> employees){
        employees.stream()
                .parallel()
                .filter(x -> x.getDepartment().equals("keep"))
                .limit(10000)
                .collect(Collectors.groupingByConcurrent(Employee::getDepartment));
    }
}
