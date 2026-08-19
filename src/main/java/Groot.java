import java.util.Scanner;

/**
 * Greets the user, echoes their commands, and exits when they enter {@code bye}.
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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(" " + command);
            System.out.println(separator);
        }
    }
}
