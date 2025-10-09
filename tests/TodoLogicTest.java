import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;

public class TodoLogicTest { // Unit tests for core logic functions in TodoLogic
// Tests counting active, due soon, and overdue tasks
// Uses JUnit 5 for assertions and test structure

    @Test
    void testCountActiveExcludesCompleted() {
        Task t1 = new Task("Do laundry", "LOW", null);
        Task t2 = new Task("Submit project", "HIGH", LocalDate.now());
        t2.markCompleted();
        long count = TodoLogic.countActive(List.of(t1, t2));
        Assertions.assertEquals(1, count, "Only 1 task should be active");
    }

    @Test
    void testCountDueSoonIncludesTodayAndNext3Days() {
        List<Task> tasks = List.of(
            new Task("Today", "HIGH", LocalDate.now()),
            new Task("Tomorrow", "MED", LocalDate.now().plusDays(1)),
            new Task("In 3 days", "LOW", LocalDate.now().plusDays(3)),
            new Task("In 5 days", "LOW", LocalDate.now().plusDays(5)) // too far
        );
        long count = TodoLogic.countDueSoon(tasks);
        Assertions.assertEquals(3, count, "3 tasks should be due within 3 days");
    }

    @Test
    void testCountDueSoonSkipsCompletedTasks() {
        Task t1 = new Task("Due soon", "MED", LocalDate.now().plusDays(1));
        Task t2 = new Task("Done task", "LOW", LocalDate.now().plusDays(2));
        t2.markCompleted();
        long count = TodoLogic.countDueSoon(List.of(t1, t2));
        Assertions.assertEquals(1, count, "Completed tasks shouldn't count");
    }

    @Test
    void testCountOverdueOnlyPastDueTasks() {
        List<Task> tasks = List.of(
            new Task("Past task", "HIGH", LocalDate.now().minusDays(1)),
            new Task("Future task", "LOW", LocalDate.now().plusDays(1)),
            new Task("No date", "MED", null)
        );
        long count = TodoLogic.countOverdue(tasks);
        Assertions.assertEquals(1, count, "Only one task should be overdue");
    }

    @Test
    void testCountOverdueSkipsCompleted() {
        Task t1 = new Task("Overdue but done", "HIGH", LocalDate.now().minusDays(2));
        t1.markCompleted();
        long count = TodoLogic.countOverdue(List.of(t1));
        Assertions.assertEquals(0, count, "Completed tasks shouldn't count as overdue");
    }
}
