package groot.parser;

import java.util.List;

/**
 * Represents a command that Groot can process.
 */
public enum CommandType {
    /** Ends the current Groot session. */
    BYE(false, "bye"),
    /** Displays all tasks. */
    LIST(false, "list"),
    /** Displays tasks whose descriptions contain a keyword. */
    FIND(true, "find"),
    /** Marks a selected task as completed. */
    MARK(true, "mark"),
    /** Marks a selected task as incomplete. */
    UNMARK(true, "unmark"),
    /** Removes a selected task. */
    DELETE(true, "delete"),
    /** Adds a task without a date or time. */
    TODO(true, "todo"),
    /** Adds a task with a completion date. */
    DEADLINE(true, "deadline"),
    /** Adds a task with a start and end. */
    EVENT(true, "event"),
    /** Displays the available commands and their syntax. */
    HELP(false, "help", "--help", "-h"),
    /** Represents input that does not match a supported command. */
    UNKNOWN(false, "");

    private final boolean acceptsArguments;
    private final List<String> keywords;

    /**
     * Creates a command type with a canonical keyword and optional aliases.
     *
     * @param acceptsArguments Whether text may follow the command keyword.
     * @param keywords Canonical keyword followed by any aliases.
     */
    CommandType(boolean acceptsArguments, String... keywords) {
        this.acceptsArguments = acceptsArguments;
        this.keywords = List.of(keywords);
    }

    /**
     * Returns the word used to invoke this command.
     *
     * @return Command keyword.
     */
    public String getKeyword() {
        return keywords.get(0);
    }

    /**
     * Identifies the type of a complete command entered by the user.
     *
     * @param command Trimmed command text.
     * @return Matching command type, or {@link #UNKNOWN} if none matches.
     */
    public static CommandType from(String command) {
        for (CommandType type : values()) {
            if (type.matches(command)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Returns whether the command uses this type's canonical keyword or an alias.
     */
    private boolean matches(String command) {
        for (String keyword : keywords) {
            boolean hasAcceptedArguments = acceptsArguments
                    && command.startsWith(keyword + " ");
            if (command.equals(keyword) || hasAcceptedArguments) {
                return true;
            }
        }
        return false;
    }
}
