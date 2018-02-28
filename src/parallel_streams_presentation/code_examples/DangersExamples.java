package parallel_streams_presentation.code_examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class illustrates a potential danger of parallel streams. The serial and parallel streams both finished in about
 * the same amount of time. The difference is that while the long ooperation was being computed in the parallel stream,
 * no other Java app on the machine is able to use parallel streams."
 */
public class DangersExamples {

    public static void main(String[] args) throws Exception{
        String temp;
        DangersExamples dops = new DangersExamples();
        ArrayList<String> list = new ArrayList<>();
        File f = new File("/Users/ethwillz/Desktop/coms430/src/parallel_streams_presentation/code_examples/random_strings_10000.txt");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        while((temp = reader.readLine()) != null){
            list.add(temp);
        }

        long start = System.nanoTime();
        list.stream().map(x -> longOperation()).findAny();
        long deltaT = System.nanoTime() - start;
        System.out.println("In Serial: " + deltaT / 1000000000 + "." + deltaT % 1000000000 + " s");

        start = System.nanoTime();
        list.stream().parallel().map(x -> longOperation()).findAny();
        deltaT = System.nanoTime() - start;
        System.out.println("In parallel: "
                + (System.nanoTime() - start) / 1000000000 + "." + Long.toString(deltaT % 1000000000).substring(0, 2) + " s");
    }

    private static String longOperation(){
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
