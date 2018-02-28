package parallel_streams_presentation.code_examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Spliterators;
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






        list.stream()
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count();

        list.stream()
                .parallel()
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count();

        StreamSupport.stream(Spliterators.spliteratorUnknownSize(list.listIterator(), 0), true)
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count();

        StreamSupport.stream(Spliterators.spliterator(list.listIterator(), list.size(), 0), true)
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count();

        list.stream()
                .unordered()
                .parallel()
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count();




        long start = System.nanoTime();
        long officialCount = ;
        long deltaT = System.nanoTime() - start;
        System.out.println("Serial stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        if( != officialCount)
            throw new Exception("Results from streams are different");
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        if(
            throw new Exception("Results from streams are different");
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ spliteratorUnknownSize took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        if( != officialCount)
            throw new Exception("Results from streams are different");
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ known-size spliterator took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        if(list.stream()
                .unordered()
                .parallel()
                .filter(x -> x.contains("a"))
                .map(x -> x = x.substring(0, 10))
                .count() != officialCount)
            throw new Exception("Results from streams are different");
        deltaT = System.nanoTime() - start;
        System.out.println("Unordered parallel stream took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");
    }

}
