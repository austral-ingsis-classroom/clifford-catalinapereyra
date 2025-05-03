package edu.austral.ingsis.clifford;

public final class FileSystemState {
  private final Directory root;
  private final Path currentPath;

  public FileSystemState(Directory root, Path currentPath) {
    this.root = root;
    this.currentPath = currentPath;
  }

  public static FileSystemState init() {
    return new FileSystemState(Directory.root(), Path.root());
  }

  public Directory getRoot() {
    return root;
  }

  public Path getCurrentPath() {
    return currentPath;
  }

  public Directory getCurrentDirectory() {
    return Directory.resolvePath(root, currentPath).orElseThrow(); // siempre partimos de root
  }

  public FileSystemState moveTo(Path newPath) {
    return new FileSystemState(root, newPath);
  }

  public FileSystemState update(Directory newRoot) {
    return new FileSystemState(newRoot, currentPath);
  }
}
