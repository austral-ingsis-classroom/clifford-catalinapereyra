package edu.austral.ingsis;

import edu.austral.ingsis.clifford.FileSystemEngine;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  private final FileSystemEngine engine = new FileSystemEngine();

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> output = new ArrayList<>();
    for (String command : commands) {
      output.add(engine.runCommand(command));
    }
    return output;
  }
}
