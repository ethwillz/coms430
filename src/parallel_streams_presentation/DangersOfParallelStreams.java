package parallel_streams_presentation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class illustrates a potential danger of parallel streams. The serial and parallel streams both finished in about
 * the same amount of time. The difference is that while the long ooperation was being computed in the parallel stream,
 * no other Java app on the machine is able to use parallel streams."
 */
public class DangersOfParallelStreams {

    public static void main(String[] args){
        DangersOfParallelStreams dops = new DangersOfParallelStreams();
        List<String> engines = Arrays.asList("https://www.google.com/?q=", "https://duckduckgo.com/?q=", "https://www.bing.com/search?q=");

        long start = System.nanoTime();
        dops.queryWithLongNetworkOperationSerial(engines, "coms430");
        long deltaT = System.nanoTime() - start;
        System.out.println("In Serial: " + deltaT / 1000000 + "." + deltaT % 1000000 + "ms");

        start = System.nanoTime();
        dops.queryWithLongNetworkOperationParallel(engines, "coms430");
        deltaT = System.nanoTime() - start;
        System.out.println("In parallel: " + (System.nanoTime() - start) / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + "ms");
    }

    public String queryWithLongNetworkOperationParallel(List<String> engines, String query){
        Optional<String> result = engines.stream().map((base) -> longOperation(base + query)).findAny();
        return result.orElse(null);
    }

    public String queryWithLongNetworkOperationSerial(List<String> engines, String query){
        Optional<String> result = engines.stream().map((base) -> longOperation(base + query)).findAny();
        return result.orElse(null);
    }

    private String longOperation(String url){
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
