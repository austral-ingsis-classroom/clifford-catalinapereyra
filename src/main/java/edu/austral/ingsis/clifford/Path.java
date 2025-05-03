package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Path(List<String> segments, boolean isAbsolute) {

  // Este metood recibe un String raw, osea, la ruta escrita por el usuario (ej: /users/cata/files)
  // y la convierte en un Path
  public static Path parse(String raw) {
    // Si la ruta esta vacia o solo tiene espacios (" "), devuelve un Path vacio y relativo
    // (isAbsolute = false)
    if (raw == null || raw.isBlank()) return new Path(List.of(), false);

    String trimmed = raw.trim(); // borra los espacios en blanco al inicio y al final de un string
    boolean isAbsolute = trimmed.startsWith("/");

    // eliminar "/" inicial y final -> pq no quiero que los extremos vacios te generen segmentos
    // vacios despues
    String clean = trimmed.replaceAll("^/+", "").replaceAll("/+$", ""); // → "users/cata"

    List<String> parts =
        clean.isEmpty() // Si clean quedo vacio (por ejemplo si la ruta era solo /), devuelve una
            // lista vacia
            ? List.of()
            : Arrays.asList(
                clean.split("/")); // Si no, usa split("/") para dividir la ruta en partes por /
    // "users/cata/docs".split("/") → ["users", "cata", "docs"]

    return new Path(
        parts, isAbsolute); // Crea el Path con: la lista de segmentos y si era absoluta o no
  }

  // hace que cuando imprimas un Path, se vea bien: Une los segmentos con / y si era absoluto, le
  // agrega / al principio
  @Override
  public String toString() {
    if (segments.isEmpty()) return "/";
    return (isAbsolute ? "/" : "") + String.join("/", segments);
  }

  public Path append(Path other) {
    if (other.isAbsolute) {
      return other;
    }
    List<String> newSegments = new ArrayList<>(this.segments);
    newSegments.addAll(other.segments);
    return new Path(newSegments, this.isAbsolute);
  }

  public Path normalize() {
    List<String> result = new ArrayList<>();
    for (String segment : segments) {
      if (segment.equals("..")) {
        if (!result.isEmpty()) result.remove(result.size() - 1);
      } else if (!segment.equals(".")) {
        result.add(segment);
      }
    }
    return new Path(result, isAbsolute);
  }

  public static Path root() {
    return new Path(List.of(), true);
  }

  public Path parent() {
    if (segments.isEmpty()) return this;
    List<String> newSegments = new ArrayList<>(segments);
    newSegments.remove(newSegments.size() - 1);
    return new Path(newSegments, isAbsolute);
  }

  public boolean isEmpty() {
    return segments.isEmpty();
  }
}
