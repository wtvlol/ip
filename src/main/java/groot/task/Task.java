package groot.task;

/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    /** Human-readable details of the task. */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the character used to display the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if the task is done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Escapes characters that have structural meaning in the data file.
     *
     * @param value Task text to store.
     * @return Text safe for the pipe-separated storage format.
     */
    protected static String escapeDataField(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Returns the task in the format used by the local data file.
     *
     * @return Pipe-separated task data.
     */
    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + escapeDataField(description);
    }

    /**
     * Returns the task in its display format, including its status icon.
     *
     * @return Formatted task text.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
