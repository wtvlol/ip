import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves the task list using the local data file.
 */
public class Storage {
    /** Relative directory resolved using the current operating system's file system. */
    private static final Path DATA_DIRECTORY = Path.of("data");

    /** Data file built from path components without a platform-specific separator. */
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("groot.txt");

    /**
     * Replaces the data file with one line for each task in the current list.
     *
     * @param tasks Tasks to save.
     * @throws GrootException If the data directory or file cannot be written.
     */
    public static void saveTasks(List<Task> tasks) throws GrootException {
        Path temporaryFile = null;
        try {
            Files.createDirectories(DATA_DIRECTORY);
            temporaryFile = Files.createTempFile(DATA_DIRECTORY, "groot-", ".tmp");
            List<String> taskLines = tasks.stream()
                    .map(Task::toDataString)
                    .toList();
            Files.write(temporaryFile, taskLines);
            replaceDataFile(temporaryFile);
            temporaryFile = null;
        } catch (IOException | SecurityException error) {
            throw new GrootException(
                    "Oops! I couldn't save your tasks. Your last change was not applied.", error);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException ignored) {
                    // The original save error is more useful to the user.
                }
            }
        }
    }

    /**
     * Reconstructs the saved tasks, or returns an empty list on the first run.
     *
     * @return Tasks stored in the local data file.
     * @throws GrootException If an existing data file cannot be read or is malformed.
     */
    public static ArrayList<Task> loadTasks() throws GrootException {
        ArrayList<Task> tasks = new ArrayList<>();
        List<String> taskLines;
        try {
            if (Files.notExists(DATA_FILE)) {
                return tasks;
            }
            if (!Files.exists(DATA_FILE) || !Files.isRegularFile(DATA_FILE)) {
                throw new IOException("Data path is not a regular file");
            }
            taskLines = Files.readAllLines(DATA_FILE);
        } catch (IOException | SecurityException error) {
            throw new GrootException("Oops! I couldn't read your saved tasks.", error);
        }

        for (int i = 0; i < taskLines.size(); i++) {
            String taskLine = taskLines.get(i);
            if (taskLine.isBlank()) {
                continue;
            }
            tasks.add(parseTask(taskLine, i + 1));
        }
        return tasks;
    }

    /**
     * Replaces the old data only after the complete new file has been written.
     */
    private static void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, DATA_FILE, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException error) {
            Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Converts one validated data line into its corresponding task subtype.
     */
    private static Task parseTask(String taskLine, int lineNumber) throws GrootException {
        List<String> taskData = splitDataFields(taskLine);
        try {
            validateTaskData(taskData);
            Task task = switch (taskData.get(0)) {
            case "T" -> new Todo(taskData.get(2));
            case "D" -> new Deadline(taskData.get(2), taskData.get(3));
            case "E" -> new Event(taskData.get(2), taskData.get(3), taskData.get(4));
            default -> throw new IllegalArgumentException("Unknown task type");
            };
            if (taskData.get(1).equals("1")) {
                task.markAsDone();
            }
            return task;
        } catch (IllegalArgumentException error) {
            throw invalidDataException(lineNumber, error);
        }
    }

    /**
     * Splits a stored line while preserving escaped pipes and backslashes.
     */
    private static List<String> splitDataFields(String taskLine) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean isEscaped = false;
        for (char character : taskLine.toCharArray()) {
            if (isEscaped) {
                if (character == '\\' || character == '|') {
                    currentField.append(character);
                } else {
                    currentField.append('\\').append(character);
                }
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(character);
            }
        }
        if (isEscaped) {
            currentField.append('\\');
        }
        fields.add(currentField.toString().trim());
        return fields;
    }

    /**
     * Checks field counts, status values, and required text before construction.
     */
    private static void validateTaskData(List<String> taskData) {
        if (taskData.size() < 3 || taskData.get(2).isEmpty()) {
            throw new IllegalArgumentException("Missing task fields");
        }
        if (!taskData.get(1).equals("0") && !taskData.get(1).equals("1")) {
            throw new IllegalArgumentException("Invalid task status");
        }

        int expectedFieldCount = switch (taskData.get(0)) {
        case "T" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (taskData.size() != expectedFieldCount) {
            throw new IllegalArgumentException("Incorrect field count");
        }
        for (int i = 2; i < taskData.size(); i++) {
            if (taskData.get(i).isEmpty()) {
                throw new IllegalArgumentException("Empty task field");
            }
        }
    }

    /**
     * Creates a consistent user-facing error for malformed stored data.
     */
    private static GrootException invalidDataException(int lineNumber, Exception cause) {
        return new GrootException(
                "Oops! I couldn't load your tasks because line " + lineNumber
                        + " in " + DATA_FILE + " is invalid.", cause);
    }
}
