package testools;

import java.util.function.Predicate;

import static java.util.function.Predicate.isEqual;
import static testools.RendererManager.instance;

public final class PredicateAssert {

    private PredicateAssert() {
    }

    public static void registerRenderer(final Renderer renderer) {
        RendererManager.registerRenderer(renderer);
    }

    public static void assertThat(final boolean actual) {
        assertThat(actual, isEqual(true));
    }

    public static <T, U extends T> void assertEqual(final T actual, final U expected) {
        assertThat(actual, isEqual(expected));
    }

    public static <T> void assertThat(final T actual, final Predicate<? super T> predicate) {
        if (!predicate.test(actual)) {
            throw new AssertionError(renderAssertion(actual, predicate));
        }
    }

    private static <T> String renderAssertion(final T actual, final Predicate<? super T> predicate) {
        return "\nExpected: " + predicate + "\n    got : " + instance().render(actual);
    }
}
