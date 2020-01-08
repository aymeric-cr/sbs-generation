package testools.rules;

import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.list;
import static org.junit.Assert.fail;
import static sun.security.krb5.internal.ktab.KeyTab.normalize;

public final class FileSystemPlugin extends TemporaryFolder {

    private static void deletePath(final Path path) {
        if (isDirectory(path)) {
            try (Stream<Path> s = list(path)) {
                s.forEach(FileSystemPlugin::deletePath);
            } catch (final IOException e) {
                fail();
            }
        } else {
            try {
                Files.delete(path);
            } catch (final IOException e) {
                fail();
            }
        }
    }

    public Path getRootPath() {
        return getRoot().toPath();
    }

    public File createFileReference(final String... elements) {
        File rawPath = getRoot();
        for (final String element : elements) {
            rawPath = new File(rawPath, element);
        }
        final String normalizedPath = normalize(rawPath.getAbsolutePath());
        if (normalizedPath == null) {
            return rawPath.getAbsoluteFile();
        }
        return new File(normalizedPath);
    }

    public void dispose() {
        deletePath(getRootPath());
    }
}
