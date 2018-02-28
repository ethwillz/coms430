package parallel_streams_presentation.code_examples;

import java.util.ArrayList;

public class runner {
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        char begin = 'a';
        char name = 'a';
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 100000000; j++){
                employees.add(new Employee(begin, name));
                name++;
            }
            begin++;
        }

        long start = System.nanoTime();
        (new CollectorsExample()).run(employees);
        long deltaT = System.nanoTime() - start;
        System.out.println(deltaT / 1000000000 + "." + Long.toString(deltaT % 1000000000).substring(0, 2) + " s\n");
    }
}
