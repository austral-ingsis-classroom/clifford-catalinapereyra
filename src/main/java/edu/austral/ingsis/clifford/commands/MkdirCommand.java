package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.Optional;

public class MkdirCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    if (args.length < 2) return new CommandResult(state, "invalid mkdir syntax");

    Path currentPath = state.currentPath().normalize();
    Optional<Directory> maybeCurrent = Directory.resolvePath(state.root(), currentPath);

    if (maybeCurrent.isEmpty()) return new CommandResult(state, "current directory not found");

    Directory current = maybeCurrent.get();

    String name = args[1];
    if (current.hasChild(name)) {
      return new CommandResult(state, "'" + name + "' already exists");
    }

    Directory newChild = new Directory(name);
    Directory newCurrent = current.addChild(newChild);
    Directory newRoot = state.root().replace(current, newCurrent);

    return new CommandResult(
        new FileSystemState(newRoot, currentPath), "'" + name + "' directory created");
  }
}
