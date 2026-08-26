package groot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task-collection operations performed by {@link TaskList}.
 */
public class TaskListTest {

    /**
     * Verifies that find matches descriptions without regard to case and preserves list order.
     */
    @Test
    public void find_mixedCaseKeyword_returnsMatchingTasksInOrder() {
        Task firstMatch = new Todo("Read Book");
        Task nonMatch = new Todo("buy groceries");
        Task secondMatch = new Deadline("return book", LocalDate.of(2026, 8, 31));
        TaskList tasks = new TaskList(List.of(firstMatch, nonMatch, secondMatch));

        assertEquals(List.of(firstMatch, secondMatch), tasks.find("BOOK"));
    }

    /**
     * Verifies that find supports phrases within descriptions.
     */
    @Test
    public void find_phraseKeyword_returnsMatchingTask() {
        Task matchingTask = new Event("project team meeting", "2pm", "3pm");
        TaskList tasks = new TaskList(List.of(
                matchingTask,
                new Todo("project report"),
                new Todo("team lunch")));

        assertEquals(List.of(matchingTask), tasks.find("team meeting"));
    }

    /**
     * Verifies that find does not search deadline or event metadata.
     */
    @Test
    public void find_keywordOnlyInMetadata_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(
                new Deadline("submit report", LocalDate.of(2026, 8, 31)),
                new Event("project meeting", "Monday", "Tuesday")));

        assertEquals(List.of(), tasks.find("2026"));
        assertEquals(List.of(), tasks.find("Monday"));
    }

    /**
     * Verifies that find returns no matches for an empty task list.
     */
    @Test
    public void find_emptyTaskList_returnsEmptyList() {
        assertEquals(List.of(), new TaskList().find("book"));
    }
}
