package testools.predicate;

import java.util.function.Predicate;

public final class ComparisonPredicate {

    private ComparisonPredicate() {
    }

    public static <T> Predicate<? super T> isSameInstance(final T fortyTwo) {
        return v -> v == fortyTwo;
    }
}