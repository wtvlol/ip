import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores todos, deadlines, and events; manages their status; handles input errors;
 * and exits on {@code bye}.
 */
public class Groot {
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

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            System.out.println(separator);

            try {
                if (command.equals("bye")) {
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println(separator);
                    break;
                }

                if (command.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " task"
                            + (tasks.size() == 1 ? "" : "s") + " in the list.");
                } else {
                    Task task = createTask(command);
                    tasks.add(task);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " task"
                            + (tasks.size() == 1 ? "" : "s") + " in the list.");
                }
            } catch (GrootException error) {
                System.out.println(" " + error.getMessage());
            }
            System.out.println(separator);
        }
    }

    /**
     * Converts a task command into the corresponding task subtype.
     *
     * @param command Full command entered by the user.
     * @return A todo, deadline, or event described by the command.
     * @throws GrootException If the command is unknown or required task details are missing.
     */
    private static Task createTask(String command) throws GrootException {
        if (command.isEmpty()) {
            throw new GrootException("Oops! Please enter a command.");
        }

        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new GrootException("Oops! A todo needs a description.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String arguments = command.substring(8).trim();
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
                throw new GrootException("Oops! A deadline needs a date or time after /by.");
            }
            return new Deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String arguments = command.substring(5).trim();
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

        throw new GrootException("Oops! I don't recognise that command.");
    }

    /**
     * Parses and validates the one-based task number in a mark, unmark, or delete command.
     *
     * @param command Full command entered by the user.
     * @param action Command action, such as {@code mark}, {@code unmark}, or {@code delete}.
     * @param taskCount Number of tasks currently stored.
     * @return Zero-based index of the selected task.
     * @throws GrootException If the task number is missing, non-numeric, or out of range.
     */
    private static int getTaskIndex(String command, String action, int taskCount)
            throws GrootException {
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
