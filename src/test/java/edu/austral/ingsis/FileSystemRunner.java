package edu.austral.ingsis;

import java.util.List;

public interface FileSystemRunner {
  List<String> executeCommands(
      List<String>
          commands); // Recibe una lista de comandos (como si los escribiera en la terminal) y
  // devuelve una lista de respuestas
}
