package edu.austral.ingsis.clifford;

import java.time.Instant;
import java.util.List;

public final class File implements FileSystemNode {
  String name;
  Directory parent; //para el cd..
  private final Instant createdAt = Instant.now();

  public File(String name, Directory parent){
    this.name = name;
    this.parent = parent;
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

  public Directory getParent() {
    return parent;
  }
}

//los files se ordenan igual que los directories, porque en mi file system ambos estan mezclados
// adentro de la misma lista children y tienen el mismo “peso” a la hora de hacer ls