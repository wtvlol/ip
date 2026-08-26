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

    /**
     * Creates a Groot application instance.
     */
    public Groot() {
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

        TaskList tasks;
        try {
            tasks = new TaskList(Storage.loadTasks());
        } catch (GrootException error) {
            System.out.println(" " + error.getMessage());
            System.out.println(separator);
            return;
        }
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        inputLoop:
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            System.out.println(separator);

            try {
                CommandType commandType = parser.parseCommandType(command);
                switch (commandType) {
                case BYE:
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println(separator);
                    break inputLoop;
                case LIST:
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    break;
                case MARK:
                    int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
                    Task markedTask = tasks.get(taskIndex);
                    boolean wasDone = markedTask.isDone();
                    tasks.markAsDone(taskIndex);
                    try {
                        Storage.saveTasks(tasks.asList());
                    } catch (GrootException error) {
                        tasks.setDone(taskIndex, wasDone);
                        throw error;
                    }
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + markedTask);
                    break;
                case UNMARK:
                    int unmarkedTaskIndex = parser.parseTaskIndex(
                            command, commandType, tasks.size());
                    Task unmarkedTask = tasks.get(unmarkedTaskIndex);
                    boolean wasUnmarkedTaskDone = unmarkedTask.isDone();
                    tasks.markAsNotDone(unmarkedTaskIndex);
                    try {
                        Storage.saveTasks(tasks.asList());
                    } catch (GrootException error) {
                        tasks.setDone(unmarkedTaskIndex, wasUnmarkedTaskDone);
                        throw error;
                    }
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + unmarkedTask);
                    break;
                case DELETE:
                    int deletedTaskIndex = parser.parseTaskIndex(
                            command, commandType, tasks.size());
                    Task removedTask = tasks.delete(deletedTaskIndex);
                    try {
                        Storage.saveTasks(tasks.asList());
                    } catch (GrootException error) {
                        tasks.add(deletedTaskIndex, removedTask);
                        throw error;
                    }
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " task"
                            + (tasks.size() == 1 ? "" : "s") + " in the list.");
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = parser.createTask(command, commandType);
                    tasks.add(task);
                    try {
                        Storage.saveTasks(tasks.asList());
                    } catch (GrootException error) {
                        tasks.delete(tasks.size() - 1);
                        throw error;
                    }
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " task"
                            + (tasks.size() == 1 ? "" : "s") + " in the list.");
                    break;
                case UNKNOWN:
                    throw new IllegalStateException("Parser returned an unknown command");
                }
            } catch (GrootException error) {
                System.out.println(" " + error.getMessage());
            }
            System.out.println(separator);
        }
    }
}
