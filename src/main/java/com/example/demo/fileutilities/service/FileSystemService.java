package com.example.demo.fileutilities.service;

import com.example.demo.fileutilities.FileWalker;
import com.example.demo.fileutilities.domain.repositories.FilePathsDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

@Service
public class FileSystemService {

  private final FileWalker walker;
  private final FilePathsDao filePathsDao;

  @Autowired
  public FileSystemService(final FileWalker walker, final FilePathsDao filePathsDao) {
    this.walker = walker;
    this.filePathsDao = filePathsDao;
  }

  public int refreshFilePaths(final Path root) {
    return filePathsDao.insertFilePaths(root, walker.walk(root));
  }

  public Set<String> getAllFilePaths() {
    return filePathsDao.getAllPaths();
  }

  public File getPathDetails(final String path) {

    String filePath = filePathsDao.findByAbsoluteKey(path);
    if (StringUtils.trimToNull(filePath) == null) {
      throw new IllegalArgumentException("File path not found in cache: [" + path + "]");
    }

    File file = new File(filePath);
    if (!file.exists() || !file.canRead()) {
      throw new IllegalStateException("File does not exist or cannot be read at [" + path + "]");
    }

    return file;
  }
}
