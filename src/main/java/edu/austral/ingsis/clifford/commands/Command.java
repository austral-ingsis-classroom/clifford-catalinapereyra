package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.CommandResult;
import edu.austral.ingsis.clifford.FileSystemState;

public interface Command {
  CommandResult execute(String[] args, FileSystemState state);
}
