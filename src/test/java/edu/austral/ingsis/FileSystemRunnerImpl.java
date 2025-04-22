package edu.austral.ingsis;

import edu.austral.ingsis.clifford.FileSystemState;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  private final FileSystemState state = new FileSystemState();

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> output = new ArrayList<>();

    for (String command : commands) {
      output.add(runCommand(command));
    }

    return output;
  }

  private String runCommand(String rawCommand) {
    String[] parts = rawCommand.trim().split(" ");
    String cmd = parts[0];

    switch (cmd) {
      case "mkdir" -> {
        if (parts.length < 2) return "invalid mkdir syntax";
        return state.mkdir(parts[1]);
      }
      case "touch" -> {
        if (parts.length < 2) return "invalid touch syntax";
        return state.touch(parts[1]);
      }
      case "cd" -> {
        if (parts.length < 2) return "invalid cd syntax";
        return state.cd(parts[1]);
      }
      case "ls" -> {
        String flag = parts.length > 1 ? parts[1] : null;
        return state.ls(flag);
      }
      case "pwd" -> {
        return state.pwd();
      }
      case "rm" -> {
        if (parts.length == 2) {
          return state.rm(parts[1], false);
        } else if (parts.length == 3 && parts[1].equals("--recursive")) {
          return state.rm(parts[2], true);
        } else {
          return "invalid rm syntax";
        }
      }
      default -> {
        return "command not found";
      }
    }
  }
}
