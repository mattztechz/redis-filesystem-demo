package com.example.demo.fileutilities;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class FileWalkVisitor implements FileVisitor<Path> {

  private final Set<Path> filePaths = new HashSet<>();

  @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    return FileVisitResult.CONTINUE;
  }

  @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    filePaths.add(file);
    return FileVisitResult.CONTINUE;
  }

  @Override public FileVisitResult visitFileFailed(Path file, IOException exc) {
    return FileVisitResult.CONTINUE;
  }

  @Override public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    return FileVisitResult.CONTINUE;
  }

  public Set<Path> getFilePaths() {
    return filePaths;
  }
}
