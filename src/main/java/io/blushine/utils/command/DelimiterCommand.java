package io.blushine.utils.command;

/**
 * Empty command that delimits two commands. Useful both when wanting to undo to a specified
 * delimiter and separate two {@link CombinableCommand}s, e.g. adding a delimiter for sliders when a
 * the mouse button is pressed. A delimiter is never chained, but the next command should be.
 */
class DelimiterCommand implements UndoCommand {
private String mName = "";

/**
 * Create a default delimiter without a name
 */
DelimiterCommand() {
}

/**
 * Create a delimiter with a specified name
 * @param name delimiter name
 */
DelimiterCommand(String name) {
	mName = name;
}

@Override
public boolean execute() {
	return true;
}

@Override
public boolean undo() {
	return true;
}

/**
 * @return name of the delimiter command
 */
public String getName() {
	return mName;
}
}
