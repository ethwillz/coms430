package parallel_streams_presentation.code_examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SpliteratorExamples{

    public static void main(String[] args) throws Exception {
        String temp;
        ArrayList<String> list = new ArrayList<>();
        File f = new File("/Users/ethwillz/Desktop/coms430/src/parallel_streams_presentation/code_examples/random_strings_10000.txt");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        while((temp = reader.readLine()) != null){
            list.add(temp);
        }

        long start = System.nanoTime();
        list.stream()
                .unordered()
                .parallel()
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .collect(Collectors.toCollection(LinkedList::new));
        long deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ generic unknown size Spliterator took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");
    }

}
