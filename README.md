## To-Do (Java Console App) 🧮
Terminal-style to-do list that feels like it came straight from a 90s PC.
Organize your day in a minimalist way — no mouse, no distractions, just text and charm.

## Features
- Add and remove tasks with simple numbered prompts  
- View your current task list anytime  
- Automatic JSON persistence ('tasks.json') — your data survives restarts- Timestamp new and completed items  
- Terminal beeps and pastel color text for extra flair  
- Optional Markdown export (`tasks.md`)
- Daily stats tracking: tasks added and completed
- Lightweight JSON serialization (manual parser, no external libraries)
- Automatic backup creation  

## How It Works
You interact with the program through the command line:
```bash
1. Add a task
2. View all tasks
3. Mark complete
4. Show upcoming
5. Export Markdown
6. Exit
```
## Setup & Run 
1. Save the code as:
```bash
todo.java
ColorText.java
Task.java
```

2. Compile and run:
```bash
javac ColorText.java todo.java Task.java
java todo
```
3. Follow the prompts and enjoy!
---
## Example Output

```bash
🔧 Initializing Retro Environment...
Loading: [██████████████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒] 100%
Spinning floppy drive... 💾

✨ Retro To-Do List ✨
💬 One task at a time 🪩

📂 Loaded 2 saved task(s).
────────────────────────────
1️⃣  Add Task
2️⃣  View Tasks
3️⃣  Mark Complete
4️⃣  Show Upcoming
5️⃣  Export Markdown
6️⃣  Exit
────────────────────────────
Choose: 1
Enter task: Finish presentation slides
Set priority (H/M/L or blank): H
Enter due date (YYYY-MM-DD or blank): 2025-10-09
✅ Task added!
─────────────────────────────────────────────
Choose: 2
📝 Your Tasks:
#   Task                                   Priority             Due Date
────────────────────────────────────────────────────────────────────────────
1   Finish presentation slides             🔥 [HIGH]             2025-10-09 ⏰
2   Clean workspace                        🌿 [LOW]              💤 [NO DUE DATE]

📊 2 total | 1 without due date | 1 added today | 0 completed today
─────────────────────────────────────────────
Press Enter to return to menu...

Choose: 3
Enter number to complete: 1
✅ Completed: Finish presentation slides
─────────────────────────────────────────────
Choose: 6
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
