package groot.parser;

/**
 * Represents a command that Groot can process.
 */
public enum CommandType {
    /** Ends the current Groot session. */
    BYE("bye", false),
    /** Displays all tasks. */
    LIST("list", false),
    /** Displays tasks whose descriptions contain a keyword. */
    FIND("find", true),
    /** Marks a selected task as completed. */
    MARK("mark", true),
    /** Marks a selected task as incomplete. */
    UNMARK("unmark", true),
    /** Removes a selected task. */
    DELETE("delete", true),
    /** Adds a task without a date or time. */
    TODO("todo", true),
    /** Adds a task with a completion date. */
    DEADLINE("deadline", true),
    /** Adds a task with a start and end. */
    EVENT("event", true),
    /** Displays the available commands and their syntax. */
    HELP("help", false),
    /** Represents input that does not match a supported command. */
    UNKNOWN("", false);

    private final String keyword;
    private final boolean acceptsArguments;

    /**
     * Creates a command type with its user-facing keyword.
     *
     * @param keyword Word used to invoke the command.
     * @param acceptsArguments Whether text may follow the command keyword.
     */
    CommandType(String keyword, boolean acceptsArguments) {
        this.keyword = keyword;
        this.acceptsArguments = acceptsArguments;
    }

    /**
     * Returns the word used to invoke this command.
     *
     * @return Command keyword.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Identifies the type of a complete command entered by the user.
     *
     * @param command Trimmed command text.
     * @return Matching command type, or {@link #UNKNOWN} if none matches.
     */
    public static CommandType from(String command) {
        for (CommandType type : values()) {
            boolean hasAcceptedArguments = type.acceptsArguments
                    && command.startsWith(type.keyword + " ");
            if (command.equals(type.keyword) || hasAcceptedArguments) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
