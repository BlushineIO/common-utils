package io.blushine.utils.command;

/**
 * Implement this interface if a command needs to dispose resources after it has been used. Works
 * automatically if executed through an {@link Invoker}.
 */
public interface DisposableCommand extends Command {
/**
 * Dispose resources used by the command
 */
void dispose();
}
