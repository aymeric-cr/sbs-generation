package testools.predicate;


import org.apache.commons.lang.StringUtils;

import java.util.function.Predicate;

import static org.apache.commons.lang.StringUtils.deleteWhitespace;


public final class StringPredicate {

    private StringPredicate() {
    }

    public static Predicate<String> equalsIgnoringWhitespace(final String reference) {
        return element -> equalsIgnoringWhitespaces(element, reference);
    }

    public static boolean equalsIgnoringWhitespaces(final String element, final String reference) {
        return StringUtils.equals(deleteWhitespace(element), deleteWhitespace(reference));
    }

    public static Predicate<String> endsWith(final String suffix) {
        return element -> element != null && element.endsWith(suffix);
    }
}