package edu.austral.ingsis.clifford;

import java.time.Instant;

public sealed interface FileSystemNode permits Directory, File {
  String getName();

  String getType();

  Instant getCreatedAt(); // para ver si es directory o file
}
