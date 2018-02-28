package parallel_streams_presentation.code_examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;

public class tetx {

    public static void main(String[] args){
        ArrayList<Integer> testList = new ArrayList<>();
        testList.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Instantiating Spliterator...");
        SimpleSpliterator og = new SimpleSpliterator<>(testList, 0, testList.size());
        System.out.println("Splitting Spliterator");
        new Thread(() -> {
            SimpleSpliterator second = og.trySplit();
            System.out.println("\nFirst thread adding 11 & 13 to source Collection");
            testList.add(5, 11);
            try { Thread.sleep(500); }
            catch (InterruptedException e) { e.printStackTrace(); }
            testList.add(7, 13);
            System.out.println("2-OG: " + og.list.toString());
            System.out.println("2-Second: " + second.list.toString());
        }).start();
        new Thread(() -> {
            og.index = og.index + 3;
            SimpleSpliterator third = og.trySplit();
            System.out.println("\nOriginal Spliterator list: " + og.list.toString());
            System.out.println("Third Spliterator list: " + third.list.toString());
            System.out.println("Second thread adding 12 to source Collection");
            testList.add(5, 12);
            System.out.println("Original Spliterator list: " + og.list.toString());
            System.out.println("Third Spliterator list: " + third.list.toString());
        }).start();
        System.out.println("\nMain thread adding 5 to collection");
        testList.add(5);
    }

    public static class SimpleSpliterator<E> implements Spliterator{
        private final ArrayList<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index

        public SimpleSpliterator(ArrayList<E> list, int index, int fence) {
            this.list = list;
            this.index = index;
            this.fence = fence;
        }

        @Override
        public boolean tryAdvance(Consumer action) {
            index++;
            return true;
        }

        @Override
        public SimpleSpliterator trySplit() {
            int mid = (index + fence) >>> 1;
            return (index >= mid) ? null : new SimpleSpliterator<E>(list, index, index = mid);
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }
}
