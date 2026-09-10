package groot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import groot.exception.GrootException;

/**
 * Tests argument parsing performed by {@link Parser}.
 */
public class ParserTest {
    private final Parser parser = new Parser();

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
