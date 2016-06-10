package com.spiddekauga.utils.command;

import java.util.Stack;

/**
 * Invokes commands and has the ability to undo/redo executions. Commands that should be able to
 * undo their execution needs to implement the {@link UndoCommand} interface.
 * <p>
 * Commands can be combinable, i.e. merging several commands of similar type, if they implement the
 * {@link CombinableCommand} interface.
 * <p>
 * The Invoker will automatically dispose any resources when the command isn't used anymore if they
 * implement the {@link UndoCommand} interface.
 */
public class Invoker {
private Stack<UndoInfo> mUndoCommands = new Stack<>();
private Stack<UndoInfo> mRedoCommands = new Stack<>();

/**
 * Clears the undo and redo list.
 */
public void clear() {
	clearUndo();
	clearRedo();
}

/**
 * Clears the undo stack
 */
public void clearUndo() {
	for (UndoInfo undoInfo : mUndoCommands) {
		if (undoInfo.mCommand instanceof DisposableCommand) {
			((DisposableCommand) undoInfo.mCommand).dispose();
		}
	}
	mUndoCommands.clear();
}

/**
 * Clears the redo stack
 */
public void clearRedo() {
	for (UndoInfo undoInfo : mRedoCommands) {
		if (undoInfo.mCommand instanceof DisposableCommand) {
			((DisposableCommand) undoInfo.mCommand).dispose();
		}
	}
	mRedoCommands.clear();
}

/**
 * Execute the specified command. Pushes this command onto the undo stack if it implements the
 * {@link UndoCommand} and was successfully executed.
 * @param command execute this command
 * @return true if the command was successfully executed.
 * @see #execute(Command, boolean) to create a {@link CombinableCommand}.
 */
public boolean execute(Command command) {
	return execute(command, false);
}

/**
 * Execute the specified command. Pushes this command onto the undo stack if it implements the
 * {@link UndoCommand} and was successfully executed. If the command is set as chained, this will
 * work as the command were inside an {@link CombinableCommand}. The first command should NOT be set
 * as chained, only subsequently commands.
 * @param command execute this command
 * @param chained set all except the first command as chained to work as a {@link
 * CombinableCommand}.
 * @return true if the command was successfully executed.
 */
public boolean execute(Command command, boolean chained) {
	// Same command type and combinable?
	boolean combined = false;
	if (command instanceof CombinableCommand && canUndo()) {
		UndoCommand previousCommand = mUndoCommands.peek().mCommand;

		if (previousCommand.getClass() == command.getClass()) {
			combined = ((CombinableCommand) previousCommand).combine((CombinableCommand) command);
		}
	}

	boolean success = combined;

	if (!combined) {
		success = command.execute();
		if (success) {
			if (command instanceof UndoCommand) {
				UndoCommand undoCommand = (UndoCommand) command;
				UndoInfo undoInfo = new UndoInfo(undoCommand, chained);
				mUndoCommands.push(undoInfo);
				clearRedo();
			} else if (command instanceof DisposableCommand) {
				((DisposableCommand) command).dispose();
			}
		} else if (command instanceof DisposableCommand) {
			((DisposableCommand) command).dispose();
		}
	}

	return success;
}

/**
 * @return true if there are commands available to undo
 */
public boolean canUndo() {
	return !mUndoCommands.isEmpty();
}

/**
 * Push a delimiter onto the undo stack. Useful when we want to separate two {@link
 * CombinableCommand}s, e.g. adding a delimiter for sliders when a the mouse button is pressed. A
 * delimiter is never chained, but the next command should be.
 */
public void pushDelimiter() {
	pushDelimiter("");
}

/**
 * Push a named delimiter onto the undo stack. Useful both when wanting to undo to this named
 * delimiter and separate two {@link CombinableCommand}s, e.g. adding a delimiter for sliders when a
 * the mouse button is pressed. A delimiter is never chained, but the next command should be.
 * @param name delimiter name
 */
public void pushDelimiter(String name) {
	DelimiterCommand delimiterCommand = new DelimiterCommand(name);
	mUndoCommands.push(new UndoInfo(delimiterCommand, false));
}

/**
 * Undo all commands to and including the specified delimiter
 * @param name delimiter name to undo to and including
 * @param addToRedoStack set to true to add all undone commands to the redo stack
 */
public void undoToDelimiter(String name, boolean addToRedoStack) {
	boolean foundDelimiter = false;
	while (canUndo() && !foundDelimiter) {
		UndoInfo undoInfo = mUndoCommands.pop();
		UndoCommand undoCommand = undoInfo.mCommand;

		undo(undoInfo, addToRedoStack);

		// Check if found
		if (undoCommand instanceof DelimiterCommand) {
			foundDelimiter = ((DelimiterCommand) undoCommand).getName().equals(name);
		}
	}
}

private void undo(UndoInfo undoInfo, boolean addToRedoStack) {
	UndoCommand undoCommand = undoInfo.mCommand;
	boolean success = undoCommand.undo();
	if (addToRedoStack && success) {
		mRedoCommands.push(undoInfo);
	} else if (undoCommand instanceof DisposableCommand) {
		((DisposableCommand) undoCommand).dispose();
	}
}

/**
 * Undoes the last executed command, or commands if they have been chained. Adds the undone commands
 * to the redo stack.
 * @see #undo(boolean) for undoing command(s) and not add it to the redo stack
 */
public void undo() {
	undo(true);
}

/**
 * Undoes the last executed command, or commands if they have been chained.
 * @param addToRedoStack set to true to add all undone commands to the redo stack (same as calling
 * {@link #undo()}.
 */
public void undo(boolean addToRedoStack) {
	boolean nextIsChained = true;
	while (canUndo() && nextIsChained) {
		UndoInfo undoInfo = mUndoCommands.pop();
		UndoCommand undoCommand = undoInfo.mCommand;
		nextIsChained = undoInfo.mChained;

		undo(undoInfo, addToRedoStack);
	}
}

/**
 * Redo the last undone command(s).
 */
public void redo() {
	boolean nextIsChained = true;
	while (canRedo() && nextIsChained) {
		UndoInfo redoInfo = mRedoCommands.pop();

		redo(redoInfo);

		// Execute next command?
		if (canRedo()) {
			nextIsChained = mRedoCommands.peek().mChained;
		}
	}
}

/**
 * @return true if there are commands available to redo
 */
public boolean canRedo() {
	return !mRedoCommands.isEmpty();
}

private void redo(UndoInfo redoInfo) {
	Command redoCommand = redoInfo.mCommand;
	boolean success = redoCommand.execute();

	if (success) {
		mUndoCommands.push(redoInfo);
	} else if (redoCommand instanceof DisposableCommand) {
		((DisposableCommand) redoCommand).dispose();
	}
}

private class UndoInfo {
	private UndoCommand mCommand;
	private boolean mChained;

	private UndoInfo(UndoCommand command, boolean chained) {
		mCommand = command;
		mChained = chained;
	}
}
}
