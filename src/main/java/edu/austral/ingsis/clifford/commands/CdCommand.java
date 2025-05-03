package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.Optional;

public class CdCommand implements Command {

  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    if (args.length < 2) return new CommandResult(state, "cd: missing operand");

    String rawPath = args[1];
    Path inputPath = Path.parse(rawPath);
    Path absolutePath =
        inputPath.isAbsolute()
            ? inputPath.normalize()
            : state.getCurrentPath().append(inputPath).normalize();

    Optional<Directory> maybeDir = Directory.resolvePath(state.getRoot(), absolutePath);

    if (maybeDir.isEmpty()) {
      return new CommandResult(state, "'" + rawPath + "' directory does not exist");
    }

    Directory targetDir = maybeDir.get();
    return new CommandResult(
        state.moveTo(absolutePath), "moved to directory '" + targetDir.getName() + "'");
  }
}
