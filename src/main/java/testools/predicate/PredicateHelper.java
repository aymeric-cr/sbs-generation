package testools.predicate;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.function.Predicate;

public final class PredicateHelper {
    private PredicateHelper() {
    }

    public static <T> Matcher<T> toMatcher(final Predicate<? super T> trace) {
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(final Object o) {
                return trace.test((T) o);
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }
}
