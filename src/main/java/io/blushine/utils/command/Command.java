package io.blushine.utils.command;

/**
 * Common usages are creating a command or command that should be executed later or multiple times.
 * It is also possible to combine commands using {@link SequenceCommand}, or undoing commands using
 * {@link UndoCommand}.
 * <p>
 * commands can be executed directly or through an {@link Invoker}. If a command is executed through
 * the Invoker it stores all undo commands and can thus undo and redo commands. If a command can be
 * combined with itself it should implement the {@link CombinableCommand}; e.g. when changing a
 * slider it might only be necessary to save the first and last value and not all the command
 * executions in between. If a command is to be disposed after discarding the command it should
 * implement the {@link DisposableCommand} interface.
 */
public interface Command {
/**
 * Execute the command.
 * @return true if the command was successfully executed. If this is an {@link UndoCommand} and it
 * has been executed through an {@link Invoker}, it will store the command in the undo queue only if
 * this method returns true.
 */
boolean execute();
}
