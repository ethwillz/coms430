package parallel_streams_presentation.code_examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html
 */
public class CollectorsExample {
    public static void main(String[] args){
        ArrayList<Employee> employees = new ArrayList<>();
        char begin = 'a';
        char name = 'a';
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 1000; j++){
                employees.add(new Employee(begin, name));
                name++;
            }
            begin++;
        }




        employees.stream()
                .parallel()
                .collect(Collectors
                        .groupingByConcurrent(Employee::getDepartment)
                );

        employees.stream()
                .parallel()
                .collect(Collectors
                        .groupingBy(Employee::getDepartment)
                );

        employees.stream()
                .parallel()
                .collect(Collectors
                        .groupingByConcurrent(Employee::getDepartment)
                );






        long start = System.nanoTime();
        Map<Character, List<Employee>> byDept
                = employees.stream()
                .collect(Collectors
                        .groupingBy(Employee::getDepartment)
                );
        long deltaT = System.nanoTime() - start;
        System.out.println("Serial stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        ConcurrentMap<Character, List<Employee>> byDeptParallel
                = employees.stream()
                .parallel()
                .collect(Collectors
                        .groupingByConcurrent(Employee::getDepartment)
                );
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/o concurrent grouping took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        ConcurrentMap<Character, List<Employee>> byDeptConcurrent
                = employees.stream()
                .parallel()
                .collect(Collectors
                        .groupingByConcurrent(Employee::getDepartment)
                );
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ concurrent grouping took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

    }

    private static class Employee{
        char department;
        char name;

        public Employee(char department, char name){
            this.department = department;
            this.name = name;
        }
        public char getDepartment(){
            return department;
        }

        public char getName(){
            return name;
        }
    }
}
