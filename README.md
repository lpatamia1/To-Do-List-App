<div align="center">

## To-Do (Java Console App) 🧮
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![JUnit5](https://img.shields.io/badge/Tests-JUnit_5-007396?style=for-the-badge&logo=java&logoColor=white)
![JaCoCo](https://img.shields.io/badge/Coverage-89%25-brightgreen?style=for-the-badge&logo=Codecov&logoColor=white)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-2E7D32?style=for-the-badge)


</div>

Terminal-style to-do list that feels like it came straight from a 90s PC.
Organize your day in a minimalist way — no mouse, no distractions, just text and charm.

---

<div align="center">

### Test Coverage & Quality Metrics

</div>

- **Line Coverage:** 89%
- **Branch Coverage:** 64%
- **Frameworks:** JUnit 5 + JaCoCo
- **Continuous Integration:** GitHub Actions  
- Automated tests ensure data persistence, JSON serialization, markdown export, and task-sorting logic work flawlessly.

---

<div align="center">

## Features

</div>

- Add and remove tasks with simple numbered prompts  
- View your current task list anytime  
- Automatic JSON persistence (`tasks.json`) — your data survives restarts- Timestamp new and completed items  
- Optional Markdown export (`tasks.md`)
- Daily stats tracking: tasks added and completed
- Lightweight JSON serialization with **Gson**
- Automatic backup creation  
- Unit-tested with **JUnit 5**
  
<div align="center">

</div>

---

<div align="center">

## Tech Stack Overview

<table>
  <tr>
    <th>Category</th>
    <th>Technology</th>
    <th>Purpose</th>
  </tr>
  <tr>
    <td><b>Language</b></td>
    <td>Java 21</td>
    <td>Implements core application logic and task management.</td>
  </tr>
  <tr>
    <td><b>Build Tool</b></td>
    <td>Make</td>
    <td>Automates compilation, packaging, and execution tasks.</td>
  </tr>
  <tr>
    <td><b>Libraries</b></td>
    <td>Gson, JUnit 5, JaCoCo</td>
    <td>Gson for JSON serialization; JUnit & JaCoCo for testing and coverage.</td>
  </tr>
  <tr>
    <td><b>Testing</b></td>
    <td>Unit + Integration Tests</td>
    <td>Ensures reliability across core logic and cross-module functionality.</td>
  </tr>
  <tr>
    <td><b>CI/CD</b></td>
    <td>GitHub Actions (Java CI workflow)</td>
    <td>Automates build, test, and coverage reporting for every commit.</td>
  </tr>
</table>

</div>

---
  
<div align="center">

## How It Works

</div>

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

---

<div align="center">

## Setup & Run 

</div>

**Options**
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

---

<div align="center">

## Example Output

</div>

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
        🌸 Retro To-Do List 🌸
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

<div align="center">

## Bonus 

</div>

- Exports tasks to `tasks.md` for easy sharing or GitHub tracking
- Shows a random motivational quote each launch
- Keeps count of tasks added and completed today

---

<div align="center">

## Development Highlights

</div>

- Achieved **89% line coverage** with JaCoCo
- Built with **modular Java classes and clear separation of logic/UI**
- Includes **CI pipeline** on GitHub Actions (auto-test every commit)
- Designed and tested in **GitHub Codespaces**

---

<div align="center">

## Lessons Learned

</div>

- Early versions broke silently after adding new features — tests caught those regressions instantly once CI was added.
- What started as a simple text to-do list evolved into a polished, maintainable project through continuous iteration and testing.
- Watching GitHub automatically compile, test, and report coverage builds real trust in the codebase.
- Combining retro design with modern tooling (JUnit, JaCoCo, GitHub Actions) creates a bridge between nostalgia and reliability — fun and functional.

---

<div align="center"> Built with ☕, 💙, and a passion for clean code.  
  
<sub>Created by Lily Patamia — blending Java craftsmanship and creativity 🌸</sub>
</div>
