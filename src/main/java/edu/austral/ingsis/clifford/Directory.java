package edu.austral.ingsis.clifford;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Directory implements FileSystemNode {
  String name;
  List<FileSystemNode> children;
  Directory parent; // para el cd..
  private final Instant createdAt = Instant.now();

  public Directory(String name, Directory parent) {
    this.name = name;
    this.parent = parent;
    this.children = new ArrayList<>();
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

  public Directory getParent() {
    return parent;
  }

  public void addChild(FileSystemNode child) {
    children.add(child);
  }

  // Directory emily = new Directory("emily", null);
  // emily.addChild(new File("elizabeth.txt", emily));

  public boolean hasChild(String name) {
    for (FileSystemNode child : children) {
      if (child.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public FileSystemNode getChildByName(String name) {
    for (FileSystemNode child : children) {
      if (child.getName().equals(name)) {
        return child;
      }
    }
    return null;
  }

  public List<FileSystemNode> listChildren(Order order) {
    if (order == Order.DEFAULT) return List.copyOf(children);
    // un comparador que ordena los FileSystemNode según su nombre alfabético
    Comparator<FileSystemNode> byName = Comparator.comparing(FileSystemNode::getName);
    if (order == Order.DESC) byName = byName.reversed();

    // Converti la lista a un stream, ordenala con byName, devolveme el resultado como nueva lista
    return children.stream().sorted(byName).toList();
  }

  public void removeChild(String name) {
    children.removeIf(child -> child.getName().equals(name));
  }
}
