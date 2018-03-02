package com.example.demo;

import com.example.demo.fileutilities.service.FileSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import java.nio.file.Paths;
import java.util.Set;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
  private final FileSystemService fileSystemService;


  public DemoApplication(final FileSystemService fileSystemService) {
    this.fileSystemService = fileSystemService;
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args).close();
  }

  @Override
  public void run(String... args) {
    StopWatch timer = new StopWatch();
    timer.start();

    int total = fileSystemService.refreshFilePaths(Paths.get("C:/"));
    //    Set<String> allFilePaths = fileSystemService.getAllFilePaths();

//    String firstPath = allFilePaths.iterator().next();
//    FilePaths first = fileSystemService.getPathDetails(firstPath);

    timer.stop();
    log.info("Saved [" + total + "] records  in [" + timer.getTotalTimeSeconds() + "] seconds");
    log.info("Done");
  }
}
