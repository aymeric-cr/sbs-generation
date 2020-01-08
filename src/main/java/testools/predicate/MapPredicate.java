package testools.predicate;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import static java.util.function.Predicate.isEqual;

public final class MapPredicate {
    private MapPredicate() {
    }

    public static Predicate<Map<?, ?>> isEmpty() {
        return new Predicate<Map<?, ?>>() {
            public boolean test(final Map<?, ?> map) {
                return map.isEmpty();
            }

            public String toString() {
                return "an empty map";
            }
        };
    }

    public static <K, V> Predicate<Entry<K, V>> entry(final K key, final V value) {
        return entry(isEqual(key), isEqual(value));
    }

    public static <K, V> Predicate<Entry<K, V>> entry(final K key, final Predicate<? super V> value) {
        return entry(isEqual(key), value);
    }

    public static <K, V> Predicate<Entry<K, V>> entry(final Predicate<? super K> key,
                                                      final Predicate<? super V> value) {
        return new Predicate<Entry<K, V>>() {
            public boolean test(final Entry<K, V> element) {
                return key.test(element.getKey()) && value.test(element.getValue());
            }

            public String toString() {
                return "entry key=" + key + " value=" + value;
            }
        };
    }
}
