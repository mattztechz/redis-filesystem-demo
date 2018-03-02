package com.example.demo.fileutilities.service;

import com.example.demo.fileutilities.FileWalker;
import com.example.demo.fileutilities.domain.repositories.FilePathsDao;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FileSystemServiceTest {

  @InjectMocks
  private FileSystemService service;

  @Mock
  private FileWalker walker;

  @Mock
  private FilePathsDao filePathsDao;

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void refreshFilePaths_UsesPassedInRoot() {
    Path root = Paths.get("root");

    HashSet<String> walkerResult = Sets.newHashSet("root", "child");
    when(walker.walk(root)).thenReturn(walkerResult);

    service.refreshFilePaths(root);
    verify(filePathsDao).insertFilePaths(root, walkerResult);
  }

  @Test
  public void getAllFilePaths_ReturnsResults() {
    when(filePathsDao.getAllPaths()).thenReturn(Sets.newHashSet("root"));
    Set<String> allFilePaths = service.getAllFilePaths();
    assertNotNull(allFilePaths);
    assertFalse(allFilePaths.isEmpty());
    assertEquals(1, allFilePaths.size());
    assertEquals("root", allFilePaths.iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void getPathDetails_FileFromPathMissing_ThrowsError() {
    when(filePathsDao.findByAbsoluteKey("key")).thenReturn("fake");
    service.getPathDetails("key");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPathDetails_PathMissingFromCache_ThrowsError() {
    when(filePathsDao.findByAbsoluteKey("key")).thenReturn("fake");
    service.getPathDetails("notkey");
  }

  @Test
  public void getPathDetails_FileExists_ReturnsFile() throws IOException {
    File temp = temporaryFolder.newFile("test.txt");
    FileUtils.writeStringToFile(temp, "This is a test", Charset.defaultCharset());
    when(filePathsDao.findByAbsoluteKey("key")).thenReturn(temp.getAbsolutePath());

    File pathDetails = service.getPathDetails("key");
    assertEquals(temp.getAbsolutePath(), pathDetails.getAbsolutePath());
  }

}
