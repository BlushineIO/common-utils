package com.spiddekauga.utils.command;

/**
 * Implement this interface when a command is to be combinable with itself. E.g. when changing a
 * slider it might only be necessary to save the first and last value and not all command executions
 * in between. Only works if executed through an {@link Invoker}.
 */
public interface CombinableCommand extends Command {
/**
 * Combines the otherCommand (to be executed) with this command (that has been executed already).
 * This method should call execute on the specified command.
 * @param otherCommand other command (to be executed).
 * @return true if otherCommand executed successfully, and thus was combined with this command.
 */
boolean combine(CombinableCommand otherCommand);
}
