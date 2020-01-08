package testools.predicate;

import java.util.function.Predicate;

import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Arrays.asList;

public final class PredicateUtils {

    private PredicateUtils() {
    }

    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T> Predicate<T> and(final Predicate<? super T>... rootPredicates) {
        return and(asList(rootPredicates));
    }

    public static <T> Predicate<T> and(final Iterable<Predicate<? super T>> predicates) {
        if (isEmpty(predicates)) {
            return t -> true;
        }
        Predicate<T> result = null;
        for (final Predicate<? super T> predicate : predicates) {
            result = result == null ? (Predicate<T>) predicate : result.and(predicate);
        }
        return result;
    }

    public static <T> Predicate<T> notEqual(final Object element) {
        return not(Predicate.isEqual(element));
    }

    public static <T> Predicate<T> alwaysTrue() {
        return x -> true;
    }

    public static <T> Predicate<T> or(final Predicate<T> left, final Predicate<T> right) {
        return left.or(right);
    }
}