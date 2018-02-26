package parallel_streams_presentation.code_examples;

import java.util.Arrays;
import java.util.List;

public class BasicParallelStreams {

    public static void main(String[] args) {
        List<Person> students = Arrays.asList(new Person("Snoop", "Dogg", true),
                new Person("Elon", "Musk", true),
                new Person("Donald", "Trump", false),
                new Person("Donald", "Glover", true));

        long start = System.nanoTime();

    //   stream            intermediate                terminal
    //    source            operations                  operation
    //  |-------|---------------------------------|----------------------------------------------------------------------------------------|
        students.stream().filter(Person::isGenius).forEach(person -> System.out.println(person.getFirstName() + " " + person.getLastName()));
        long deltaT = System.nanoTime() - start;
        System.out.println("Serial stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        students.parallelStream().filter(Person::isGenius).forEach(person -> System.out.println(person.getFirstName() + " " + person.getLastName()));
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms");
    }
}

class Person {
    private String firstName;
    private String lastName;
    private boolean isGenius;

    public Person(String firstName, String lastName, boolean isGenius) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isGenius = isGenius;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public boolean isGenius(){
        return isGenius;
    }
}