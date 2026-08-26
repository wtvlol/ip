package groot.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import groot.exception.GrootException;
import groot.task.Deadline;
import groot.task.Event;
import groot.task.Task;
import groot.task.Todo;

/**
 * Interprets user input and converts command arguments into application objects.
 */
public class Parser {

    /**
     * Identifies and validates the command represented by the user's input.
     *
     * @param command Trimmed command entered by the user.
     * @return Recognized command type.
     * @throws GrootException If the command is empty or unknown.
     */
    public CommandType parseCommandType(String command) throws GrootException {
        if (command.isEmpty()) {
            throw new GrootException("Oops! Please enter a command.");
        }

        CommandType commandType = CommandType.from(command);
        if (commandType == CommandType.UNKNOWN) {
            throw new GrootException("Oops! I don't recognise that command.");
        }
        return commandType;
    }

    /**
     * Converts a task command into the corresponding task subtype.
     *
     * @param command Full command entered by the user.
     * @param commandType Type of task to create.
     * @return A todo, deadline, or event described by the command.
     * @throws GrootException If required task details are missing or invalid.
     */
    public Task createTask(String command, CommandType commandType) throws GrootException {
        if (commandType == CommandType.TODO) {
            String description = command.substring(commandType.getKeyword().length()).trim();
            if (description.isEmpty()) {
                throw new GrootException("Oops! A todo needs a description.");
            }
            return new Todo(description);
        }

        if (commandType == CommandType.DEADLINE) {
            String arguments = command.substring(commandType.getKeyword().length()).trim();
            int byIndex = arguments.indexOf("/by");
            if (byIndex < 0) {
                throw new GrootException("Oops! Use: deadline DESCRIPTION /by DATE");
            }
            String description = arguments.substring(0, byIndex).trim();
            String by = arguments.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new GrootException("Oops! A deadline needs a description.");
            }
            if (by.isEmpty()) {
                throw new GrootException("Oops! A deadline needs a date after /by.");
            }
            try {
                return new Deadline(description, LocalDate.parse(by));
            } catch (DateTimeParseException error) {
                throw new GrootException(
                        "Oops! Use deadline dates in yyyy-MM-dd format, e.g. 2019-10-15.");
            }
        }

        if (commandType == CommandType.EVENT) {
            String arguments = command.substring(commandType.getKeyword().length()).trim();
            int fromIndex = arguments.indexOf("/from");
            int toIndex = fromIndex < 0 ? -1 : arguments.indexOf("/to", fromIndex + 5);
            if (fromIndex < 0 || toIndex < 0) {
                throw new GrootException("Oops! Use: event DESCRIPTION /from START /to END");
            }
            String description = arguments.substring(0, fromIndex).trim();
            String from = arguments.substring(fromIndex + 5, toIndex).trim();
            String to = arguments.substring(toIndex + 3).trim();
            if (description.isEmpty()) {
                throw new GrootException("Oops! An event needs a description.");
            }
            if (from.isEmpty()) {
                throw new GrootException("Oops! An event needs a start date or time after /from.");
            }
            if (to.isEmpty()) {
                throw new GrootException("Oops! An event needs an end date or time after /to.");
            }
            return new Event(description, from, to);
        }

        throw new IllegalArgumentException("Command type does not create a task: " + commandType);
    }

    /**
     * Parses and validates the one-based task number in a mark, unmark, or delete command.
     *
     * @param command Full command entered by the user.
     * @param commandType Type of command that selects a task.
     * @param taskCount Number of tasks currently stored.
     * @return Zero-based index of the selected task.
     * @throws GrootException If the task number is missing, non-numeric, or out of range.
     */
    public int parseTaskIndex(String command, CommandType commandType, int taskCount)
            throws GrootException {
        String action = commandType.getKeyword();
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            throw new GrootException("Oops! Tell me which task to " + action + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException error) {
            throw new GrootException("Oops! The task number must be a whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new GrootException("Oops! Task " + taskNumber + " is not in the list.");
        }
        return taskNumber - 1;
    }
}
