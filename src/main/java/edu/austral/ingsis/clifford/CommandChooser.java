package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.commands.Command;
import java.util.Map;

// es el que sabe q comando ejecutar segun el nombre que vos le pases

public class CommandChooser {
  private final Map<String, Command> commandMap;

  public CommandChooser(Map<String, Command> commandMap) {
    this.commandMap = commandMap;
  }

  public Command choose(String name) {
    return commandMap.getOrDefault(
        name, (args, state) -> new CommandResult(state, "command not found"));
  }
}
