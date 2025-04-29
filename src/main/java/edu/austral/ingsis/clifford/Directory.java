package edu.austral.ingsis.clifford;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class Directory implements FileSystemNode {
  String name;
  List<FileSystemNode> children;
  private final Optional<Directory> parent; // para el cd..
  private Instant createdAt = Instant.now();

  public Directory(String name) {
    this(name, Optional.empty(), List.of(), Instant.now());
  }

  // Constructor privado para crear un nuevo directorio con hijos
  private Directory(
      String name, Optional<Directory> parent, List<FileSystemNode> children, Instant createdAt) {
    this.name = name;
    this.parent = parent;
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

  public Optional<Directory> getParent() {
    return parent;
  }

  public static Directory root() {
    return new Directory("/", Optional.empty(), List.of(), Instant.now());
  }

  //  public Directory addChild(FileSystemNode child) {
  //    List<FileSystemNode> newChildren = new ArrayList<>(children);
  //
  //    if (child instanceof Directory dir) {
  //      Directory newDir = new Directory(dir.getName(), Optional.of(this),
  // dir.listChildren(Order.DEFAULT), dir.getCreatedAt());
  //      newChildren.add(newDir);
  //    } else if (child instanceof File file) {
  //      File newFile = new File(file.getName(), this, file.getCreatedAt());
  //      newChildren.add(newFile);
  //    } else {
  //      newChildren.add(child);
  //    }
  //
  //    return new Directory(name, parent, List.copyOf(newChildren), createdAt);
  //  }
  public Directory addChild(FileSystemNode child) {
    List<FileSystemNode> newChildren = new ArrayList<>(children);
    if (child instanceof Directory dir) {
      newChildren.add(
          new Directory(
              dir.getName(),
              Optional.of(this),
              dir.listChildren(Order.DEFAULT),
              dir.getCreatedAt()));
    } else if (child instanceof File file) {
      newChildren.add(new File(file.getName(), this, file.getCreatedAt()));
    } else {
      newChildren.add(child);
    }
    return new Directory(name, parent, List.copyOf(newChildren), createdAt);
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

  public Optional<FileSystemNode> getChildByName(String name) {
    for (FileSystemNode child : children) {
      if (child.getName().equals(name)) {
        return Optional.of(child); // lo encontró
      }
    }
    return Optional.empty(); // no lo encontró
  }

  public List<FileSystemNode> listChildren(Order order) {
    if (order == Order.DEFAULT) return List.copyOf(children);
    // un comparador que ordena los FileSystemNode según su nombre alfabético
    Comparator<FileSystemNode> byName = Comparator.comparing(FileSystemNode::getName);
    if (order == Order.DESC) byName = byName.reversed();

    // Converti la lista a un stream, ordenala con byName, devolveme el resultado como nueva lista
    return children.stream().sorted(byName).toList();
  }

  public Directory removeChild(String name) {
    List<FileSystemNode> newChildren =
        children.stream().filter(child -> !child.getName().equals(name)).toList();

    return new Directory(this.name, this.parent, newChildren, this.createdAt);
  }

  // si sos el target devolves el replacement, sino buscas recursivamente en los hijos y reconstruis
  // un directoy nuevo con hijos actualizados
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

    return new Directory(name, parent, newChildren, createdAt);
  }

  public static Optional<Directory> resolvePath(Directory start, Path path) {
    Directory current = start;

    for (String segment : path.segments()) {
      if (segment.equals(".") || segment.isBlank()) continue; // queda en el mismo lugar
      if (segment.equals("..")) {
        current = current.getParent().orElse(current); // sube un nivel si existe
      } else {
        Optional<FileSystemNode> maybeChild = current.getChildByName(segment);
        if (maybeChild.isEmpty() || !(maybeChild.get() instanceof Directory dir)) {
          return Optional.empty(); // si no existe o no es un directorio, falla
        }
        current = (Directory) maybeChild.get(); // baja a ese hijo
      }
    }
    return Optional.of(current); // cuando termina de recorrer, devuelve el directorio encontrado
  }
}
