package groot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import groot.exception.GrootException;
import groot.task.Deadline;
import groot.task.Event;
import groot.task.Task;
import groot.task.Todo;

/**
 * Tests argument parsing performed by {@link Parser}.
 */
public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    public void createTask_todoWithSpaces_returnsTrimmedTodo() throws GrootException {
        Task task = parser.createTask("todo   read book  ", CommandType.TODO);
        assertInstanceOf(Todo.class, task);
        assertEquals("T | 0 | read book", task.toDataString());
    }

    @Test
    public void createTask_leapDayDeadline_returnsDeadline() throws GrootException {
        Task task = parser.createTask("deadline  report  /by  2024-02-29  ", CommandType.DEADLINE);
        assertInstanceOf(Deadline.class, task);
        assertEquals("D | 0 | report | 2024-02-29", task.toDataString());
    }

    @Test
    public void createTask_eventWithSpaces_returnsTrimmedEvent() throws GrootException {
        Task task = parser.createTask("event  meeting  /from  Mon 2pm  /to  4pm  ", CommandType.EVENT);
        assertInstanceOf(Event.class, task);
        assertEquals("E | 0 | meeting | Mon 2pm | 4pm", task.toDataString());
    }

    @Test
    public void createTask_missingTodoDescription_preservesErrorMessage() {
        assertTaskError("todo   ", CommandType.TODO, "Oops! A todo needs a description.");
    }

    @Test
    public void createTask_missingDeadlineFields_preservesErrorMessages() {
        assertTaskError("deadline report", CommandType.DEADLINE,
                "Oops! Use: deadline DESCRIPTION /by DATE");
        assertTaskError("deadline /by 2024-02-29", CommandType.DEADLINE,
                "Oops! A deadline needs a description.");
        assertTaskError("deadline report /by", CommandType.DEADLINE,
                "Oops! A deadline needs a date after /by.");
    }

    @Test
    public void createTask_invalidDeadlineDates_preservesErrorMessage() {
        for (String date : new String[]{"Sunday", "2023-02-29", "2024-13-01", "2024-01-01 /by 2024-02-01"}) {
            assertTaskError("deadline report /by " + date, CommandType.DEADLINE,
                    "Oops! Use deadline dates in yyyy-MM-dd format, e.g. 2019-10-15.");
        }
    }

    @Test
    public void createTask_missingOrReversedEventMarkers_preservesErrorMessage() {
        String[] commands = {"event meeting", "event meeting /from Mon", "event meeting /to Tue /from Mon"};
        for (String command : commands) {
            assertTaskError(command, CommandType.EVENT,
                    "Oops! Use: event DESCRIPTION /from START /to END");
        }
    }

    @Test
    public void createTask_emptyEventFields_preservesErrorMessages() {
        assertTaskError("event /from Mon /to Tue", CommandType.EVENT,
                "Oops! An event needs a description.");
        assertTaskError("event meeting /from /to Tue", CommandType.EVENT,
                "Oops! An event needs a start date or time after /from.");
        assertTaskError("event meeting /from Mon /to", CommandType.EVENT,
                "Oops! An event needs an end date or time after /to.");
    }

    @Test
    public void createTask_unsupportedType_throwsIllegalArgumentException() {
        for (CommandType type : new CommandType[]{CommandType.LIST, CommandType.UNKNOWN}) {
            assertThrows(IllegalArgumentException.class, () -> parser.createTask("", type));
        }
    }

    /**
     * Verifies the exact user-facing error to guard validation behavior during refactoring.
     */
    private void assertTaskError(String command, CommandType type, String expectedMessage) {
        GrootException error = assertThrows(GrootException.class, () -> parser.createTask(command, type));
        assertEquals(expectedMessage, error.getMessage());
    }

    @Test
    public void parseTaskIndex_negativeTaskCount_assertionThrown() {
        AssertionError error = assertThrows(
                AssertionError.class, () -> parser.parseTaskIndex("mark 1", CommandType.MARK, -1));

        assertEquals("Task count must not be negative", error.getMessage());
    }

    @Test
    public void parseTaskIndex_validBoundaries_returnsZeroBasedIndex() throws GrootException {
        assertEquals(0, parser.parseTaskIndex("mark 1", CommandType.MARK, 3));
        assertEquals(2, parser.parseTaskIndex("unmark 3", CommandType.UNMARK, 3));
        assertEquals(0, parser.parseTaskIndex("delete 1", CommandType.DELETE, 1));
    }

    @Test
    public void parseTaskIndex_invalidUserNumbers_throwsGrootException() {
        for (String number : new String[]{"", "two", "0", "-1", "4", "2147483648"}) {
            assertThrows(
                    GrootException.class, () -> parser.parseTaskIndex("mark " + number, CommandType.MARK, 3));
        }
        assertThrows(
                GrootException.class, () -> parser.parseTaskIndex("delete 1", CommandType.DELETE, 0));
    }

    @Test
    public void parseCommandType_supportedCommands_returnsRecognizedTypes() throws GrootException {
        assertEquals(CommandType.TODO, parser.parseCommandType("todo read book"));
        assertEquals(CommandType.HELP, parser.parseCommandType("--help"));
        assertEquals(CommandType.BYE, parser.parseCommandType("bye"));
    }

    @Test
    public void parseCommandType_unknownOrEmptyInput_throwsGrootException() {
        assertThrows(GrootException.class, () -> parser.parseCommandType("unknown"));
        assertThrows(GrootException.class, () -> parser.parseCommandType(""));
    }

    /**
     * Verifies that surrounding spaces are excluded from the find keyword.
     *
     * @throws GrootException If the valid command is unexpectedly rejected.
     */
    @Test
    public void parseFindKeyword_keywordPresent_returnsTrimmedKeyword() throws GrootException {
        assertEquals("project book", parser.parseFindKeyword("find   project book"));
    }

    /**
     * Verifies that a find command without a keyword is rejected clearly.
     */
    @Test
    public void parseFindKeyword_keywordMissing_exceptionThrown() {
        GrootException error = assertThrows(
                GrootException.class, () -> {
                    parser.parseFindKeyword("find");
                });

        assertEquals("Oops! Tell me what to find.", error.getMessage());
    }
}
