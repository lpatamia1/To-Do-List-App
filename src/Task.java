import java.time.LocalDate;

public class Task implements Comparable<Task> {
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

    @Override
    public int compareTo(Task other) {
        return Integer.compare(priorityValue(other.priority), priorityValue(this.priority));
    }

    private int priorityValue(String p) {
        switch (p) {
            case "HIGH": return 3;
            case "MED": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }
}
