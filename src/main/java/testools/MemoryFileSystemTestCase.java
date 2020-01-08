package testools;

import org.junit.After;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import static com.google.common.jimfs.Jimfs.newFileSystem;

public abstract class MemoryFileSystemTestCase {

    protected final FileSystem fileSystem = newFileSystem();
    protected Path root = fileSystem.getRootDirectories().iterator().next().resolve("work");

    @After
    public void tearDown() throws Exception {
        fileSystem.close();
    }
}
