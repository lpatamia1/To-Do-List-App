## To-Do (Java Console App) 🧮
Terminal-style to-do list that feels like it came straight from a 90s PC.
Organize your day in a minimalist way — no mouse, no distractions, just text and charm.

## Features
- Add and remove tasks with simple numbered prompts  
- View your current task list anytime  
- Auto-save tasks between sessions (`tasks.txt`)  
- Timestamp new and completed items  
- Terminal beeps and pastel color text for extra flair  
- Optional Markdown export (`tasks.md`)  
- Automatic backup creation  

## How It Works
You interact with the program through the command line:
```bash
1. Add a task
2. View all tasks
3. Mark complete
4. Export Markdown
5. Exit
```
## Setup & Run 
1. Save the code as:
```bash
ToDo.java
ColorText.java
```

2. Compile and run:
```bash
javac ColorText.java ToDo.java
java ToDo
```
3. Follow the prompts and enjoy!
---
## Example Output

```bash
✨ Retro To-Do List ✨
💬 One task at a time 🪩

📂 Loaded 2 saved task(s).
────────────────────────────
1️⃣ Add Task
2️⃣ View Tasks
3️⃣ Mark Complete
4️⃣ Exit
────────────────────────────
Choose: 1
Enter task: Finish README styling
✅ Task added! (added 2025-10-06 14:31:22)
────────────────────────────
Choose: 2
📝 Your Tasks:
1. Finish README styling (added 2025-10-06 14:31:22)
2. Email project update (added 2025-10-05 09:47:13)
────────────────────────────
Choose: 3
Enter number to complete: 1
✅ Done: Finish README styling (completed 2025-10-06 14:32:04)
────────────────────────────
Choose: 4
💾 Exiting… your tasks are saved!
🔔 *beep* 
────────────────────────────
📦 Backup saved: tasks_backup_1738889513.txt
Goodbye! 🌈 Keep calm and code on 💻
```
---

## Bonus 
- Exports tasks to tasks.md for easy sharing or GitHub tracking
- Shows a random motivational quote each launch
- Plays terminal beeps for nostalgic feedback
- Keeps count of tasks added and completed today
