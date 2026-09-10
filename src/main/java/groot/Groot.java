package groot;

import java.util.Scanner;

import groot.exception.GrootException;
import groot.parser.CommandType;
import groot.parser.Parser;
import groot.storage.Storage;
import groot.task.Task;
import groot.task.TaskList;

/**
 * Stores todos, deadlines, and events; manages their status; handles input errors;
 * and exits on {@code bye}.
 */
public class Groot {
    /** Summary of every command supported by Groot. */
    private static final String HELP_MESSAGE = String.join("\n",
            " Here are the commands you can use:",
            "   todo DESCRIPTION - Add a todo task.",
            "   deadline DESCRIPTION /by YYYY-MM-DD - Add a deadline task.",
            "   event DESCRIPTION /from START /to END - Add an event task.",
            "   list - Show all tasks.",
            "   find KEYWORD - Find tasks by description.",
            "   mark NUMBER - Mark a task as done.",
            "   unmark NUMBER - Mark a task as not done.",
            "   delete NUMBER - Delete a task.",
            "   help, --help, -h - Show this help message.",
            "   bye - Exit Groot.");

    private final Parser parser;
    private final TaskList tasks;

    /**
     * Creates a Groot application instance and loads its saved tasks.
     *
     * @throws GrootException If the saved tasks cannot be loaded.
     */
    public Groot() throws GrootException {
        parser = new Parser();
        tasks = new TaskList(Storage.loadTasks());
    }

    /**
     * Starts Groot, loads saved tasks, and processes commands until the user exits.
     *
     * @param args Command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        String separator = "_".repeat(60);
        String banner = """
                  \\  |  /
                ___\\_|_/___
               /   /   \\   \\
              /   | o o |    |
             |    |  ^  |    |
             |    \\ \\_/ /    |
              \\    '---'    /
               \\  |||||||  /
                | ||||||| |
             ___|_|||||||_|___
            /     |||||||     \\
           /      |||||||      \\
                  |||||||
                 /||| |||\\
                /_||| |||_\\
                """;

        System.out.println(separator);
        System.out.print(banner);
        System.out.println("Hello! I'm Groot.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Groot groot;
        try {
            groot = new Groot();
        } catch (GrootException error) {
            System.out.println(" " + error.getMessage());
            System.out.println(separator);
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            System.out.println(separator);
            System.out.println(groot.getResponse(command));
            System.out.println(separator);
            if (command.equals("bye")) {
                break;
            }
        }
    }

    /**
     * Processes one user command and returns Groot's response.
     *
     * @param command User command to process.
     * @return Groot's response to the command.
     */
    public String getResponse(String command) {
        try {
            CommandType commandType = parser.parseCommandType(command);
            return executeCommand(command, commandType);
        } catch (GrootException error) {
            return " " + error.getMessage();
        }
    }

    /**
     * Executes a parsed command and returns its success response.
     *
     * @param command Original command text.
     * @param commandType Parsed command type.
     * @return Response describing the command result.
     * @throws GrootException If command arguments or saved-task operations fail.
     */
    private String executeCommand(String command, CommandType commandType) throws GrootException {
        return switch (commandType) {
            case BYE -> " Bye. Hope to see you again soon!";
            case LIST -> getTaskListResponse(tasks.asList(), " Here are the tasks in your list:");
            case FIND -> findTasks(command);
            case MARK, UNMARK -> updateTaskStatus(command, commandType);
            case DELETE -> deleteTask(command);
            case TODO, DEADLINE, EVENT -> addTask(command, commandType);
            case HELP -> HELP_MESSAGE;
            default -> throw new IllegalStateException("Parser returned an unknown command");
        };
    }

    /**
     * Finds tasks without changing the stored list and formats the matching results.
     */
    private String findTasks(String command) throws GrootException {
        String keyword = parser.parseFindKeyword(command);
        return getTaskListResponse(tasks.find(keyword), " Here are the matching tasks in your list:");
    }

    /**
     * Applies a mark or unmark command and restores the previous status if saving fails.
     */
    private String updateTaskStatus(String command, CommandType commandType) throws GrootException {
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        boolean isDone = commandType == CommandType.MARK;
        tasks.setDone(taskIndex, isDone);
        try {
            Storage.saveTasks(tasks.asList());
        } catch (GrootException error) {
            tasks.setDone(taskIndex, wasDone);
            throw error;
        }
        String response = isDone ? " Nice! I've marked this task as done:\n   "
                : " OK, I've marked this task as not done yet:\n   ";
        return response + task;
    }

    /**
     * Deletes a task and restores its original position if saving fails.
     */
    private String deleteTask(String command) throws GrootException {
        int taskIndex = parser.parseTaskIndex(command, CommandType.DELETE, tasks.size());
        Task removedTask = tasks.delete(taskIndex);
        try {
            Storage.saveTasks(tasks.asList());
        } catch (GrootException error) {
            tasks.add(taskIndex, removedTask);
            throw error;
        }
        return " Noted. I've removed this task:\n   " + removedTask + "\n Now you have "
                + getTaskCountDescription() + ".";
    }

    /**
     * Adds a parsed task and removes it again if saving fails.
     */
    private String addTask(String command, CommandType commandType) throws GrootException {
        Task task = parser.createTask(command, commandType);
        tasks.add(task);
        try {
            Storage.saveTasks(tasks.asList());
        } catch (GrootException error) {
            tasks.delete(tasks.size() - 1);
            throw error;
        }
        return " Got it. I've added this task:\n   " + task + "\n Now you have "
                + getTaskCountDescription() + ".";
    }

    /**
     * Formats tasks as a numbered response list.
     *
     * @param listedTasks Tasks to include.
     * @param heading Heading displayed before the tasks.
     * @return Numbered task-list response.
     */
    private String getTaskListResponse(Iterable<Task> listedTasks, String heading) {
        StringBuilder response = new StringBuilder(heading);
        int taskNumber = 1;
        for (Task task : listedTasks) {
            response.append("\n ").append(taskNumber).append(".").append(task);
            taskNumber++;
        }
        return response.toString();
    }

    /**
     * Returns the current task count with the correct singular or plural noun.
     *
     * @return Human-readable task count.
     */
    private String getTaskCountDescription() {
        return tasks.size() + " task" + (tasks.size() == 1 ? "" : "s") + " in the list";
    }
}
