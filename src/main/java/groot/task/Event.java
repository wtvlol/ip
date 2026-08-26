package groot.task;

/**
 * Represents a task that occurs between a given start and end date or time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description Description of the event.
     * @param from Start date or time of the event.
     * @param to End date or time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event in the format used by the local data file.
     *
     * @return Pipe-separated event data.
     */
    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + escapeDataField(from)
                + " | " + escapeDataField(to);
    }

    /**
     * Returns the event in its display format.
     *
     * @return Formatted event text.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
