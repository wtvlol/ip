package groot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command dispatch in a separate process with isolated storage and assertions enabled.
 */
public class GrootTest {
    @TempDir
    public Path temporaryDirectory;

    @Test
    public void main_invalidThenValidCommands_reportsErrorsAndContinues() throws Exception {
        String javaExecutable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classPath = Path.of(Groot.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .toString();
        Process process = new ProcessBuilder(javaExecutable, "-ea", "-cp", classPath, "groot.Groot")
                .directory(temporaryDirectory.toFile())
                .redirectErrorStream(true)
                .start();
        try {
            try (var input = process.getOutputStream()) {
                input.write("unknown\n\nhelp\nbye\n".getBytes(StandardCharsets.UTF_8));
            }
            assertTrue(process.waitFor(10, TimeUnit.SECONDS), "Groot must exit after bye");
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals(0, process.exitValue(), output);
            assertTrue(output.contains("Oops! I don't recognise that command."), output);
            assertTrue(output.contains("Oops! Please enter a command."), output);
            assertTrue(output.contains("Here are the commands you can use:"), output);
            assertTrue(output.contains("Bye. Hope to see you again soon!"), output);
            assertFalse(output.contains("AssertionError"), output);
        } finally {
            process.destroyForcibly();
        }
    }
}
