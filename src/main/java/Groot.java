import java.util.Scanner;

/**
 * Stores todos, deadlines, and events; manages their status; and exits on {@code bye}.
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

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                tasks[taskCount] = new Todo(description);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[taskCount]);
                taskCount++;
                System.out.println(" Now you have " + taskCount + " task"
                        + (taskCount == 1 ? "" : "s") + " in the list.");
            } else if (command.startsWith("deadline ")) {
                String arguments = command.substring(9);
                int byIndex = arguments.indexOf(" /by ");
                String description = arguments.substring(0, byIndex);
                String by = arguments.substring(byIndex + 5);
                tasks[taskCount] = new Deadline(description, by);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[taskCount]);
                taskCount++;
                System.out.println(" Now you have " + taskCount + " task"
                        + (taskCount == 1 ? "" : "s") + " in the list.");
            } else if (command.startsWith("event ")) {
                String arguments = command.substring(6);
                int fromIndex = arguments.indexOf(" /from ");
                int toIndex = arguments.indexOf(" /to ", fromIndex + 7);
                String description = arguments.substring(0, fromIndex);
                String from = arguments.substring(fromIndex + 7, toIndex);
                String to = arguments.substring(toIndex + 5);
                tasks[taskCount] = new Event(description, from, to);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[taskCount]);
                taskCount++;
                System.out.println(" Now you have " + taskCount + " task"
                        + (taskCount == 1 ? "" : "s") + " in the list.");
            }
            System.out.println(separator);
        }
    }
}
