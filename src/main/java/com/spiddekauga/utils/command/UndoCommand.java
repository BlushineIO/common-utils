package com.spiddekauga.utils.command;

/**
 * Implement this interface if a command should be able to be undone, i.e. revert it's execution.
 * Works automatically if executed through an {@link Invoker}.
 */
public interface UndoCommand extends Command {
/**
 * Undo the command
 * @return true if the command was successfully undone
 */
boolean undo();
}
