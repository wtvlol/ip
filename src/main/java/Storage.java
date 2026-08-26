import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves the task list using the local data file.
 */
public class Storage {
    private static final Path DATA_FILE = Path.of("data", "groot.txt");

    /**
     * Replaces the data file with one line for each task in the current list.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public static void saveTasks(List<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());
        List<String> taskLines = tasks.stream()
                .map(Task::toDataString)
                .toList();
        Files.write(DATA_FILE, taskLines);
    }

    /**
     * Reconstructs the saved tasks, or returns an empty list on the first run.
     *
     * @return Tasks stored in the local data file.
     * @throws IOException If an existing data file cannot be read.
     */
    public static ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        for (String taskLine : Files.readAllLines(DATA_FILE)) {
            String[] taskData = taskLine.split("\\s*\\|\\s*");
            Task task = switch (taskData[0]) {
            case "T" -> new Todo(taskData[2]);
            case "D" -> new Deadline(taskData[2], taskData[3]);
            case "E" -> new Event(taskData[2], taskData[3], taskData[4]);
            default -> throw new IllegalArgumentException("Unknown task type: " + taskData[0]);
            };
            if (taskData[1].equals("1")) {
                task.markAsDone();
            }
            tasks.add(task);
        }
        return tasks;
    }
}
