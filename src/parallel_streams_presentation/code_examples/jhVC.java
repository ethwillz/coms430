package parallel_streams_presentation.code_examples;

import java.util.ArrayList;
import java.util.Arrays;

public class jhVC {



    public static void main(String[] args){
        for(int i = 0; i < 10; i++) {
            ArrayList<Integer> storage = new ArrayList<>(), list = new ArrayList<>();
            list.addAll(Arrays.asList(1, 2, 3, 4, 5));
            list.parallelStream().map(storage::add).toArray();
            System.out.println(storage.toString());
        }
    }



}
