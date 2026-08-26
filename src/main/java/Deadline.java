import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a given date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Date by which this task must be completed. */
    private final LocalDate by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description Description of the task.
     * @param by Date by which the task must be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline in the format used by the local data file.
     *
     * @return Pipe-separated deadline data.
     */
    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + by;
    }

    /**
     * Returns the deadline in its display format.
     *
     * @return Formatted deadline text.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
