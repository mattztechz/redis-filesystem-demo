package com.example.demo.fileutilities;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileWalkerTest {

  private FileWalker walker = new FileWalker();

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void walk_RootNotFound_ReturnsEmptyList() {
    assertTrue(walker.walk(Paths.get("fake")).isEmpty());
  }

  @Test
  public void walk_DirectoryFoundOnly_ReturnsEmptyList() throws IOException {
    temporaryFolder.newFolder("theFolder", "diffFolder");
    Set<String> paths = walker.walk(temporaryFolder.getRoot().getAbsoluteFile().toPath());
    assertTrue(paths.isEmpty());
  }

  @Test
  public void walk_OneFileFound_ReturnsFileOnly() throws IOException {
    File file = temporaryFolder.newFile("test.txt");
    Set<String> paths = walker.walk(temporaryFolder.getRoot().getAbsoluteFile().toPath());
    assertFalse(paths.isEmpty());
    assertEquals(1, paths.size());
    assertEquals(file.getAbsolutePath(), paths.iterator().next());
  }
}
