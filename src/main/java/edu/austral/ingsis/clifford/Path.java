package edu.austral.ingsis.clifford;

import java.util.Arrays;
import java.util.List;

// segments: una lista con los pedacitos de la ruta, por ejemplo ["users", "cata", "files"]
// isAbsolute: booleano que indica si la ruta empieza desde el root (/) o es relativa
public record Path(List<String> segments, boolean isAbsolute) {

  // Este metood recibe un String raw, osea, la ruta escrita por el usuario (ej: /users/cata/files)
  // y la convierte en un Path
  public static Path parse(String raw) {
    // Si la ruta esta vacia o solo tiene espacios (" "), devuelve un Path vacio y relativo
    // (isAbsolute = false)
    if (raw == null || raw.isBlank()) return new Path(List.of(), false);

    String trimmed = raw.trim(); // borra los espacios en blanco al inicio y al final de un string
    boolean isAbsolute =
        trimmed.startsWith(
            "/"); // Verifica si la ruta empieza con /, lo cual indica que es absoluta (desde la
    // raíz). "/users/cata" → true

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
    String joined = String.join("/", segments);
    return isAbsolute ? "/" + joined : joined; // condición ? siEsVerdadero : siEsFalso
  }
}
