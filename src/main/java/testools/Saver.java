package testools;


import java.util.function.Predicate;

public final class Saver<T> implements Predicate<T> {

    private T saved;

    private Saver() {
    }

    public static <T> Saver<T> create() {
        return new Saver<>();
    }

    public static <T> Predicate<? super T> savedIn(final Saver<T> loadedScenario) {
        return t -> {
            loadedScenario.save(t);
            return true;
        };
    }

    public void save(final T input) {
        saved = input;
    }

    public T get() {
        return saved;
    }

    @Override
    public boolean test(final T o) {
        return o == saved;
    }
}
