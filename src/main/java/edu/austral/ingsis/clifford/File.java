package edu.austral.ingsis.clifford;

import java.time.Instant;

public final class File implements FileSystemNode {
  private final String name;
  private final Instant createdAt;

  public File(String name, Instant createdAt) {
    this.name = name;
    this.createdAt = createdAt;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "file";
  }

  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }
}
