package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.time.Instant;
import java.util.Optional;

public class TouchCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    if (args.length < 2) return new CommandResult(state, "invalid touch syntax");

    String name = args[1];
    Path currentPath = state.getCurrentPath().normalize();

    Optional<Directory> maybeCurrent = Directory.resolvePath(state.getRoot(), currentPath);
    if (maybeCurrent.isEmpty()) return new CommandResult(state, "current directory not found");

    Directory current = maybeCurrent.get();

    if (current.hasChild(name)) {
      return new CommandResult(state, "'" + name + "' already exists");
    }

    File newFile = new File(name, Instant.now());
    Directory newCurrent = current.addChild(newFile);
    Directory newRoot = state.getRoot().replace(current, newCurrent);

    return new CommandResult(
        new FileSystemState(newRoot, currentPath), "'" + name + "' file created");
  }
}
