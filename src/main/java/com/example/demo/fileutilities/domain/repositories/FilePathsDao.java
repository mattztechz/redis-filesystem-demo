package com.example.demo.fileutilities.domain.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.nio.file.Path;
import java.util.Set;

@Repository
public class FilePathsDao {

  private final JedisPool jedisPool;
  private final String jedisHost;

  public FilePathsDao(final JedisPool jedisPool, @Value("${jedis.host:localhost}") final String jedisHost) {
    this.jedisPool = jedisPool;
    this.jedisHost = jedisHost;
  }

  /**
   * Bulk inserts filepath objects - note that this is a very low level pipelined implementation for maximum speed
   * because of the potentially large number of filePaths. If using smaller numbers of items, there are much
   * easier ways to do this using spring-data-redis
   * (And for normal data access redis is overkill, a SQL database is even easier)
   */
  public int insertFilePaths(final Path root, final Set<String> filePaths) {
    try (Jedis jedis = jedisPool.getResource()) {
      Pipeline p = jedis.pipelined();
      filePaths.forEach(path -> p.set(path, root.toString()));
      p.sync();
    }
    return filePaths.size();
  }

  public Set<String> getAllPaths() {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.isConnected();
      return jedis.keys("*");
    }
  }

  /**
   * Performs a wildcard search, i.e key*, for partial matches
   */
  public Set<String> findByWildcardKey(final String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.keys(key + "*");
    }
  }

  public String findByAbsoluteKey(final String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.get(key);
    }
  }
}
