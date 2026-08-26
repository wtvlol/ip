package groot.parser;

/**
 * Represents a command that Groot can process.
 */
public enum CommandType {
    BYE("bye", false),
    LIST("list", false),
    MARK("mark", true),
    UNMARK("unmark", true),
    DELETE("delete", true),
    TODO("todo", true),
    DEADLINE("deadline", true),
    EVENT("event", true),
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
