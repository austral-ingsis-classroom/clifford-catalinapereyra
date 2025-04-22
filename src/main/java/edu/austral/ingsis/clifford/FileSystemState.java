package edu.austral.ingsis.clifford;

import java.util.List;
import java.util.stream.Collectors;

public class FileSystemState {
  private final Directory root;
  private Directory current;

  public FileSystemState() {
    this.root = new Directory("/", null);
    this.current = root;
  }

  public Directory getCurrent() {
    return current;
  }

  public Directory getRoot() {
    return root;
  }

  public void moveTo(Directory newDirectory) {
    this.current = newDirectory;
  }

  public void moveToRoot() {
    this.current = root;
  }

  public String mkdir(String name) {
    if (name.contains("/") || name.contains(" ")) return "Invalid directory name";
    if (current.hasChild(name)) return "'" + name + "' already exists";

    Directory newDirectory = new Directory(name, current);
    current.addChild(
        newDirectory); // Agrega el nuevo directorio a la lista de hijos del directorio actual
    return "'" + name + "' directory created";
  }

  public String touch(String name) {
    if (name.contains("/") || name.contains(" ")) return "Invalid file name";
    if (current.hasChild(name)) return "'" + name + "' already exists";

    File newFile = new File(name, current);
    current.addChild(newFile);
    return "'" + name + "' file created";
  }

  public String cd(String rawPath) {
    Path path =
        Path.parse(
            rawPath); // Este metood recibe un String raw, osea, la ruta escrita por el usuario (ej:
    // /users/cata/files) y la convierte en un Path
    Directory from =
        path.isAbsolute()
            ? root
            : current; // Si el path es absoluto (/algo/otro), empiezo desde root y sino, si es
    // relativo (docs/archivos), empiezo desde el directorio actual (current).

    // Recorrer cada parte de la ruta
    for (String part : path.segments()) {
      if (part.equals(".") || part.isBlank()) continue; // Ignorar . o partes vacias

      // Si es .., ir al padre
      if (part.equals("..")) {
        if (from.getParent() != null) {
          from = from.getParent();
        }
      } else {
        // Si no es . ni .., avanzar al hijo
        FileSystemNode next = from.getChildByName(part);
        if (next == null)
          return "'"
              + part
              + "' directory does not exist"; // Si no encuentra el hijo, devuelve error
        if (!(next instanceof Directory dir))
          return "'"
              + part
              + "' is not a directory"; // encuentra un hijo con ese nombre pero no es un
        // directorio, tmb da error
        from = dir; // si todo esta bien avanza al siguritne directorio
      }
    }

    current = from; // Si recorrio todo bien, actualiza current
    return "moved to directory '" + current.getName() + "'";
  }

  public String pwd() {
    Directory dir =
        current; // Guarda el directorio actual en una variable auxiliar llamada dir para no
    // modificar current
    StringBuilder path =
        new StringBuilder(); // Se usa para construir el path final de forma eficiente (en lugar de
    // ir concatenando strings con +).

    // Recorremos desde el actual hacia arriba
    while (dir != null && dir.getParent() != null) {
      path.insert(
          0,
          "/" + dir.getName()); // insert(0, ...) agrega al inicio del string (porque voy de abajo
      // hacia arriba. si estoy en /home/user/docs, se arma asi: /docs ->
      // /user/docs -> /home/user/docs
      dir = dir.getParent(); // sube un nivel.
    }

    return path.length() == 0
        ? "/"
        : path.toString(); // Si path quedó vacío (porque estás en el root), devuelve /, Si no,
    // devuelve el path completo
  }

  public String ls(String flag) {
    Order order;
    if (flag == null || flag.isBlank()) {
      order = Order.DEFAULT;
    } else if (flag.trim().equals("--ord=asc")) { // trim extra por si hay espacios
      order = Order.ASC;
    } else if (flag.trim().equals("--ord=desc")) {
      order = Order.DESC;
    } else {
      return "invalid flag";
    }

    List<FileSystemNode> items =
        current.listChildren(
            order); // devuelve una lista con los archivos y subdirectorios ordenados segun el Order
    if (items.isEmpty()) return "";

    return items
        .stream() // convierte la lista de items en un stream -> pasan los elementos de a uno, y
        // podés ir aplicando operaciones como transformar, filtrar, ordenar, etc.
        .map(
            FileSystemNode
                ::getName) // convierte la lista de objetos FileSystemNode en una lista de Strings
        // con los nombres
        .collect(
            Collectors.joining(
                " ")); // collect(...) convierte el stream en otra cosa (por ejemplo, una lista, un
    // string...) y Collectors.joining une todos los elementos usando un espacio
    // como separador.
  }

  public String rm(String name, boolean recursive) {
    FileSystemNode target =
        current.getChildByName(
            name); // Busca en los hijos del directorio actual (current) si hay alguno con ese
    // nombre

    if (target == null)
      return "'"
          + name
          + "' does not exist"; // Si no hay ningún hijo con ese nombre, devuelve error.

    if (target instanceof File) {
      current.removeChild(name);
      return "'" + name + "' removed";
    }

    if (target instanceof Directory) {
      if (!recursive) return "cannot remove '" + name + "', is a directory";
      current.removeChild(name); // borra todo el subárbol
      return "'" + name + "' removed";
    }

    return "unknown error";
  }
}
