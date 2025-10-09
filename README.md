## To-Do (Java Console App) 🧮
Terminal-style to-do list that feels like it came straight from a 90s PC.
Organize your day in a minimalist way — no mouse, no distractions, just text and charm.

## Test Coverage and Quality Metrics
![Tests](https://github.com/lpatamia1/To-Do-List-App/actions/workflows/test.yml/badge.svg)
![Coverage](https://img.shields.io/badge/Coverage-89%25-brightgreen)

- **Line Coverage:** 89%
- **Branch Coverage:** 64%
- **Frameworks:** JUnit 5 + JaCoCo
- **Continuous Integration:** GitHub Actions  
- Automated tests ensure data persistence, JSON serialization, markdown export, and task-sorting logic work flawlessly.

## Features
- Add and remove tasks with simple numbered prompts  
- View your current task list anytime  
- Automatic JSON persistence ('tasks.json') — your data survives restarts- Timestamp new and completed items  
- Terminal beeps and pastel color text for extra flair  
- Optional Markdown export (`tasks.md`)
- Daily stats tracking: tasks added and completed
- Lightweight JSON serialization with **Gson**
- Automatic backup creation  
- Unit-tested with **JUnit 5**

## Tech Stack
- **Language:** Java 21  
- **Build Tool:** Make  
- **Libraries:** Gson, JUnit 5, JaCoCo  
- **Testing:** Unit + Integration Tests  
- **CI/CD:** GitHub Actions (Java CI workflow)

## How It Works
You interact with the program through the command line:
```bash
1. Add a task
2. View all tasks
3. Mark complete
4. Show upcoming
5. Export Markdown
6. View Completed
7. Exit
```
## Setup & Run 
1. Using Makefile
```bash
make
```
2. Manual
```bash
javac -cp .:lib/gson-2.10.1.jar todo.java Task.java ColorText.java LocalDateAdapter.java
java  -cp .:lib/gson-2.10.1.jar todo
```
3. Runnable JAR
```bash
make package
java -jar todo.jar
```

## Example Output

```bash
🔧 Initializing Retro Environment...
Loading: Loading: [████████████████████████████████████████] 100%
Spinning floppy drive... 💾
Checking for Y2K bugs... 🧮
Compiling positive energy... ✨

✨ Ready to roll! ✨
📂 Loaded 4 task(s) from tasks.json
────────────────────────────────────────────
📅 You have 2 active tasks (1 due soon, 0 overdue)
🌟 You're on top of it!
╔══════════════════════════════════════╗
  ✨ Retro To-Do List ✨
╚══════════════════════════════════════╝
💬 One task at a time 🪩
────────────────────────────────────────────
1️⃣  Add Task
2️⃣  View Tasks
3️⃣  Mark Complete
4️⃣  Show Upcoming
5️⃣  Export Markdown
6️⃣  View Completed
7️⃣  Exit
Choose: 1
Enter task: Finish presentation slides
Set priority (H/M/L or blank): H
Enter due date (YYYY-MM-DD or blank): 2025-10-09
Task added!
Task saved to tasks.json

Press Enter to return to menu...
────────────────────────────────────────────
1️⃣  Add Task
2️⃣  View Tasks
3️⃣  Mark Complete
4️⃣  Show Upcoming
5️⃣  Export Markdown
6️⃣  View Completed
7️⃣  Exit
Choose: 2
📝 Your Tasks:
#   Task                                   Priority             Due Date
────────────────────────────────────────────────────────────────────────────
1   Finish presentation slides             🔥 [HIGH]             2025-10-09 ⏰
2   Clean workspace                        🌿 [LOW]              💤 [NO DUE DATE]

📊 2 total | 1 without due date | 1 added today | 0 completed today

Press Enter to return to menu...
────────────────────────────────────────────
1️⃣  Add Task
2️⃣  View Tasks
3️⃣  Mark Complete
4️⃣  Show Upcoming
5️⃣  Export Markdown
6️⃣  View Completed
7️⃣  Exit
Choose: 3

Press Enter to return to menu...
Enter number to complete: 1
✅ Completed: Finish presentation slides
Tasks saved to tasks.json

Press enter to return to menu...
────────────────────────────────────────────
1️⃣  Add Task
2️⃣  View Tasks
3️⃣  Mark Complete
4️⃣  Show Upcoming
5️⃣  Export Markdown
6️⃣  View Completed
7️⃣  Exit
Choose: 7
💾 Exiting… your tasks are saved!
📅 Today’s Stats: Added 1 | Completed 1
🗒️ Exported to tasks.md
📂 Serialized 2 tasks to tasks.json
✨ Goodbye! Stay groovy and productive! 🎸
```
---

## Bonus 
- Exports tasks to tasks.md for easy sharing or GitHub tracking
- Shows a random motivational quote each launch
- Plays terminal beeps for nostalgic feedback
- Keeps count of tasks added and completed today

## Development Highlights
- Achieved **89% line coverage** with JaCoCo
- Built with **modular Java classes and clear separation of logic/UI**
- Includes **CI pipeline** on GitHub Actions (auto-test every commit)
- Designed and tested in **GitHub Codespaces**