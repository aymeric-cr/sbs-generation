package testools.predicate;

import com.google.common.base.Predicate;

import java.util.Map;
import java.util.Map.Entry;

final class IsMapContaining<K, V> implements Predicate<Map<K, V>> {
    private final Predicate<K> keyMatcher;
    private final Predicate<V> valueMatcher;

    IsMapContaining(final Predicate<K> keyMatcher, final Predicate<V> valueMatcher) {
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    public boolean apply(final Map<K, V> map) {
        for (final Entry<K, V> entry : map.entrySet()) {
            if (keyMatcher.apply(entry.getKey()) && valueMatcher.apply(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "IsMapContaining(" + keyMatcher + ": " + valueMatcher + ')';
    }
}