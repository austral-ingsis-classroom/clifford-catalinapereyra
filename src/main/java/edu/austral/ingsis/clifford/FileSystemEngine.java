package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.commands.*;
import java.util.*;

public class FileSystemEngine {
  private FileSystemState state;
  private final CommandChooser chooser;

  public FileSystemEngine() {
    this.state = FileSystemState.init();
    this.chooser =
        new CommandChooser(
            Map.of(
                "mkdir", new MkdirCommand(),
                "touch", new TouchCommand(),
                "cd", new CdCommand(),
                "ls", new LsCommand(),
                "pwd", new PwdCommand(),
                "rm", new RmCommand()));
  }

  public String runCommand(String rawCommand) {
    String[] parts = rawCommand.trim().split(" ");
    String cmdName = parts[0];

    Command command = chooser.choose(cmdName);
    CommandResult result = command.execute(parts, state);
    this.state = result.newState();
    return result.output();
  }
}
