package testools.predicate;

import com.google.common.base.Charsets;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Throwables.propagate;
import static java.nio.file.Files.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FilenameUtils.separatorsToSystem;
import static org.apache.commons.lang.SystemUtils.LINE_SEPARATOR;
import static testools.predicate.CollectionPredicate.containsOnly;
import static testools.predicate.StringPredicate.equalsIgnoringWhitespaces;

public final class PathPredicate {
    private PathPredicate() {
    }

    public static Predicate<Path> filepath(final Predicate<String> predicate) {
        return new Predicate<Path>() {
            public boolean test(final Path element) {
                return predicate.test(separatorsToSystem(element.toAbsolutePath().toString()));
            }

            public String toString() {
                return "a file with path " + predicate;
            }
        };
    }

    public static Predicate<Path> aDirectory(final Predicate<Path>... children) {
        return file -> isDirectory(file) && containsOnly(children).test(listPaths(file));
    }

    public static Predicate<Path> aDirectory(final String name, final Predicate<Path>... children) {
        return file -> isDirectory(file) &&
                file.getFileName().toString().equals(name) &&
                containsOnly(children).test(listPaths(file));
    }

    public static Predicate<Path> aFile(final String name, final String... contents) {
        return aFile(name, containsAll(contents));
    }

    public static Predicate<Path> aFile(final String name, final Predicate<Path> content) {
        return path -> {
            if (isRegularFile(path)) {
                if (path.getFileName().toString().equals(name)) {
                    if (content.test(path)) {
                        return true;
                    }
                    System.out.println(readFileToString(path));
                }
            }
            return false;
        };
    }

    public static Predicate<Path> containsAll(final String... contents) {
        return file -> {
            final String fileToString = readFileToString(file); //.replaceAll("\\w{32}", "<ID>");
            for (final String content : contents) {
                final Pattern compile = Pattern.compile(content);
                final Matcher matcher = compile.matcher(fileToString);
                if (!matcher.find()) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Predicate<Path> exactContent(final String content) {
        return file -> {
            final String element = readFileToString(file);
            final boolean result = equalsIgnoringWhitespaces(element, content);
            if (!result) {
                System.out.println(element);
            }
            return result;
        };
    }

    public static Predicate<Path> regexpContent(final String content) {
        return file -> {
            final String element = readFileToString(file);
            final Pattern compile = Pattern.compile(content);
            final Matcher matcher = compile.matcher(element);
            if (!matcher.find()) {
                System.out.println(element);
                return false;
            }
            return true;
        };
    }

    public static Predicate<Path> containContent(final String content) {
        return file -> {
            final String element = readFileToString(file);
            final boolean result = StringUtils.contains(element, content.replace("\n", LINE_SEPARATOR));
            if (!result) {
                System.out.println(element);
            }
            return result;
        };
    }

    private static String readFileToString(final Path file) {
        try {
            return readAllLines(file, Charsets.UTF_8).stream().collect(joining(LINE_SEPARATOR));
        } catch (final IOException e) {
            throw propagate(e);
        }
    }

    public static Collection<Path> listPaths(final Path directory) {
        try (Stream<Path> s = list(directory)) {
            return s.collect(toList());
        } catch (final IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

}
