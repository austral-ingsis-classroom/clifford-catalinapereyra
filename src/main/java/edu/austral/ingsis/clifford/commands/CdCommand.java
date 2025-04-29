package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.Optional;

public class CdCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    if (args.length < 2) return new CommandResult(state, "invalid cd syntax");

    String pathStr = args[1];
    Path path = Path.parse(pathStr);
    Path newPath;

    if (path.isAbsolute()) {
      newPath = path.normalize(); // Si el path es absoluto, normalizalo directo
    } else {
      newPath =
          state
              .currentPath()
              .append(path)
              .normalize(); // Si es relativo, lo agregás al actual y normalizás
    }

    Optional<Directory> maybeNewCurrent = Directory.resolvePath(state.root(), newPath);

    if (maybeNewCurrent.isEmpty()) {
      return new CommandResult(state, "'" + newPath.toString() + "' directory does not exist");
    }

    String displayPath = newPath.toString().isEmpty() ? "/" : newPath.toString();

    return new CommandResult(
        new FileSystemState(state.root(), newPath), "moved to directory '" + displayPath + "'");
  }
}
