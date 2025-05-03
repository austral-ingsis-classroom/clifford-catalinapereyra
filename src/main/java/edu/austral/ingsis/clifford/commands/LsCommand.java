package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LsCommand implements Command {
  @Override
  public CommandResult execute(String[] args, FileSystemState state) {
    String flag = (args.length > 1) ? args[1] : null;
    Order order;

    if (flag == null || flag.isBlank()) {
      order = Order.DEFAULT;
    } else if (flag.trim().equals("--ord=asc")) {
      order = Order.ASC;
    } else if (flag.trim().equals("--ord=desc")) {
      order = Order.DESC;
    } else {
      return new CommandResult(state, "invalid flag");
    }

    Path normalizedPath = state.getCurrentPath().normalize();
    Optional<Directory> maybeCurrent = Directory.resolvePath(state.getRoot(), normalizedPath);
    if (maybeCurrent.isEmpty()) return new CommandResult(state, "current directory not found");

    Directory current = maybeCurrent.get();
    List<FileSystemNode> items = current.listChildren(order);
    if (items.isEmpty()) return new CommandResult(state, "");

    String result = items.stream().map(FileSystemNode::getName).collect(Collectors.joining(" "));
    return new CommandResult(state, result);
  }
}
