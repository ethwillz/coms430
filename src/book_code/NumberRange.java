package book_code;

import java.util.concurrent.atomic.AtomicInteger;

public class NumberRange {
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i){
        if(i > upper.get())
            throw new IllegalArgumentException("can't set lower greater than upper");
        lower.set(i);
    }
}
