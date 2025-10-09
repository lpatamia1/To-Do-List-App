import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskSortingTest {
    @Test
    void testSortingByDueDateThenPriority() {
        Task t1 = new Task("Low later", "LOW", LocalDate.now().plusDays(5));
        Task t2 = new Task("High sooner", "HIGH", LocalDate.now().plusDays(1));
        Task t3 = new Task("Med today", "MED", LocalDate.now());

        List<Task> list = new ArrayList<>(List.of(t1, t2, t3));
        Collections.sort(list);

        assertEquals("Med today", list.get(0).getName());
        assertEquals("High sooner", list.get(1).getName());
    }
}
