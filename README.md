# Groot chatbot

Groot is a simple Java chatbot that saves todos, deadlines, and events between sessions. Use `todo DESCRIPTION`, `deadline DESCRIPTION /by yyyy-MM-dd`, or `event DESCRIPTION /from START /to END` to add tasks. Deadline dates are validated and displayed as `MMM dd yyyy`. Enter `list` to display tasks, `find KEYWORD` to search task descriptions, `mark TASK_NUMBER` or `unmark TASK_NUMBER` to update their status, `delete TASK_NUMBER` to remove one, and `bye` to exit. Invalid commands and malformed task details produce an explanatory error without ending the program.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/groot/Groot.java` file, right-click it, and choose `Run Groot.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the following output:
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
   deadline return book /by 2019-12-02
   ____________________________________________________________
    Got it. I've added this task:
      [D][ ] return book (by: Dec 02 2019)
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
    2.[D][ ] return book (by: Dec 02 2019)
   3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
   ____________________________________________________________
   find book
   ____________________________________________________________
    Here are the matching tasks in your list:
    1.[T][ ] borrow book
    2.[D][ ] return book (by: Dec 02 2019)
   ____________________________________________________________
   mark 2
   ____________________________________________________________
    Nice! I've marked this task as done:
      [D][X] return book (by: Dec 02 2019)
   ____________________________________________________________
   list
   ____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] borrow book
    2.[D][X] return book (by: Dec 02 2019)
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

## Package structure

The Java source root remains `src/main/java`. Classes are grouped by responsibility beneath the `groot` root package:

- `groot`: application entry point and coordination;
- `groot.exception`: application-specific exceptions;
- `groot.parser`: command recognition and input parsing;
- `groot.storage`: loading and saving tasks; and
- `groot.task`: task models and task-list operations.

Keep `src/main/java` as the Java source root. Package folders must remain inside that directory so Java and project tools can find them correctly.

## Building and running the fat JAR

The Shadow plugin packages Groot and its runtime dependencies into one executable fat JAR. From the project root, create a fresh JAR on macOS or Linux with:

```shell
./gradlew clean shadowJar
```

On Windows, use:

```shell
gradlew.bat clean shadowJar
```

The generated file is located at `build/libs/groot.jar`. Run it using Java 25:

```shell
java -jar build/libs/groot.jar
```

Groot resolves its `data/groot.txt` path relative to the directory from which the JAR is run. Run the command from the project root to use the project's existing `data` directory.

## AI use

This project was developed with assistance from OpenAI Codex. AI was used to:

- review requirements and suggest suitable Java designs;
- help implement task deletion, task searching, collection-based storage, command enums, and error handling;
- organize the Java classes into responsibility-based packages and update their imports;
- configure and verify Gradle fat-JAR packaging;
- create and apply project-specific SE-EDU Java and Git standard skills;
- draft and review JUnit and console UI tests, including invalid and edge-case inputs;
- diagnose a JUnit functional-interface warning and add no-match coverage for task searching;
- improve Javadoc, user-facing documentation, and Git commit messages; and
- run checks that compare the program's actual output with its expected output.

All AI-generated suggestions were reviewed before use. The source code was inspected, and the recorded UI test plan was run with Java 25 to verify the resulting behavior. The project author remains responsible for the final implementation.

The JUnit coverage target is the approximately 50% highest-value methods, prioritizing complex, core, and critical business logic over trivial accessors. After each code change, the affected classes and their JUnit tests must be reviewed and the tests updated as needed to continue meeting this target.
