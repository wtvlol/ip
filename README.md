# Groot project template

Groot is a simple Java chatbot that stores todos, deadlines, and events in memory. Use `todo DESCRIPTION`, `deadline DESCRIPTION /by DATE`, or `event DESCRIPTION /from START /to END` to add tasks. Enter `list` to display them, `mark TASK_NUMBER` or `unmark TASK_NUMBER` to update their status, `delete TASK_NUMBER` to remove one, and `bye` to exit. Dates and times are stored as text. Invalid commands and malformed task details produce an explanatory error without ending the program.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Groot.java` file, right-click it, and choose `Run Groot.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the following output:
   ```
   ____________________________________________________________
          \  |  /
        ___\_|_/___
       /   /   \   \
      /   | o o |    |
     |    |  ^  |    |
     |    \ \_/ /    |
      \    '---'    /
       \  |||||||  /
        | ||||||| |
     ___|_|||||||_|___
    /     |||||||     \
   /      |||||||      \
          |||||||
         /||| |||\
        /_||| |||_\
   Hello! I'm Groot.
   What can I do for you?
   ____________________________________________________________
   todo borrow book
   ____________________________________________________________
    Got it. I've added this task:
      [T][ ] borrow book
    Now you have 1 task in the list.
   ____________________________________________________________
   deadline return book /by Sunday
   ____________________________________________________________
    Got it. I've added this task:
      [D][ ] return book (by: Sunday)
    Now you have 2 tasks in the list.
   ____________________________________________________________
   event project meeting /from Mon 2pm /to 4pm
   ____________________________________________________________
    Got it. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
    Now you have 3 tasks in the list.
   ____________________________________________________________
   list
   ____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] borrow book
    2.[D][ ] return book (by: Sunday)
    3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
   ____________________________________________________________
   mark 2
   ____________________________________________________________
    Nice! I've marked this task as done:
      [D][X] return book (by: Sunday)
   ____________________________________________________________
   list
   ____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] borrow book
    2.[D][X] return book (by: Sunday)
    3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
   ____________________________________________________________
   delete 1
   ____________________________________________________________
    Noted. I've removed this task:
      [T][ ] borrow book
    Now you have 2 tasks in the list.
   ____________________________________________________________
   bye
   ____________________________________________________________
    Bye. Hope to see you again soon!
   ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## AI use

This project was developed with assistance from OpenAI Codex. AI was used to:

- review requirements and suggest suitable Java designs;
- help implement task deletion, collection-based storage, command enums, and error handling;
- draft and review console UI tests, including invalid and edge-case inputs;
- improve documentation and Git commit messages; and
- run checks that compare the program's actual output with its expected output.

All AI-generated suggestions were reviewed before use. The source code was inspected, and the recorded UI test plan was run with Java 25 to verify the resulting behavior. The project author remains responsible for the final implementation.
