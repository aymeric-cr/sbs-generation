package testools.predicate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.io.FilenameUtils.separatorsToSystem;
import static org.apache.commons.lang.CharEncoding.UTF_8;
import static org.apache.commons.lang.StringUtils.contains;
import static testools.FileTestUtils.*;
import static testools.predicate.CollectionPredicate.containsOnly;
import static testools.predicate.StringPredicate.equalsIgnoringWhitespaces;

public final class FilePredicate {
    private FilePredicate() {
    }

    public static Predicate<File> filepath(final Predicate<String> predicate) {
        return new Predicate<File>() {
            public boolean test(final File element) {
                return predicate.test(separatorsToSystem(element.getAbsolutePath()));
            }

            public String toString() {
                return "a file with path " + predicate;
            }
        };
    }

    public static Predicate<File> aDirectory(final Predicate<File>... children) {
        return file -> file.isDirectory() && containsOnly(children).test(listFiles(file));
    }

    public static Predicate<File> aDirectory(final String name, final Predicate<File>... children) {
        return file -> file.isDirectory() &&
                file.getName().equals(name) &&
                containsOnly(children).test(listFiles(file));
    }

    public static Predicate<File> aFile(final String name) {
        return aFile(name, containsAll());
    }

    public static Predicate<File> aFile(final String name, final String... contents) {
        return aFile(name, containsAll(contents));
    }

    public static Predicate<File> aFile(final String name, final Predicate<File>... contents) {
        return file -> {
            if (file.isFile()) {
                if (file.getName().equals(name)) {
                    for (final Predicate<File> content : contents) {
                        if (content.test(file)) {
                            continue;
                        }
                        System.err.println(readFileToString(file));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        };
    }

    public static Predicate<File> containsAllXML(final String content) {
        return file -> {
            final String fileContent = readFileToString(file);
            if (areSimilar(fileContent, content)) {
                return true;
            }
            System.err.println(fileContent + "\n\nis not similar to :\n" + content);
            return false;
        };
    }

    public static Predicate<File> containsXML(final String node, final String xpath) {
        return file -> {
            final String fileContent = readFileToString(file);
            if (subsetsSimilar(fileContent, node, xpath)) {
                return true;
            }
            System.err.println(node + "\n\nis not present in : \n");
            return false;
        };
    }

    public static Predicate<File> xpathsExist(final String... xpaths) {
        return file -> {
            final String fileContent = readFileToString(file);
            for (final String xpath : xpaths) {
                if (!isXpathResolved(fileContent, xpath)) {
                    System.err.println(xpath + "\n\ncannot be resolved in : \n");
                    return false;
                }
            }
            return true;
        };
    }

    public static Predicate<File> containsAll(final String... contents) {
        return file -> {
            final String element = readFileToString(file);
            for (final String content : contents) {
                final boolean result = contains(element, content.replace("\n", SystemUtils.LINE_SEPARATOR));
                if (!result) {
                    System.err.println(content + "\n\nis not present in :\n");
                    return false;
                }
            }
            return true;
        };
    }

    public static Predicate<File> content(final String content) {
        return file -> {
            final String element = readFileToString(file);
            final boolean result = equalsIgnoringWhitespaces(element, content);
            if (!result) {
                System.err.println(element);
            }
            return result;
        };
    }

    public static Predicate<File> containContent(final String content) {
        return file -> {
            final String element = readFileToString(file);
            final boolean result = contains(element, content.replace("\n", SystemUtils.LINE_SEPARATOR));
            if (!result) {
                System.out.println(element);
            }
            return result;
        };
    }

    public static String readFileToString(final File file) {
        try {
            return FileUtils.readFileToString(file, UTF_8);
        } catch (final IOException e) {
            throw propagate(e);
        }
    }

    public static Collection<File> listFiles(final File directory) {
        final File[] files = directory.listFiles();
        return files == null ? EMPTY_LIST : newArrayList(files);
    }

}
