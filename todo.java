import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;

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
            System.out.println("1️⃣  Add Task\n2️⃣  View Tasks\n3️⃣  Mark Complete\n4️⃣  Show Upcoming\n5️⃣  Export Markdown\n6️⃣  Exit");
            System.out.print(ColorText.CYAN + "Choose: " + ColorText.RESET);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addTask(scanner); break;
                case "2": showTasks(); break;
                case "3": completeTask(scanner); break;
                case "4": showUpcoming(); break;
                case "5": exportMarkdown(); break;
                case "6": exitApp(); scanner.close(); return;
                case "?": ColorText.info("Commands: 1-Add | 2-View | 3-Complete | 4-Upcoming | 5-Export | 6-Exit | ?-Help"); break;
                default: beep(); ColorText.warn("❓ Invalid option. Try again!");
            }
        }
    }

    // --- Core Features ---

    private static void addTask(Scanner scanner) {
        System.out.print(ColorText.YELLOW + "Enter task: " + ColorText.RESET);
        String task = scanner.nextLine().trim();
        System.out.print(ColorText.YELLOW + "Set priority (H/M/L or blank): " + ColorText.RESET);
        String priority = scanner.nextLine().trim().toUpperCase();

        System.out.print(ColorText.YELLOW + "Enter due date (YYYY-MM-DD or blank): " + ColorText.RESET);
        String dueInput = scanner.nextLine().trim();

        LocalDate dueDate = null;
        if (!dueInput.isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueInput);
            } catch (Exception e) {
                ColorText.warn("⚠️ Invalid date format. Skipping due date.");
            }
        }

        String formattedPriority = "";
        switch (priority) {
            case "H":
                formattedPriority = ColorText.RED + "[HIGH]" + ColorText.RESET;
                break;
            case "M":
                formattedPriority = ColorText.YELLOW + "[MED]" + ColorText.RESET;
                break;
            case "L":
                formattedPriority = ColorText.GREEN + "[LOW]" + ColorText.RESET;
                break;
            default:
                formattedPriority = "";
                break;
        }

        if (!task.isEmpty()) {
            if (formattedPriority.isEmpty()) formattedPriority = "[NONE]";
            String duelabel = (dueDate != null) ? " | Due: " + dueDate.toString() : "";
            String entry = task + " " + formattedPriority + duelabel + " (added " + timestamp.format(new Date()) + ")";

            tasks.add(entry);
            addedToday++;
            saveTasks();
            sortTasksByPriority();
            beep();
            ColorText.success("Task added!");
            pauseAndClear(scanner);
        } else {
            beep();
            ColorText.warn("You can’t add an empty task!");
        }
        backupTasks();
    }

    private static void showTasks() {
        sortTasksByPriority();
        if (tasks.isEmpty()) {
            ColorText.warn("🌈 No tasks yet!");
            pauseAndClear(new Scanner(System.in));
            return;
        }

        // --- Table Header ---
        ColorText.info("\n📝 Your Tasks:");
        System.out.printf(ColorText.CYAN + "%-3s %-28s %-14s %-12s%n" + ColorText.RESET,
                "#", "Task", "Priority", "Due Date");
        System.out.println(ColorText.CYAN + "───────────────────────────────────────────────────────────────────────" + ColorText.RESET);

        // --- Table Rows ---
        for (int i = 0; i < tasks.size(); i++) {
            String raw = tasks.get(i);
            String priority = "—";
            String due = "—";

            // Extract priority
            if (raw.contains("[HIGH]")) priority = "[HIGH]";
            else if (raw.contains("[MED]")) priority = "[MED]";
            else if (raw.contains("[LOW]")) priority = "[LOW]";
            else priority = "[NONE]";

            // Extract due date
            if (raw.contains("Due: ")) {
                int start = raw.indexOf("Due: ") + 5;
                int end = raw.indexOf(" ", start);
                if (end < 0) end = raw.length();
                due = raw.substring(start, end).trim();
                if (due.isEmpty()) due = "[NO DATE]";
            } else {
                due = "[NO DATE]";
            }

            // Clean task name
            String taskName = raw
                    .replaceAll("\\[HIGH\\]|\\[MED\\]|\\[LOW\\]", "")
                    .replaceAll("\\| Due:.*", "")
                    .replaceAll("\\(added.*\\)", "")
                    .trim();

            // Add colors AFTER formatting
            String coloredPriority;
            switch (priority) {
                case "[HIGH]":
                    coloredPriority = ColorText.RED + priority + ColorText.RESET;
                    break;
                case "[MED]":
                    coloredPriority = ColorText.YELLOW + priority + ColorText.RESET;
                    break;
                case "[LOW]":
                    coloredPriority = ColorText.GREEN + priority + ColorText.RESET;
                    break;
                case "[NONE]":
                    coloredPriority = ColorText.GRAY + priority + ColorText.RESET;
                    break;
                default:
                    coloredPriority = priority;
                    break;
            }


        // --- Print each task row with spacing ---
        System.out.printf(ColorText.BLUE + "%-3d %-37s %-23s %-12s" + ColorText.RESET + "%n",
                (i + 1), taskName, coloredPriority, due);
    }

    pauseAndClear(new Scanner(System.in));
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
        pauseAndClear(scanner);
    }

    private static boolean isOverdue(String taskLine) {
        try {
            int start = taskLine.indexOf("Due: ") + 5;
            int end = taskLine.indexOf(" ", start);
            if (start > 4 && end > start) {
                String dateStr = taskLine.substring(start, end).trim();
                LocalDate due = LocalDate.parse(dateStr);
                return LocalDate.now().isAfter(due);
            }
        } catch (Exception ignored) {}
        return false;
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
            sortTasksByPriority();
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
                    String clean = task.replaceAll("\u001B\\[[;\\d]*m", "");
                    writer.println("- [ ] " + clean);
                }
            }
            ColorText.success("🗒️ Exported to tasks.md");
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not export to Markdown.");
            pauseAndClear(new Scanner(System.in));
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

    // --- Sorting Helper ---
    private static void sortTasksByPriority() {
        tasks.sort((a, b) -> Integer.compare(priorityValue(b), priorityValue(a))); // HIGH → LOW
    }

    private static int priorityValue(String t) {
        if (t.contains("[HIGH]")) return 3;
        if (t.contains("[MED]")) return 2;
        if (t.contains("[LOW]")) return 1;
        return 0; // No priority
    }

    private static void showUpcoming() {
        ColorText.info("\n📅 Tasks due within 3 days:");
        boolean found = false;
        for (String t : tasks) {
            if (t.contains("Due: ")) {
                try {
                    int start = t.indexOf("Due: ") + 5;
                    int end = t.indexOf(" ", start);
                    LocalDate due = LocalDate.parse(t.substring(start, end).trim());
                    if (!LocalDate.now().isAfter(due) && !due.isAfter(LocalDate.now().plusDays(3))) {
                        System.out.println(ColorText.YELLOW + "⏰ " + t + ColorText.RESET);
                        found = true;
                    }
                } catch (Exception ignored) {}
            }
        }
        if (!found) System.out.println(ColorText.GREEN + "No tasks due soon!" + ColorText.RESET);
        pauseAndClear(new Scanner(System.in));
    }

    // --- Aesthetic Touches ---

    private static void retroBoot() {
        String[] loading = {"█▒▒▒▒▒▒▒▒▒ 10%", "███▒▒▒▒▒▒▒ 30%", "██████▒▒▒▒ 60%", "██████████ 100%"};
        for (String bar : loading) {
            System.out.print("\r" + ColorText.PINK+ "Loading... " + bar + ColorText.RESET);
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        System.out.println("\n" + ColorText.CYAN + "✨ Ready to roll! ✨" + ColorText.RESET);
    }

    private static void showQuote() {
        String[] quotes = {
            "Keep calm and code on 💻",
            "Progress, not perfection 🌈",
            "Retro vibes only ✨",
            "Every bug teaches you something 🐛",
            "One task at a time 🪩",
            "Stay groovy and productive! 🎸",
            "You are doing great! 🌟"
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


// --- Utility Helpers ---

    private static void pauseAndClear(Scanner scanner) {
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();           // wait for user
        clearScreen();                // clear terminal
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
