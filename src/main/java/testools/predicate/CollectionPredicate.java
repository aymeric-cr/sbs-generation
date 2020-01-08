package testools.predicate;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.function.Predicate.isEqual;

public final class CollectionPredicate {

    private CollectionPredicate() {
    }

    public static Predicate<Iterable<?>> hasSize(final Predicate<Integer> predicate) {
        return element -> predicate.test(size(element));
    }

    public static Predicate<Iterable<?>> hasSize(final int size) {
        return hasSize(isEqual(size));
    }

    public static <T> Predicate<Collection<? extends T>> hasItem(final T element) {
        return ts -> ts.contains(element);
    }

    public static <T> Predicate<Collection<T>> hasItem(final Predicate<T> criterion) {
        return collection -> collection.stream().anyMatch(criterion);
    }

    @SafeVarargs
    public static <T> Predicate<Collection<T>> containsAll(final T... expected) {
        return collection -> stream(expected).allMatch(collection::contains);
    }

    @SafeVarargs
    public static <T> Predicate<Collection<? extends T>> containsAll(final Predicate<? super T>... constraints) {
        return collection -> {
            for (final Predicate<? super T> predicate : constraints) {
                if (collection.stream().noneMatch(predicate)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static <T> Predicate<Iterable<T>> isEmpty() {
        return isSequence(emptyList());
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<T>> isSequence(final T... expected) {
        return isSequence(asList(expected));
    }

    public static <T> Predicate<Iterable<T>> isSequence(final Iterable<T> expected) {
        return element -> newArrayList(element).equals(newArrayList(expected));
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<? extends T>> isSequence(final Predicate<? super T>... constraints) {
        return collection -> {
            final List<Predicate<? super T>> predicates = asList(constraints);
            final Iterator<Predicate<? super T>> iterator = predicates.iterator();
            for (final T element : collection) {
                if (!iterator.hasNext() || !iterator.next().test(element)) {
                    return false;
                }
            }
            return !iterator.hasNext();
        };
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<T>> containsOnly(final T... expected) {
        return containsOnly(asList(expected));
    }

    public static <T> Predicate<Iterable<T>> containsOnly(final Iterable<T> expected) {
        return element -> HashMultiset.create(element).equals(HashMultiset.create(expected));
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<? extends T>> containsOnly(final Predicate<? super T>... constraints) {
        return new Predicate<Iterable<? extends T>>() {
            boolean isItemMatching(final T item,
                                   final Iterator<Predicate<? super T>> predicatesIterator) {
                while (predicatesIterator.hasNext()) {
                    if (predicatesIterator.next().test(item)) {
                        predicatesIterator.remove();
                        return true;
                    }
                }
                return false;
            }

            public boolean test(final Iterable<? extends T> collection) {
                final Collection<Predicate<? super T>> predicates = newArrayList(constraints);
                for (final T t : collection) {
                    if (!isItemMatching(t, predicates.iterator())) {
                        return false;
                    }
                }
                return predicates.isEmpty();
            }

            @Override
            public String toString() {
                return "<containsOnly>" + Joiner.on(' ').join(constraints) + "</containsOnly>";
            }
        };
    }

}
