package com.example.demo.fileutilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileWalker {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileWalker.class);

  public Set<String> walk(final Path root) {
    FileWalkVisitor visitor = new FileWalkVisitor();
    try {
      Files.walkFileTree(root, visitor);
    } catch (IOException e) {
      // This can't actually be thrown unless some weird network shit happens, since the visitor can't throw it..
      // but we don't want to have to throw a checked exception all the way up
      throw new IllegalArgumentException(e);
    }

    Set<String> filePaths = new HashSet<>();
    visitor.getFilePaths().forEach(path -> filePaths.add(path.toString()));
    LOGGER.debug("Walked tree from root [" + root.toString() + "] and found [" + filePaths.size() + "] files");
    return filePaths;
  }
}
