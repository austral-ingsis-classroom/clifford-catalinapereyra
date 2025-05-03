package edu.austral.ingsis.clifford;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class Directory implements FileSystemNode {
  private final String name;
  private final List<FileSystemNode> children;
  private final Instant createdAt;

  public Directory(String name) {
    this(name, List.of(), Instant.now());
  }

  private Directory(String name, List<FileSystemNode> children, Instant createdAt) {
    this.name = name;
    this.children = children;
    this.createdAt = createdAt;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "directory";
  }

  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }

  public static Directory root() {
    return new Directory("/", List.of(), Instant.now());
  }

  public Directory addChild(FileSystemNode child) {
    List<FileSystemNode> newChildren = new ArrayList<>(children);
    if (child instanceof Directory dir) {
      newChildren.add(
          new Directory(dir.getName(), dir.listChildren(Order.DEFAULT), dir.getCreatedAt()));
    } else if (child instanceof File file) {
      newChildren.add(new File(file.getName(), file.getCreatedAt()));
    } else {
      newChildren.add(child);
    }
    return new Directory(name, List.copyOf(newChildren), createdAt);
  }

  public boolean hasChild(String name) {
    for (FileSystemNode child : children) {
      if (child.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public Optional<FileSystemNode> getChildByName(String name) {
    for (FileSystemNode child : children) {
      if (child.getName().equals(name)) {
        return Optional.of(child);
      }
    }
    return Optional.empty();
  }

  public List<FileSystemNode> listChildren(Order order) {
    if (order == Order.DEFAULT) return List.copyOf(children);
    Comparator<FileSystemNode> byName = Comparator.comparing(FileSystemNode::getName);
    if (order == Order.DESC) byName = byName.reversed();
    return children.stream().sorted(byName).toList();
  }

  public Directory removeChild(String name) {
    List<FileSystemNode> newChildren =
        children.stream().filter(child -> !child.getName().equals(name)).toList();
    return new Directory(this.name, newChildren, this.createdAt);
  }

  public Directory replace(Directory target, Directory replacement) {
    if (this == target) return replacement;
    List<FileSystemNode> newChildren =
        children.stream()
            .map(
                child -> {
                  if (child instanceof Directory dir) {
                    return dir.replace(target, replacement);
                  }
                  return child;
                })
            .toList();
    return new Directory(name, newChildren, createdAt);
  }

  public static Optional<Directory> resolvePath(Directory root, Path path) {
    Directory current = root;
    for (String segment : path.segments()) {
      if (segment.equals(".") || segment.isBlank()) continue;
      Optional<FileSystemNode> maybeChild = current.getChildByName(segment);
      if (maybeChild.isEmpty() || !(maybeChild.get() instanceof Directory dir)) {
        return Optional.empty();
      }
      current = (Directory) maybeChild.get();
    }
    return Optional.of(current);
  }
}
