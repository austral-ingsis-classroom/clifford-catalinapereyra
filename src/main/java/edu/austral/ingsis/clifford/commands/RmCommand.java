package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.Optional;

public class RmCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    if (args.length < 2) return new CommandResult(state, "invalid rm syntax");

    boolean recursive = false;
    String name;

    if (args.length == 2) {
      name = args[1];
    } else if (args.length == 3 && args[1].equals("--recursive")) {
      recursive = true;
      name = args[2];
    } else {
      return new CommandResult(state, "invalid rm syntax");
    }

    Path currentPath = state.getCurrentPath().normalize();
    Optional<Directory> maybeCurrent = Directory.resolvePath(state.getRoot(), currentPath);

    if (maybeCurrent.isEmpty()) return new CommandResult(state, "current directory not found");

    Directory current = maybeCurrent.get();
    Optional<FileSystemNode> maybeTarget = current.getChildByName(name);

    if (maybeTarget.isEmpty()) {
      return new CommandResult(state, "'" + name + "' does not exist");
    }

    FileSystemNode target = maybeTarget.get();

    if (target instanceof Directory && !recursive) {
      return new CommandResult(state, "cannot remove '" + name + "', is a directory");
    }

    Directory newCurrent = current.removeChild(name);
    Directory newRoot = state.getRoot().replace(current, newCurrent);

    return new CommandResult(new FileSystemState(newRoot, currentPath), "'" + name + "' removed");
  }
}
