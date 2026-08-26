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
