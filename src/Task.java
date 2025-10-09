import java.time.LocalDate;

public class Task implements Comparable<Task> { // Represents a to-do task with name, priority, due date, and completion status
// Implements Comparable for sorting by priority
    private String name;
    private String priority;
    private LocalDate due;
    private boolean completed;

    public Task(String name, String priority, LocalDate due) {
        this.name = name;
        this.priority = (priority == null || priority.isEmpty()) ? "NONE" : priority;
        this.due = due;
        this.completed = false;
    }

    public String getName() { return name; }
    public String getPriority() { return priority; }
    public LocalDate getDue() { return due; }
    public boolean isCompleted() { return completed; }

    public void markCompleted() { this.completed = true; }

    @Override // Compare tasks by priority for sorting
    public int compareTo(Task other) {
        return Integer.compare(priorityValue(other.priority), priorityValue(this.priority));
    }

    private int priorityValue(String p) { // Convert priority string to numeric value for comparison
    // Higher number = higher priority
    // HIGH=3, MED=2, LOW=1, NONE=0
    // Default to 0 for unknown priorities
        switch (p) {
            case "HIGH": return 3;
            case "MED": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }
}
