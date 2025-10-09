import java.time.LocalDate;
import java.util.List;

public class TodoLogic {

    /** Counts active tasks that are not completed */
    public static long countActive(List<Task> tasks) {
        return tasks.stream().filter(t -> !t.isCompleted()).count();
    }

    /** Counts tasks due within 3 days (including today) */
    public static long countDueSoon(List<Task> tasks) {
        return tasks.stream()
            .filter(t -> !t.isCompleted()
                    && t.getDue() != null
                    && !LocalDate.now().isAfter(t.getDue())
                    && !t.getDue().isAfter(LocalDate.now().plusDays(3)))
            .count();
    }

    /** Counts overdue tasks */
    public static long countOverdue(List<Task> tasks) {
        return tasks.stream()
            .filter(t -> !t.isCompleted()
                    && t.getDue() != null
                    && LocalDate.now().isAfter(t.getDue()))
            .count();
    }
}
