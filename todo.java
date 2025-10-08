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
            String entry = task + " " + formattedPriority +
                (dueDate != null ? " | Due: " + dueDate : "") +
                " (added " + timestamp.format(new Date()) + ")";
            tasks.add(entry);
            addedToday++;
            saveTasks();
            beep();
            ColorText.success("Task added!");
        } else {
            beep();
            ColorText.warn("You can’t add an empty task!");
        }
        backupTasks();
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            ColorText.warn("🌈 No tasks yet!");
        } else {
            ColorText.info("\n📝 Your Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                String t = tasks.get(i);
                boolean overdue = t.contains("Due: ") && isOverdue(t);
                if (overdue) t += ColorText.RED + " ⚠️ Overdue!" + ColorText.RESET;
                System.out.println(ColorText.BLUE + (i + 1) + ". " + t + ColorText.RESET);
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
}
