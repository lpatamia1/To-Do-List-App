import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class todo {
    private static final String FILE_NAME = "tasks.txt";
    private static final List<String> tasks = new ArrayList<>();
    private static final SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int addedToday = 0;
    private static int completedToday = 0;

    public static void main(String[] args) {
        retroBoot();
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        ColorText.banner("✨ Retro To-Do List ✨");
        showQuote();

        while (true) {
            ColorText.line();
            System.out.println("1️⃣ Add Task\n2️⃣ View Tasks\n3️⃣ Mark Complete\n4️⃣ Export Markdown\n5️⃣ Exit");
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
                    exportMarkdown();
                    break;
                case "5":
                    exitApp();
                    scanner.close();
                    return;
                default:
                    beep();
                    ColorText.warn("❓ Invalid option. Try again!");
            }
        }
    }

    // --- Core Features ---

    private static void addTask(Scanner scanner) {
        System.out.print(ColorText.YELLOW + "Enter task: " + ColorText.RESET);
        String task = scanner.nextLine().trim();
        if (!task.isEmpty()) {
            String entry = task + " (added " + timestamp.format(new Date()) + ")";
            tasks.add(entry);
            addedToday++;
            saveTasks();
            beep();
            ColorText.success("Task added!");
        } else {
            beep();
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
                completedToday++;
                String doneMsg = completed + " ✅ (completed " + timestamp.format(new Date()) + ")";
                beep();
                ColorText.success(doneMsg);
                saveTasks();
            } else {
                beep();
                ColorText.warn("Invalid task number!");
            }
        } catch (NumberFormatException e) {
            beep();
            ColorText.warn("Please enter a valid number.");
        }
    }

    // --- Data Handling ---

    private static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) writer.println(task);
        } catch (IOException e) {
            beep();
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

    private static void exportMarkdown() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.md"))) {
            writer.println("# 📝 Retro To-Do List");
            writer.println("_Updated: " + timestamp.format(new Date()) + "_\n");
            if (tasks.isEmpty()) {
                writer.println("✅ All tasks completed! 🎉");
            } else {
                writer.println("## Pending Tasks:\n");
                for (String task : tasks) {
                    writer.println("- [ ] " + task);
                }
            }
            ColorText.success("🗒️ Exported to tasks.md");
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not export to Markdown.");
        }
    }

    private static void backupTasks() {
        File file = new File(FILE_NAME);
        File backup = new File("tasks_backup_" + System.currentTimeMillis() + ".txt");
        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(backup)) {
            in.transferTo(out);
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not create backup file.");
        }
    }

    // --- Aesthetic Touches ---

    private static void retroBoot() {
        String[] loading = {"█▒▒▒▒▒▒▒▒▒ 10%", "███▒▒▒▒▒▒▒ 30%", "██████▒▒▒▒ 60%", "██████████ 100%"};
        for (String bar : loading) {
            System.out.print("\r" + ColorText.CYAN + "Loading... " + bar + ColorText.RESET);
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        System.out.println("\n" + ColorText.GREEN + "✨ Ready to roll! ✨" + ColorText.RESET);
    }

    private static void showQuote() {
        String[] quotes = {
            "Keep calm and code on 💻",
            "Progress, not perfection 🌈",
            "Retro vibes only ✨",
            "Every bug teaches you something 🐛",
            "One task at a time 🪩"
        };
        Random rand = new Random();
        System.out.println(ColorText.CYAN + "💬 " + quotes[rand.nextInt(quotes.length)] + ColorText.RESET);
    }

    private static void exitApp() {
        beep();
        backupTasks();
        ColorText.line();
        ColorText.success("💾 Exiting… your tasks are saved!");
        ColorText.info("📅 Today’s Stats: Added " + addedToday + " | Completed " + completedToday);
        exportMarkdown();
    }

    private static void beep() {
        System.out.print("\007");
        System.out.flush();
    }
}
