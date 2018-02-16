package parallel_streams_presentation;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;


/**
 * This is a mock data structure which is a basic array-backed list implementation. The purpose is to illustrate some
 * of the different ways to implement Spliterators, outlined in the spliterator method.
 * @param <E> Type of data to hold in list
 *
 * https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html
 */
public class SpliteratorImplementationExamples<E> implements List<E> {
    private final int DEFAULT_SIZE = 10;
    private int cursorIndex;
    private E arr[];

    public static void main(String[] args){
        //Look into ArrayList's Spliterator
        ArrayList<String> thisHasNoFunctionalPurpose;

        SpliteratorImplementationExamples<String> abl = new SpliteratorImplementationExamples<>();
        abl.add("Hello");
        abl.add("Goodbye");

        long start = System.nanoTime();
        StreamSupport.stream(abl.spliterator(), false).anyMatch(e -> e.equals("Hello"));
        long deltaT = System.nanoTime() - start;
        System.out.println("Serial stream w/ null trySplit() took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(abl.listIterator(), 0), false).anyMatch(e -> e.equals("Hello"));
        deltaT = System.nanoTime() - start;
        System.out.println("Serial stream w/ spliteratorUnknownSize took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(abl.listIterator(), 0), true).anyMatch(e -> e.equals("Hello"));
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ spliteratorUnknownSize took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        StreamSupport.stream(Spliterators.spliterator(abl.listIterator(), abl.size(), 0), false).anyMatch(e -> e.equals("Hello"));
        deltaT = System.nanoTime() - start;
        System.out.println("Serial stream w/ known-size spliterator took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

        start = System.nanoTime();
        StreamSupport.stream(Spliterators.spliterator(abl.listIterator(), abl.size(), 0), true).anyMatch(e -> e.equals("Hello"));
        deltaT = System.nanoTime() - start;
        System.out.println("Parallel stream w/ known-size spliterator took " + deltaT / 1000000 + "." + Long.toString(deltaT % 1000000).substring(0, 2) + " ms\n");

    }

    public SpliteratorImplementationExamples(){
        arr = (E[]) new Object[DEFAULT_SIZE];
        cursorIndex = 0;
    }

    @Override
    public int size() { return cursorIndex; }

    @Override
    public boolean isEmpty() { return cursorIndex == 0; }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < cursorIndex; i++)
            if(o.equals(arr[cursorIndex])) return true;
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return Arrays.stream(arr).limit(cursorIndex).toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {  return null; }

    public boolean add(E data){
        if(cursorIndex + 1 == arr.length) {
            E nextArr[] = (E[]) new Object[arr.length * 2];
            for (int i = 0; i < arr.length; i++) { nextArr[i] = arr[i]; }
            arr = nextArr;
        }
        arr[cursorIndex++] = data;
        return true;
    }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public void clear() { }

    @Override
    public boolean equals(Object o) { return false; }

    @Override
    public int hashCode() { return 0; }

    @Override
    public E get(int index) {
        if(index > arr.length) throw new ArrayIndexOutOfBoundsException();
        return arr[index];
    }

    @Override
    public E set(int index, E element) { return null; }

    @Override
    public void add(int index, E element) {  }

    @Override
    public E remove(int index) { return null; }

    @Override
    public int indexOf(Object o) { return 0; }

    @Override
    public int lastIndexOf(Object o) { return 0; }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                if (currentIndex == arr.length || arr[currentIndex] == null) return false;
                return true;
            }

            @Override
            public E next() {
                return arr[currentIndex];
            }

            @Override
            public boolean hasPrevious() { return false; }

            @Override
            public E previous() { return null; }

            @Override
            public int nextIndex() { return 0; }

            @Override
            public int previousIndex() {  return 0; }

            @Override
            public void remove() { }

            @Override
            public void set(E e) { }

            @Override
            public void add(E e) { }
        };
    }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }

    public E remove(){
        if(cursorIndex == 0) throw new ArrayIndexOutOfBoundsException();
        return arr[cursorIndex--];
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArrayBackedListSpliterator<>(this);
    }

    final class ArrayBackedListSpliterator<E> implements Spliterator<E>{

        private int currentIndex = 0;

        private SpliteratorImplementationExamples<E> arr;

        public ArrayBackedListSpliterator(SpliteratorImplementationExamples<E> arr){
            this.arr = arr;
        }

        @Override
        public boolean tryAdvance(Consumer action) {
            E next = arr.get(currentIndex);
            currentIndex++;
            action.accept(next);
            return true;
        }

        @Override
        public Spliterator trySplit() {
            return null;
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
