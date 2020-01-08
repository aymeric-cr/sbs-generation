package testools.predicate;

import java.util.function.Predicate;

import static java.lang.Math.abs;

public final class NumericPredicate {
    private NumericPredicate() {
    }

    public static Predicate<Double> closeTo(final double reference, final double error) {
        return new Predicate<Double>() {
            public boolean test(final Double value) {
                return abs(reference - value) <= error;
            }

            public String toString() {
                return reference + " +/- " + error;
            }
        };
    }
}
