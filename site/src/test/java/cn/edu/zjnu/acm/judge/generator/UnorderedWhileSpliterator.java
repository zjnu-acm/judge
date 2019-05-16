package cn.edu.zjnu.acm.judge.generator;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

class UnorderedWhileSpliterator<T> extends Spliterators.AbstractSpliterator<T> implements Consumer<T> {

    private static final int CANCEL_CHECK_COUNT = 63;
    private final Predicate<? super T> p;
    private final Spliterator<T> s;
    private int count;
    private T t;
    private final AtomicBoolean cancel = new AtomicBoolean();
    private boolean takeOrDrop = true;

    UnorderedWhileSpliterator(Predicate<? super T> p, Spliterator<T> s) {
        super(s.estimateSize(), s.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED));
        this.p = p;
        this.s = s;
    }

    @Override
    @SuppressWarnings("NestedAssignment")
    public boolean tryAdvance(Consumer<? super T> action) {
        boolean test = true;
        if (takeOrDrop // If can take
                && (count != 0 || !cancel.get()) // and if not cancelled
                && s.tryAdvance(this) // and if advanced one element
                && (test = p.test(t))) { // and test on element passes
            action.accept(t); // then accept element
            return true;
        } else { // Taking is finished
            takeOrDrop = false;
            // Cancel all further traversal and splitting operations
            // only if test of element failed (short-circuited)
            if (!test) {
                cancel.set(true);
            }
            return false;
        }
    }

    @Override
    public Comparator<? super T> getComparator() {
        return s.getComparator();
    }

    @Override
    public void accept(T t) {
        count = (count + 1) & CANCEL_CHECK_COUNT;
        this.t = t;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

}
