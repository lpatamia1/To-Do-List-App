import java.io.*;
import java.util.*;

public class todo {
    private static final String FILE_NAME = "tasks.txt";
    private static final List<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        ColorText.banner("Retro To-Do List");

        while (true) {
            ColorText.line();
            System.out.println("1️⃣ Add Task\n2️⃣ View Tasks\n3️⃣ Mark Complete\n4️⃣ Exit");
            System.out.print(ColorText.CYAN + "Choose: " + ColorText.RESET);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(scanner);
                    break;
                case "2":
                    showTasks();
                    break;
                case "3":
                    completeTask(scanner);
                    break;
                case "4":
                    ColorText.success("💾 Exiting… your tasks are saved!");
                    scanner.close();
                    return;
                default:
                    ColorText.warn("❓ Invalid option. Try again!");
            }
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.print(ColorText.YELLOW + "Enter task: " + ColorText.RESET);
        String task = scanner.nextLine().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            saveTasks();
            ColorText.success("Task added!");
        } else {
            ColorText.warn("You can’t add an empty task!");
        }
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            ColorText.warn("🌈 No tasks yet!");
        } else {
            ColorText.info("\n📝 Your Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(ColorText.BLUE + (i + 1) + ". " + tasks.get(i) + ColorText.RESET);
            }
        }
    }

    private static void completeTask(Scanner scanner) {
        showTasks();
        if (tasks.isEmpty()) return;

        System.out.print(ColorText.YELLOW + "Enter number to complete: " + ColorText.RESET);
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < tasks.size()) {
                String completed = tasks.remove(index);
                ColorText.success("✅ Done: " + completed);
                saveTasks();
            } else {
                ColorText.warn("Invalid task number!");
            }
        } catch (NumberFormatException e) {
            ColorText.warn("Please enter a valid number.");
        }
    }

    private static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) writer.println(task);
        } catch (IOException e) {
            ColorText.warn("Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                tasks.add(fileScanner.nextLine());
            }
            ColorText.info("📂 Loaded " + tasks.size() + " saved task(s).");
        } catch (IOException e) {
            ColorText.warn("Could not load previous tasks.");
        }
    }
}
