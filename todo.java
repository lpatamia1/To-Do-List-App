import java.util.ArrayList;
import java.util.Scanner;

public class RetroTodo {
    private static final ArrayList<String> tasks = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String PINK = "\u001B[95m";
    private static final String CYAN = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String GREEN = "\u001B[92m";
    private static final String PURPLE = "\u001B[94m";
    private static final String GRAY = "\u001B[90m";

    public static void main(String[] args) {
        showBanner();

        int choice;
        do {
            printMenu();
            System.out.print(PINK + "♡ Choose an option (1–5): " + RESET);
            while (!scanner.hasNextInt()) {
                System.out.println(YELLOW + "Oops! Enter a number please (1–5)." + RESET);
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // clear input

            switch (choice) {
                case 1 -> addTask();
                case 2 -> viewTasks();
                case 3 -> removeTask();
                case 4 -> clearTasks();
                case 5 -> exitApp();
                default -> System.out.println(YELLOW + "That’s not an option, silly! Try again. ♡" + RESET);
            }
        } while (choice != 5);
    }

    private static void showBanner() {
        System.out.println(PURPLE + "╔══════════════════════════════╗");
        System.out.println("║     ✨ RETRO TO-DO LIST ✨     ║");
        System.out.println("╚══════════════════════════════╝" + RESET);
        System.out.println(GRAY + "       “Stay organized, cutie!” 💾" + RESET);
    }

    private static void printMenu() {
        System.out.println(CYAN + "\n⋆｡°✩ MENU ✩°｡⋆" + RESET);
        System.out.println("1. Add a task ✍️");
        System.out.println("2. View tasks 📋");
        System.out.println("3. Remove a task ❌");
        System.out.println("4. Clear all 🧹");
        System.out.println("5. Exit 🌙");
    }

    private static void addTask() {
        System.out.print(GREEN + "Enter a new task: " + RESET);
        String task = scanner.nextLine();
        if (!task.isBlank()) {
            tasks.add(task);
            System.out.println(PINK + "💖 Task added: " + task + RESET);
        } else {
            System.out.println(YELLOW + "Hmm… you didn’t write anything!" + RESET);
        }
    }

    private static void viewTasks() {
        System.out.println(CYAN + "\n⋆｡°✩ YOUR TASKS ✩°｡⋆" + RESET);
        if (tasks.isEmpty()) {
            System.out.println(GRAY + "Nothing here yet! Add something cute ✨" + RESET);
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(PURPLE + (i + 1) + ". " + RESET + tasks.get(i));
            }
        }
    }

    private static void removeTask() {
        if (tasks.isEmpty()) {
            System.out.println(YELLOW + "No tasks to remove 💭" + RESET);
            return;
        }
        viewTasks();
        System.out.print(GREEN + "Which number to remove? " + RESET);
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index > 0 && index <= tasks.size()) {
            String removed = tasks.remove(index - 1);
            System.out.println(PINK + "❀ Removed: " + removed + RESET);
        } else {
            System.out.println(YELLOW + "That number doesn’t exist 💔" + RESET);
        }
    }

    private static void clearTasks() {
        tasks.clear();
        System.out.println(CYAN + "🧹 All clean! Your list is sparkling ✨" + RESET);
    }

    private static void exitApp() {
        System.out.println(PINK + "\nThanks for using Retro To-Do! 💾");
        System.out.println("Remember: you’re doing amazing. 🌸" + RESET);
    }
}
