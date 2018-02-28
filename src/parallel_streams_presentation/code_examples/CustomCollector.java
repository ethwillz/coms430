package parallel_streams_presentation.code_examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomCollector<E> implements Collector{
    @Override
    public Supplier supplier() {
        return  ArrayList::new;
    }

    @Override
    public BiConsumer<ArrayList<E>, E> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<ArrayList<E>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<ArrayList<E>, E> finisher() {
        return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return null;
    }
}
