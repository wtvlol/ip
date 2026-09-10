package groot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command responses and failed-save recovery with isolated storage and assertions enabled.
 */
public class GrootTest {
    @TempDir
    public Path temporaryDirectory;

    @Test
    public void main_invalidThenValidCommands_reportsErrorsAndContinues() throws Exception {
        String output = runProcess(Groot.class, "unknown\n\nhelp\nbye\n");
        assertTrue(output.contains("Oops! I don't recognise that command."), output);
        assertTrue(output.contains("Oops! Please enter a command."), output);
        assertTrue(output.contains("Here are the commands you can use:"), output);
        assertTrue(output.contains("Bye. Hope to see you again soon!"), output);
        assertFalse(output.contains("AssertionError"), output);
    }

    @Test
    public void getResponse_repeatedStatusCommands_preservesResponses() throws Exception {
        String input = "todo first\nmark 1\nmark 1\nlist\nunmark 1\nunmark 1\nlist\ndelete 1\nbye\n";
        String output = runProcess(Groot.class, input);
        assertTrue(output.contains(" Nice! I've marked this task as done:\n   [T][X] first"), output);
        assertTrue(output.contains(" Here are the tasks in your list:\n 1.[T][X] first"), output);
        assertTrue(output.contains(" OK, I've marked this task as not done yet:\n   [T][ ] first"), output);
        assertTrue(output.contains(" Here are the tasks in your list:\n 1.[T][ ] first"), output);
        assertTrue(output.contains(" Now you have 0 tasks in the list."), output);
    }

    @Test
    public void getResponse_failedMark_restoresIncompleteStatus() throws Exception {
        assertFailedSavePreservesTasks(false, "mark 2");
    }

    @Test
    public void getResponse_failedUnmark_restoresCompletedStatus() throws Exception {
        assertFailedSavePreservesTasks(true, "unmark 2");
    }

    @Test
    public void getResponse_failedRepeatedMark_preservesCompletedStatus() throws Exception {
        assertFailedSavePreservesTasks(true, "mark 2");
    }

    @Test
    public void getResponse_failedRepeatedUnmark_preservesIncompleteStatus() throws Exception {
        assertFailedSavePreservesTasks(false, "unmark 2");
    }

    @Test
    public void getResponse_failedDelete_restoresPositionAndStatus() throws Exception {
        assertFailedSavePreservesTasks(true, "delete 2");
    }

    @Test
    public void getResponse_failedAdd_removesOnlyNewTask() throws Exception {
        assertFailedSavePreservesTasks(true, "todo new task");
    }

    /**
     * Compares the list before failure, after rollback, and after reloading the original file.
     */
    private void assertFailedSavePreservesTasks(boolean isDone, String command) throws Exception {
        String output = runProcess(SaveFailureScenario.class, "", Boolean.toString(isDone), command);
        String expectedList = " Here are the tasks in your list:\n 1.[T][ ] first\n 2.[T]["
                + (isDone ? "X" : " ") + "] second\n 3.[T][ ] third";
        String expectedError = " Oops! I couldn't save your tasks. Your last change was not applied.";
        assertEquals(String.join("\n", expectedList, expectedError, expectedList, expectedList) + "\n", output);
    }

    /**
     * Runs a scenario with its own working directory so tests never touch the user's saved tasks.
     */
    private String runProcess(Class<?> entryPoint, String input, String... arguments) throws Exception {
        String javaExecutable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String productionClasses = Path.of(Groot.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .toString();
        String testClasses = Path.of(GrootTest.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .toString();
        String classPath = productionClasses + File.pathSeparator + testClasses;
        List<String> command = new ArrayList<>(List.of(javaExecutable, "-ea", "-cp", classPath, entryPoint.getName()));
        command.addAll(List.of(arguments));
        Process process = new ProcessBuilder(command).directory(temporaryDirectory.toFile())
                .redirectErrorStream(true).start();
        try {
            try (var processInput = process.getOutputStream()) {
                processInput.write(input.getBytes(StandardCharsets.UTF_8));
            }
            assertTrue(process.waitFor(10, TimeUnit.SECONDS), "The scenario must finish within ten seconds");
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                    .replace("\r\n", "\n");
            assertEquals(0, process.exitValue(), output);
            return output;
        } finally {
            process.destroyForcibly();
        }
    }

    /**
     * Makes the save destination a directory to trigger a deterministic write failure in a child process.
     */
    public static class SaveFailureScenario {
        /**
         * Prints task state around a failed command and reloads the preserved data file.
         *
         * @param args Initial status of the second task, followed by the command to fail.
         * @throws Exception If scenario setup or restoration fails.
         */
        public static void main(String[] args) throws Exception {
            Groot groot = new Groot();
            groot.getResponse("todo first");
            groot.getResponse("todo second");
            groot.getResponse("todo third");
            if (Boolean.parseBoolean(args[0])) {
                groot.getResponse("mark 2");
            }
            System.out.println(groot.getResponse("list"));

            Path dataFile = Path.of("data", "groot.txt");
            Path backupFile = Path.of("data", "saved.txt");
            Files.move(dataFile, backupFile);
            Files.createDirectory(dataFile);
            System.out.println(groot.getResponse(args[1]));
            System.out.println(groot.getResponse("list"));

            Files.delete(dataFile);
            Files.move(backupFile, dataFile);
            System.out.println(new Groot().getResponse("list"));
        }
    }
}
