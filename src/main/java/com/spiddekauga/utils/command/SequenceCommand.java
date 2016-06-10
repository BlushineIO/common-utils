package com.spiddekauga.utils.command;

/**
 * Wrapper to execute several commands in a sequence.
 */
public class SequenceCommand implements UndoCommand {
private Command[] mCommands;

/**
 * Execute these commands in a sequence
 * @param commands the commands that should be executed in a sequence
 */
public SequenceCommand(Command... commands) {
	mCommands = commands;
}

@Override
public boolean execute() {
	for (Command command : mCommands) {
		if (!command.execute()) {
			return false;
		}
	}

	return true;
}

@Override
public boolean undo() {
	// Execute the commands in the reverse order
	for (int i = mCommands.length - 1; i >= 0; --i) {
		Command command = mCommands[i];
		if (command instanceof UndoCommand) {
			UndoCommand undoCommand = (UndoCommand) command;
			if (!undoCommand.undo()) {
				return false;
			}
		}
	}

	return true;
}
}
