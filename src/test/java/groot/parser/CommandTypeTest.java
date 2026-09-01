package groot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests command recognition performed by {@link CommandType#from(String)}.
 */
public class CommandTypeTest {

    /**
     * Verifies that every exact command keyword maps to its corresponding command type.
     */
    @Test
    public void from_exactKeyword_returnsMatchingCommandType() {
        assertEquals(CommandType.BYE, CommandType.from("bye"));
        assertEquals(CommandType.LIST, CommandType.from("list"));
        assertEquals(CommandType.FIND, CommandType.from("find"));
        assertEquals(CommandType.MARK, CommandType.from("mark"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark"));
        assertEquals(CommandType.DELETE, CommandType.from("delete"));
        assertEquals(CommandType.TODO, CommandType.from("todo"));
        assertEquals(CommandType.DEADLINE, CommandType.from("deadline"));
        assertEquals(CommandType.EVENT, CommandType.from("event"));
        assertEquals(CommandType.HELP, CommandType.from("help"));
    }

    /**
     * Verifies that commands designed to accept arguments are recognized with arguments present.
     */
    @Test
    public void from_commandThatAcceptsArguments_returnsMatchingCommandType() {
        assertEquals(CommandType.MARK, CommandType.from("mark 1"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark 1"));
        assertEquals(CommandType.DELETE, CommandType.from("delete 1"));
        assertEquals(CommandType.FIND, CommandType.from("find book"));
        assertEquals(CommandType.TODO, CommandType.from("todo read book"));
        assertEquals(CommandType.DEADLINE,
                CommandType.from("deadline return book /by 2019-12-02"));
        assertEquals(CommandType.EVENT,
                CommandType.from("event meeting /from 2pm /to 3pm"));
    }

    /**
     * Verifies that extra spaces between a command keyword and its arguments remain valid.
     */
    @Test
    public void from_multipleSpacesBeforeArguments_returnsMatchingCommandType() {
        assertEquals(CommandType.MARK, CommandType.from("mark   1"));
        assertEquals(CommandType.FIND, CommandType.from("find   book"));
        assertEquals(CommandType.TODO, CommandType.from("todo   read book"));
        assertEquals(CommandType.DEADLINE,
                CommandType.from("deadline   return book /by 2019-12-02"));
    }

    /**
     * Verifies that commands defined without arguments reject trailing argument text.
     */
    @Test
    public void from_argumentlessCommandWithArguments_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("bye now"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("list extra"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("help extra"));
    }

    /**
     * Verifies that a keyword embedded at the start of a longer word is not accepted.
     */
    @Test
    public void from_keywordOnlyAsPrefix_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("todoist"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("marked 1"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("events meeting"));
    }

    /**
     * Verifies that keywords must start the command and use a space before any arguments.
     */
    @Test
    public void from_keywordNotAtStartOrWithoutSpaceSeparator_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("please list"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("todo\tread book"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("mark\t1"));
    }

    /**
     * Verifies that empty, unrecognized, and incorrectly cased commands map to unknown.
     */
    @Test
    public void from_emptyUnknownOrWrongCaseCommand_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from(""));
        assertEquals(CommandType.UNKNOWN, CommandType.from("hello"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("LIST"));
    }
}
