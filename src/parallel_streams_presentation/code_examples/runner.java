package parallel_streams_presentation.code_examples;

import java.util.*;
import java.util.stream.Collectors;

public class runner {
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        Random r = new Random();
        String dept = "keep";
        char name = 'a';
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 1000; j++){
                employees.add(new Employee(dept, name));
                name++;
                //if(r.nextInt() % 10 < 5) dept = random;
            }
            //begin++;
        }

        Map<String, List<Employee>> g = employees.stream()
            .parallel()
            .collect(Collectors
                    .groupingBy(Employee::getDepartment)
            );

        Map<String, List<Employee>> c = employees.stream()
            .parallel()
            .collect(Collectors
                    .groupingByConcurrent(Employee::getDepartment)
            );

        ArrayList<Employee> listOne = (ArrayList<Employee>) g.entrySet().iterator().next().getValue();
        ArrayList<Employee> listTwo = (ArrayList<Employee>) c.entrySet().iterator().next().getValue();
        listOne.forEach(x -> {
            if(!listTwo.contains(x)) System.out.println(x + " not present in the second map");
        });
    }
}
