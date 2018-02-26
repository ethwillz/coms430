package parallel_streams_presentation;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        System.out
                .println("using reduce: " + Arrays.asList("ohello2ooo", "whello1", "dhello3", "ahello4", "zhello5hhhh")
                        .parallelStream().reduce("", (c1, s1) -> {
                            //print("c in accumulator: " + c1);
                            print("s1 in accumulator: " + s1);
                            return c1 + process(s1);
                        }, (s2, s3) -> {
                            print("s2 in combiner: " + s2);
                            print("s3 in combiner: " + s3);
                            return s2 + s3;
                        }));

    }

    public static String process(String input) {
        if (input.length() > 6) {
            input = input.substring(1, 7);
        }
        System.out.println("process output: " + input);
        return input;
    }

    public static void print(String input) {
        System.out.println("in print: " + input);
    }
}
