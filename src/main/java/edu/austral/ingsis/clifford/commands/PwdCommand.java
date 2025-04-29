package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class PwdCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    Path currentPath = state.currentPath().normalize();

    String pathString = currentPath.toString();
    if (pathString.isEmpty()) {
      pathString = "/";
    }

    return new CommandResult(state, pathString);
  }
}
