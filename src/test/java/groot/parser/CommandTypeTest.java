package groot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests command recognition performed by {@link CommandType#from(String)}.
 */
public class CommandTypeTest {

    @Test
    public void from_exactKeyword_returnsMatchingCommandType() {
        assertEquals(CommandType.BYE, CommandType.from("bye"));
        assertEquals(CommandType.LIST, CommandType.from("list"));
        assertEquals(CommandType.MARK, CommandType.from("mark"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark"));
        assertEquals(CommandType.DELETE, CommandType.from("delete"));
        assertEquals(CommandType.TODO, CommandType.from("todo"));
        assertEquals(CommandType.DEADLINE, CommandType.from("deadline"));
        assertEquals(CommandType.EVENT, CommandType.from("event"));
    }

    @Test
    public void from_commandThatAcceptsArguments_returnsMatchingCommandType() {
        assertEquals(CommandType.MARK, CommandType.from("mark 1"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark 1"));
        assertEquals(CommandType.DELETE, CommandType.from("delete 1"));
        assertEquals(CommandType.TODO, CommandType.from("todo read book"));
        assertEquals(CommandType.DEADLINE,
                CommandType.from("deadline return book /by 2019-12-02"));
        assertEquals(CommandType.EVENT,
                CommandType.from("event meeting /from 2pm /to 3pm"));
    }

    @Test
    public void from_argumentlessCommandWithArguments_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("bye now"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("list extra"));
    }

    @Test
    public void from_keywordOnlyAsPrefix_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("todoist"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("marked 1"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("events meeting"));
    }

    @Test
    public void from_emptyUnknownOrWrongCaseCommand_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from(""));
        assertEquals(CommandType.UNKNOWN, CommandType.from("hello"));
        assertEquals(CommandType.UNKNOWN, CommandType.from("LIST"));
    }
}
