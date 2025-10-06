import java.io.*;
import java.util.*;

public class RetroToDo {
    private static final String FILE_NAME = "tasks.txt";
    private static final List<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        System.out.println("✨ Retro To-Do List ✨");

        while (true) {
            System.out.println("\n1️⃣ Add Task\n2️⃣ View Tasks\n3️⃣ Mark Complete\n4️⃣ Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter task: ");
                    tasks.add(scanner.nextLine());
                    saveTasks();
                    break;
                case "2":
                    showTasks();
                    break;
                case "3":
                    showTasks();
                    System.out.print("Enter number to complete: ");
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        String completed = tasks.remove(index);
                        System.out.println("✅ Done: " + completed);
                        saveTasks();
                    }
                    break;
                case "4":
                    System.out.println("💾 Exiting… your tasks are saved!");
                    scanner.close();
                    return;
                default:
                    System.out.println("❓ Invalid option.");
            }
        }
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("🌈 No tasks yet!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) writer.println(task);
        } catch (IOException e) {
            System.out.println("❌ Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) tasks.add(fileScanner.nextLine());
            System.out.println("📂 Loaded " + tasks.size() + " saved task(s).");
        } catch (IOException e) {
            System.out.println("⚠️ Could not load previous tasks.");
        }
    }
}
