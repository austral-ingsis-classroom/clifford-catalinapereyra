package edu.austral.ingsis.clifford;

public record FileSystemState(Directory root, Path currentPath) {

  public static FileSystemState init() {
    Directory root = Directory.root();
    return new FileSystemState(root, Path.parse("/"));
  }
}
