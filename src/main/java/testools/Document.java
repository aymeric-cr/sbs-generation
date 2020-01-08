package testools;

import com.google.common.base.Strings;

public class Document {

    private final StringBuilder builder = new StringBuilder(1000);
    private int tab;

    private Document() {

    }

    public static Document newDocument() {
        return new Document();
    }

    public Document line(final String line) {
        builder.append(Strings.repeat("  ", tab));
        builder.append(line);
        nl();
        return this;
    }

    public Document nl() {
        builder.append("\n");
        return this;
    }

    public Document tab() {
        tab++;
        return this;
    }

    public Document untab() {
        tab--;
        return this;
    }

    public String get() {
        return builder.toString();
    }
}
